package eu.cloudopting.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class CloudoptingProcessVMinstalled implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Sono nella classe CloudoptingProcessVMinstalled");
		
		execution.setVariable("vmInstalled", true);
	}

}
