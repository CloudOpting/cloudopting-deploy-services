package eu.cloudopting.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import eu.cloudopting.file.FileSystemManager;

public class CloudoptingProcessR10K implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("I'm processing the R10K Puppetfile to download all the puppet's modules");
		String toscaFile = (String) execution.getVariable("toscaFile");
//		System.out.println("la variabile toscaFile vale:"+toscaFile);
		String customer = (String) execution.getVariable("customer");
		String service = (String) execution.getVariable("service");
		String dockerContextPath = (String) execution.getVariable("dockerContextPath");
		

		FileSystemManager dm = new FileSystemManager();
		dm.getDockerVersion();
		dm.runR10K(customer, service, dockerContextPath);
	}

}
