package eu.cloudopting.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import eu.cloudopting.docker.DockerManager;

public class CloudoptingProcessDockerBuild implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Sono nella classe CloudoptingProcessDockerBuild");
		String toscaFile = (String) execution.getVariable("toscaFile");
		System.out.println("la variabile toscaFile vale:"+toscaFile);
		String dockerNode = (String) execution.getVariable("dockerNode");
		System.out.println("I will create the Dockerfile for :"+dockerNode);
		String customer = (String) execution.getVariable("customer");
		String service = (String) execution.getVariable("service");
		String dockerContextPath = (String) execution.getVariable("dockerContextPath");
		
		DockerManager dm = new DockerManager();
		dm.getDockerVersion();
		
		dm.buildDockerImage(customer, service, dockerNode,dockerContextPath);
		
	}
	
	

}
