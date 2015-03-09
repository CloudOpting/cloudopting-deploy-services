package eu.cloudopting.activiti;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.xml.dtm.ref.DTMNodeList;

import eu.cloudopting.tosca.transformer.ToscaFileManager;

public class CloudoptingProcessVM implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Sono nella classe java");
		String toscaFile = (String) execution.getVariable("toscaFile");
		System.out.println("la variabile toscaFile vale:"+toscaFile);
		
//		String filePath = new String("ClearoExample.xml");
		String xml = null;
		try {
			xml = new String(Files.readAllBytes(Paths.get(toscaFile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// With the retrieved XML we instantiate the ToscaFileManager that is the only one that know how to read it
		ToscaFileManager tfm = new ToscaFileManager(xml);
		
		// TODO here we need a mean to create the VM
		System.out.println("I'm creating the VM");
		
		
		// TODO here we prepare the array to create the containers
		System.out.println("Now prepare to create the Containers");
		
		DTMNodeList nodes = tfm.getNodeByType("DockerContainer");
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
		
		
//		dockerNodesList.add("Nodo1");
//		dockerNodesList.add("Nodo2");
//		dockerNodesList.add("Nodo3");
		execution.setVariable("dockerNodesList", dockerNodesList);
		
	}

}
