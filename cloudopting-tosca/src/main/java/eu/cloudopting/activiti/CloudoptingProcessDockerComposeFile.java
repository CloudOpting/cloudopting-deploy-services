package eu.cloudopting.activiti;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class CloudoptingProcessDockerComposeFile implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Sono nella classe CloudoptingProcessDockerComposeFile");
		String toscaFile = (String) execution.getVariable("toscaFile");
		System.out.println("la variabile toscaFile vale:"+toscaFile);
		String dockerNode = (String) execution.getVariable("dockerNode");
		System.out.println("I will create the Dockerfile for :"+dockerNode);
		String creationPath = (String) execution.getVariable("creationPath");
		String customer = (String) execution.getVariable("customer");
		ArrayList<String> dockerNodesList = (ArrayList<String>) execution.getVariable("dockerNodesList");

		
		ArrayList<HashMap<String, String>> modData = new ArrayList<HashMap<String, String>>();
		for (String node : dockerNodesList) {
			HashMap<String, String> containerData = new HashMap<String, String>();
			containerData.put("container", node);
			containerData.put("image", "cloudopting/"+customer+"_"+node);
//			modData.add(toscaFileManager.getPuppetModulesProperties(mod));
			System.out.println(node);
			modData.add(containerData);
		}
		System.out.println(modData.toString());
		
		HashMap<String, Object> templData = new HashMap<String, Object>();
		templData.put("dockerContainers", modData);
		// write the "Puppetfile" file
		Configuration cfg = new Configuration();
		Template tpl = null;
		try {
			tpl = cfg.getTemplate("docker-compose.ftl");
		} catch (TemplateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		OutputStreamWriter outputTempl = new OutputStreamWriter(System.out);
//		FileOutputStream outFile = new FileOutputStream("the-file-name");
		PrintWriter outFile = null;
		String composeFile = new String("docker-compose.yml");
		try {
			outFile = new PrintWriter(creationPath+"/"+composeFile, "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//tpl.process(nodeData, outputTempl);
			tpl.process(templData, outFile);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
