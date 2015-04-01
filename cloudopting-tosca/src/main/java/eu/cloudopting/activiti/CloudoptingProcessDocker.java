package eu.cloudopting.activiti;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import eu.cloudopting.tosca.nodes.CloudOptingNode;
import eu.cloudopting.tosca.nodes.DockerContainer;
import eu.cloudopting.tosca.transformer.ToscaFileManager;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class CloudoptingProcessDocker implements JavaDelegate {
	
	@Autowired
	ToscaFileManager tfm;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("CloudoptingProcessDocker");
		String resultVar = (String) execution.getVariable("resultVar");
//		System.out.println("result from shell execution:"+resultVar);
		String toscaFile = (String) execution.getVariable("toscaFile");
//		System.out.println("toscaFile :"+toscaFile);
		String dockerNode = (String) execution.getVariable("dockerNode");
		System.out.println("I will create the Dockerfile for :"+dockerNode);
		String creationPath = (String) execution.getVariable("creationPath");
//		System.out.println("creationPath :"+creationPath);
		String servicePath = (String) execution.getVariable("servicePath");
//		System.out.println("servicePath :"+servicePath);
		
		CloudOptingNode dc = new DockerContainer();
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("id", dockerNode);
		hm.put("creationPath", creationPath);
		hm.put("servicePath", servicePath);
		dc.prepare(hm);
		/*
		// Add the values in the datamodel

		Map datamodel = new HashMap();

		datamodel.put("pet", "Bunny");
		Configuration cfg = new Configuration();

		Template tpl = cfg.getTemplate("Dockerfile.ftl");

		OutputStreamWriter output = new OutputStreamWriter(System.out);


		tpl.process(datamodel, output);
		/// getting the Hashmap from the TOSCA file of the properties
		// getting from the TOSCA FILE THE TREMPLATE FOR THE NODE
		String xml = null;
		try {
			xml = new String(Files.readAllBytes(Paths.get(toscaFile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		// With the retrieved XML we instantiate the ToscaFileManager that is the only one that know how to read it
//		ToscaFileManager tfm = new ToscaFileManager(xml);
/*		tfm = ToscaFileManager.getInstance();

		String myTemplate = tfm.getTemplateForNode("ClearoApacheVH","PuppetTemplate");
		System.out.println("The template is :"+myTemplate);
		Map nodeData = tfm.getPropertiesForNode("ClearoApacheVH");
//		System.out.println("Data :"+nodeData.toString());
		Template tpl1 = cfg.getTemplate(myTemplate+".ftl");

		OutputStreamWriter outputTempl = new OutputStreamWriter(System.out);
		tpl1.process(nodeData, outputTempl);
		*/
	}

}
