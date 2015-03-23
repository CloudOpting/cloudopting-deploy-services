package eu.cloudopting.docker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DockerManager {
	public void getDockerVersion() throws IOException, InterruptedException
	  {
	    // build the system command we want to run
	    List<String> commands = new ArrayList<String>();
	    commands.add("/bin/sh");
	    commands.add("-c");
	    commands.add("docker --version");

	    // execute the command
	    SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
	    int result = commandExecutor.executeCommand();

	    // get the stdout and stderr from the command that was run
	    StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
	    StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();
	    
	    // print the stdout and stderr
	    System.out.println("The numeric result of the command was: " + result);
	    System.out.println("STDOUT:");
	    System.out.println(stdout);
	    System.out.println("STDERR:");
	    System.out.println(stderr);
	  }
	
	
	public void buildDockerImage(String customer, String service, String dockerfile){
	    // build the system command we want to run
	    List<String> commands = new ArrayList<String>();
	    commands.add("/bin/sh");
	    commands.add("-c");
	    commands.add("docker build -t cloudopting/"+customer+" -f "+customer+"-"+service+"/"+dockerfile+".dockerfile");

	    // execute the command
	    SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
	    int result = 0;
		try {
			result = commandExecutor.executeCommand();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // get the stdout and stderr from the command that was run
	    StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
	    StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();
	    
	    // print the stdout and stderr
	    System.out.println("The numeric result of the command was: " + result);
	    System.out.println("STDOUT:");
	    System.out.println(stdout);
	    System.out.println("STDERR:");
	    System.out.println(stderr);
		
	}
}
