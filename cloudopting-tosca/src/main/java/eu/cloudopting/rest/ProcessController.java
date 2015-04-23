package eu.cloudopting.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ProcessController {

	@Autowired
	private ProcessService processService;

	@RequestMapping(value="/process", method= RequestMethod.POST,headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody String startProcessInstance(@RequestParam(value="customerId", required=false) String customerId,@RequestParam(value="cloudId", required=false) String cloudId,@RequestParam(value="toscaId", required=false) String toscaId) {
		String pid = processService.startProcess(customerId, cloudId, toscaId);
		System.out.println("returning pid: "+pid);
		return pid;
	}

	@RequestMapping(value="/testProcess", method= RequestMethod.POST,headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody void testProcessInstance(@RequestParam(value="customerId", required=false) String customerId,@RequestParam(value="cloudId", required=false) String cloudId,@RequestParam(value="toscaId", required=false) String toscaId) {
		processService.testProcess(customerId, cloudId, toscaId);
	}

	@RequestMapping(value = "/processImage/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] getProcessStatusImage(@PathVariable String id, final HttpServletResponse response){
		if (id == null){
			return null;
		}
		response.setHeader("Cache-Control", "no-cache");
		InputStream is = processService.getProcessImage(id);

		// Prepare buffered image.
        BufferedImage img;
		try {
			img = ImageIO.read(is);
	        // Create a byte array output stream.
	        ByteArrayOutputStream bao = new ByteArrayOutputStream();

	        // Write to output stream
	        ImageIO.write(img, "png", bao);

	        return bao.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

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
