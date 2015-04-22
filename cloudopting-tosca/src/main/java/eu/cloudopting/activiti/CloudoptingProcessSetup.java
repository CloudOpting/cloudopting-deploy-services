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
import org.apache.xml.dtm.ref.DTMNodeList;
import org.springframework.beans.factory.annotation.Autowired;

import eu.cloudopting.file.FileSystemManager;
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
		System.out.println("Setting up the process information and preparing the environment");
		String serviceName = (String) execution.getVariable("toscaFile");
//		System.out.println("toscaFile :" + toscaFile);
		String customer = (String) execution.getVariable("customer");
		System.out.println("customer :" + customer);
		String cloud = (String) execution.getVariable("cloud");
		System.out.println("cloud :" + cloud);
		
		FileSystemManager dm = new FileSystemManager();

		String dockerContextPath = new String("/cloudOptingData");
		String dir = new String(dockerContextPath + "/" + customer + "-"
				+ serviceName);
		System.out.println("Preparing the folders for the service and customer");
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

		dm.unzip(serviceName+".czar", dir+"/tosca");
		
		toscaFileManager = ToscaFileManager.getInstance();
		
		toscaFileManager.getDefinitionFile(dir+"/tosca");
/*
		String xml = null;
		try {
			xml = new String(Files.readAllBytes(Paths.get(toscaFile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		// With the retrieved XML we instantiate the ToscaFileManager that is
		// the only one that know how to read it
		
	//	toscaFileManager.setToscaFile(xml);
		// preparing the Puppet env

		// Creating new directory in Java, if it doesn't exists

		// getting the list of puppet modules that this service needs
		System.out.printf("Preparing the Puppetfile");
		ArrayList<String> modules = toscaFileManager.getPuppetModules();
		ArrayList<HashMap<String, String>> modData = new ArrayList<HashMap<String, String>>();
		for (String mod : modules) {
			modData.add(toscaFileManager.getPuppetModulesProperties(mod));
//			System.out.println(mod);
		}
//		System.out.println(modData.toString());
		
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

		DTMNodeList nodes = toscaFileManager.getNodeByType("DockerContainer");
		ArrayList<String> dockerNodesList = new ArrayList<String>();
		System.out.println("before cycle");
		for (int i = 0; i < nodes.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());
//			System.out.println(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
			dockerNodesList.add(nodes.item(i).getAttributes().getNamedItem("id")
					.getNodeValue());
		}
		
		ArrayList<String> dockerPortsList = toscaFileManager.getHostPorts();
		dockerPortsList.add("Port1");
		execution.setVariable("dockerNodesList", dockerNodesList);
		execution.setVariable("vmPortsList", dockerPortsList);
		
		
		// setting the variables for the rest of the tasks
		execution.setVariable("creationPath", dir);
		execution.setVariable("dockerContextPath", dockerContextPath);
		execution.setVariable("service", serviceName);
		execution.setVariable("servicePath", customer + "-" + serviceName);

	}

}
