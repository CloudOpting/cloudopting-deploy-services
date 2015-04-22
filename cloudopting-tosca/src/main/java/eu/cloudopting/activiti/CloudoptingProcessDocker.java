package eu.cloudopting.activiti;

import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import eu.cloudopting.tosca.nodes.CloudOptingNodeImpl;
import eu.cloudopting.tosca.transformer.ToscaFileManager;

public class CloudoptingProcessDocker implements JavaDelegate {
	
	@Autowired
	ToscaFileManager tfm;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
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
		String customer = (String) execution.getVariable("customer");
		

		CloudOptingNodeImpl dc = new CloudOptingNodeImpl();
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("id", dockerNode);
		hm.put("creationPath", creationPath);
		hm.put("servicePath", servicePath);
		hm.put("toscaPath", "tosca/");
		hm.put("customer", customer);
		dc.execute(hm);
	}

}
