package eu.cloudopting.activiti;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import eu.cloudopting.tosca.transformer.ToscaFileManager;

public class CloudoptingProcessSetup implements JavaDelegate {
	@Autowired
	ToscaFileManager toscaFileManager;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("In CloudoptingProcessSetup");
		String toscaFile = (String) execution.getVariable("toscaFile");
		System.out.println("toscaFile :" + toscaFile);
		String customer = (String) execution.getVariable("customer");
		System.out.println("customer :"+customer);
		String cloud = (String) execution.getVariable("cloud");
		System.out.println("cloud :"+cloud);
		
		String xml = null;
		try {
			xml = new String(Files.readAllBytes(Paths.get(toscaFile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// With the retrieved XML we instantiate the ToscaFileManager that is
		// the only one that know how to read it
		toscaFileManager = ToscaFileManager.getInstance();
		toscaFileManager.setToscaFile(xml);
		// preparing the Puppet env
		String serviceName = toscaFileManager.getServiceName();
		String dockerContextPath = new String("/cloudOptingData");
		String dir = new String(dockerContextPath+"/"+customer + "-" + serviceName);

		// Creating new directory in Java, if it doesn't exists
		boolean success = false;
		File directory = new File(dir);
		if (directory.exists()) {
			System.out.println("Directory already exists ...");

		} else {
			System.out.println("Directory not exists, creating now");

			success = directory.mkdir();
			if (success) {
				System.out.printf("Successfully created new directory : %s%n",
						dir);
			} else {
				System.out.printf("Failed to create new directory: %s%n", dir);
			}
		}
		execution.setVariable("creationPath", dir);
		execution.setVariable("dockerContextPath", dockerContextPath);
		execution.setVariable("service", serviceName);
		execution.setVariable("servicePath", customer + "-" + serviceName);
		

	}

}
