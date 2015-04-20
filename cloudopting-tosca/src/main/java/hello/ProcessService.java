package hello;

import java.util.HashMap;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessService {

	
	@Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

	@Transactional
    public void startProcess(String customerId, String cloudId, String toscaId) {
		System.out.println("\ncustomerId:"+customerId);
		System.out.println("\ncloudId:"+cloudId);
		HashMap<String, Object> v = new HashMap<String, Object>();
		v.put("toscaFile", toscaId);
		v.put("customer", customerId);
		v.put("cloud", cloudId);
        runtimeService.startProcessInstanceByKey("cloudoptingProcess",v);
    }

	@Transactional
    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().list();
    }
	
	
}
