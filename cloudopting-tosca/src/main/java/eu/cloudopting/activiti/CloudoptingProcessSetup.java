package eu.cloudopting.activiti;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import eu.cloudopting.tosca.transformer.ToscaFileManager;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class CloudoptingProcessSetup implements JavaDelegate {
	@Autowired
	ToscaFileManager toscaFileManager;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("In CloudoptingProcessSetup");
		String toscaFile = (String) execution.getVariable("toscaFile");
		System.out.println("toscaFile :" + toscaFile);
		String customer = (String) execution.getVariable("customer");
		System.out.println("customer :" + customer);
		String cloud = (String) execution.getVariable("cloud");
		System.out.println("cloud :" + cloud);
		
		

		String xml = null;
		try {
			xml = new String(Files.readAllBytes(Paths.get(toscaFile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// With the retrieved XML we instantiate the ToscaFileManager that is
		// the only one that know how to read it
		toscaFileManager = ToscaFileManager.getInstance();
		toscaFileManager.setToscaFile(xml);
		// preparing the Puppet env
		String serviceName = toscaFileManager.getServiceName();
		String dockerContextPath = new String("/cloudOptingData");
		String dir = new String(dockerContextPath + "/" + customer + "-"
				+ serviceName);

		// Creating new directory in Java, if it doesn't exists
		boolean success = false;
		File directory = new File(dir);
		if (directory.exists()) {
			System.out.println("Directory already exists ...");

		} else {
			System.out.println("Directory not exists, creating now");

			success = directory.mkdir();
			if (success) {
				System.out.printf("Successfully created new directory : %s%n",
						dir);
			} else {
				System.out.printf("Failed to create new directory: %s%n", dir);
			}
		}

		// getting the list of puppet modules that this service needs
		ArrayList<String> modules = toscaFileManager.getPuppetModules();
		ArrayList<HashMap<String, String>> modData = new ArrayList<HashMap<String, String>>();
		for (String mod : modules) {
			modData.add(toscaFileManager.getPuppetModulesProperties(mod));
			System.out.println(mod);
		}
		System.out.println(modData.toString());
		
		HashMap<String, Object> templData = new HashMap<String, Object>();
		templData.put("modData", modData);
		// write the "Puppetfile" file
		Configuration cfg = new Configuration();
		Template tpl = null;
		try {
			tpl = cfg.getTemplate("Puppetfile.ftl");
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
		String puppetFile = new String("Puppetfile");
		try {
			outFile = new PrintWriter(dir+"/"+puppetFile, "UTF-8");
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

		// setting the variables for the rest of the tasks
		execution.setVariable("creationPath", dir);
		execution.setVariable("dockerContextPath", dockerContextPath);
		execution.setVariable("service", serviceName);
		execution.setVariable("servicePath", customer + "-" + serviceName);

	}

}
