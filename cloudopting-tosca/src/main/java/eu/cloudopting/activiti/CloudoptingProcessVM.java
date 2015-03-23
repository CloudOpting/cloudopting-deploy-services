package eu.cloudopting.activiti;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import eu.cloudopting.tosca.transformer.IToscaFileManager;
import eu.cloudopting.tosca.transformer.ToscaFileManager;

public class CloudoptingProcessVM implements JavaDelegate {
	
	@Autowired
	ToscaFileManager toscaFileManager;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Sono nella classe java");
		String toscaFile = (String) execution.getVariable("toscaFile");
		System.out.println("la variabile toscaFile vale:"+toscaFile);
		String customer = (String) execution.getVariable("customer");
		
//		String filePath = new String("ClearoExample.xml");
		String xml = null;
		try {
			xml = new String(Files.readAllBytes(Paths.get(toscaFile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// With the retrieved XML we instantiate the ToscaFileManager that is the only one that know how to read it
//		ToscaFileManager tfm = new ToscaFileManager(xml);
		toscaFileManager = ToscaFileManager.getInstance();
		toscaFileManager.setToscaFile(xml);
		
		// TODO here we need a mean to create the VM
		System.out.println("I'm creating the VM");
		
		
		// TODO here we prepare the array to create the containers )will have to create the correct ordered array
		System.out.println("Now prepare to create the Containers");
		
		DTMNodeList nodes = toscaFileManager.getNodeByType("DockerContainer");
		ArrayList<String> dockerNodesList = new ArrayList<String>();
		System.out.println("before cycle");
		for (int i = 0; i < nodes.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());
			System.out.println(nodes.item(i).getAttributes().getNamedItem("id")
					.getNodeValue());
			dockerNodesList.add(nodes.item(i).getAttributes().getNamedItem("id")
					.getNodeValue());
		}
		
		ArrayList<String> dockerPortsList = new ArrayList<String>();
		dockerPortsList.add("Port1");
//		dockerNodesList.add("Nodo2");
//		dockerNodesList.add("Nodo3");
		execution.setVariable("dockerNodesList", dockerNodesList);
		execution.setVariable("vmPortsList", dockerPortsList);
		
		// preparing the Puppet env
		String serviceName = toscaFileManager.getServiceName();
		String dir = new String(customer+"-"+serviceName);

        // Creating new directory in Java, if it doesn't exists
		boolean success = false;
        File directory = new File(dir);
        if (directory.exists()) {
            System.out.println("Directory already exists ...");

        } else {
            System.out.println("Directory not exists, creating now");

            success = directory.mkdir();
            if (success) {
                System.out.printf("Successfully created new directory : %s%n", dir);
            } else {
                System.out.printf("Failed to create new directory: %s%n", dir);
            }
        }
        execution.setVariable("creationPath", dir);
        execution.setVariable("service", serviceName);
	}

}
