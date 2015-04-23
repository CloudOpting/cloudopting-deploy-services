package eu.cloudopting.rest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scala.sys.process.processInternal;

@Service
public class ProcessService {

	
	@Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    protected ProcessEngineConfiguration processEngineConfiguration;
 //   @Autowired
//    private ProcessDiagramGenerator pdg;

	@Transactional
    public String startProcess(String customerId, String cloudId, String toscaId) {
		System.out.println("\ncustomerId:"+customerId);
		System.out.println("\ncloudId:"+cloudId);
		HashMap<String, Object> v = new HashMap<String, Object>();
		v.put("toscaFile", toscaId);
		v.put("customer", customerId);
		v.put("cloud", cloudId);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("cloudoptingProcess",v);
        System.out.println("ProcessID:"+pi.getProcessInstanceId());
        return pi.getProcessInstanceId();
    }

	@Transactional
    public String testProcess(String customerId, String cloudId, String toscaId) {
		System.out.println("\ncustomerId:"+customerId);
		System.out.println("\ncloudId:"+cloudId);
		HashMap<String, Object> v = new HashMap<String, Object>();
		v.put("toscaFile", toscaId);
		v.put("customer", customerId);
		v.put("cloud", cloudId);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("cloudoptingTestProcess",v);
        return pi.getProcessInstanceId();
    }

	public InputStream getProcessImage(String id){
		System.out.println("ProcessID:"+id);
		ProcessInstance pi;
	//	pi.g
		pi = runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
		if (pi == null){
			return null;
		}
		System.out.println("Process execution:"+pi.getId());
		BpmnModel bmod = processEngine.getRepositoryService().getBpmnModel(pi.getProcessDefinitionId());
		ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
		List<String> actact = runtimeService.getActiveActivityIds(id);
		System.out.println("ActiveActivities:"+actact.toString());
		return new DefaultProcessDiagramGenerator().generateDiagram(bmod,"png",actact);
//		return diagramGenerator.generateDiagram(bmod,"png",runtimeService.getActiveActivityIds(id));
	}
	
	@Transactional
    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().list();
    }
	
	
}
