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
		
		
		// TODO here we need a mean to create the VM
		System.out.println("I'm creating the VM");
		
		
		// TODO here we prepare the array to create the containers )will have to create the correct ordered array
		System.out.println("Now prepare to create the Containers");
		toscaFileManager = ToscaFileManager.getInstance();
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
		execution.setVariable("dockerNodesList", dockerNodesList);
		execution.setVariable("vmPortsList", dockerPortsList);
		
		
	}

}
