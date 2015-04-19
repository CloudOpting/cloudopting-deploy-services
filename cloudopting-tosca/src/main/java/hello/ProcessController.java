package hello;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ProcessController {

	@Autowired
	private ProcessService processService;

	@RequestMapping(value="/process", method= RequestMethod.POST,headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody void startProcessInstance(@RequestParam(value="customerId", required=false) String customerId,@RequestParam(value="cloudId", required=false) String cloudId) {
		System.out.println("\ncustomerId:"+customerId);
		System.out.println("\ncloudId:"+cloudId);
		processService.startProcess(customerId, cloudId);
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
		List<Task> tasks = processService.getTasks(assignee);
		List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
		for (Task task : tasks) {
			dtos.add(new TaskRepresentation(task.getId(), task.getName()));
		}
		return dtos;
	}

	static class TaskRepresentation {

		private String id;
		private String name;

		public TaskRepresentation(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}
