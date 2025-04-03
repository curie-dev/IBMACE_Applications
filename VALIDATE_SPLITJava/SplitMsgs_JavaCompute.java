import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbJSON;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;

public class SplitMsgs_JavaCompute extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage();
            
            // Access the Environment.Variables.student[] array
            MbElement envSt = inAssembly.getGlobalEnvironment().getRootElement().getFirstElementByPath("/Variables");
            MbElement locEnv = inAssembly.getLocalEnvironment().getRootElement().getFirstChild();
            MbElement fl = locEnv.createElementAsFirstChild(MbElement.TYPE_NAME,"File",null).createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,"Name",null);
            
           // String loEnv = locEnv.
            //MbElement envStudents = environment.getRootElement().getFirstElementByPath("/Variables/*");
            if (envSt != null) {
                MbElement student = envSt.getFirstChild();
                
                while (student != null) {
                    // Create a JSON object for each student
                	
        			MbElement outRoot = outMessage.getRootElement();
                    MbElement jsonData = outRoot.createElementAsLastChild(MbJSON.PARSER_NAME);
                    
                    // Create the JSON structure
                    MbElement jsonRoot = jsonData.createElementAsLastChild(MbJSON.OBJECT, "Data", null);
                	MbElement studentArray = jsonRoot.createElementAsLastChild(MbJSON.OBJECT, "student", null);
                    MbElement studentObject = studentArray.createElementAsLastChild(MbJSON.OBJECT, null, null);
                    String Name = student.getFirstElementByPath("Name").getValueAsString();
                    String FileName = Name.replace(" ", "_")+".json";
                    String Branch = student.getFirstElementByPath("Branch").getValueAsString();
                    String Age = student.getFirstElementByPath("Age").getValueAsString();
                    String City = student.getFirstElementByPath("City").getValueAsString();
                    studentObject.createElementAsLastChild(MbJSON.OBJECT, "Name", Name);
                    studentObject.createElementAsLastChild(MbJSON.OBJECT, "Branch", Branch);
                    studentObject.createElementAsLastChild(MbJSON.OBJECT, "Age", Age);
                    studentObject.createElementAsLastChild(MbJSON.OBJECT, "City", City);
                    outAssembly = new MbMessageAssembly(inAssembly, outMessage);
                    fl.setValue(FileName);
                    alt.propagate(outAssembly);
                    outAssembly = null;
                    outRoot.delete();
                    student = student.getNextSibling();
                }
            }
            outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);			
			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(), null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

	/**
	 * onPreSetupValidation() is called during the construction of the node
	 * to allow the node configuration to be validated.  Updating the node
	 * configuration or connecting to external resources should be avoided.
	 *
	 * @throws MbException
	 */
	@Override
	public void onPreSetupValidation() throws MbException {
	}

	/**
	 * onSetup() is called during the start of the message flow allowing
	 * configuration to be read/cached, and endpoints to be registered.
	 *
	 * Calling getPolicy() within this method to retrieve a policy links this
	 * node to the policy. If the policy is subsequently redeployed the message
	 * flow will be torn down and reinitialized to it's state prior to the policy
	 * redeploy.
	 *
	 * @throws MbException
	 */
	@Override
	public void onSetup() throws MbException {
	}

	/**
	 * onStart() is called as the message flow is started. The thread pool for
	 * the message flow is running when this method is invoked.
	 *
	 * @throws MbException
	 */
	@Override
	public void onStart() throws MbException {
	}

	/**
	 * onStop() is called as the message flow is stopped. 
	 *
	 * The onStop method is called twice as a message flow is stopped. Initially
	 * with a 'wait' value of false and subsequently with a 'wait' value of true.
	 * Blocking operations should be avoided during the initial call. All thread
	 * pools and external connections should be stopped by the completion of the
	 * second call.
	 *
	 * @throws MbException
	 */
	@Override
	public void onStop(boolean wait) throws MbException {
	}

	/**
	 * onTearDown() is called to allow any cached data to be released and any
	 * endpoints to be deregistered.
	 *
	 * @throws MbException
	 */
	@Override
	public void onTearDown() throws MbException {
	}

}
