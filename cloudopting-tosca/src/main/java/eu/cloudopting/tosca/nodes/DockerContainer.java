package eu.cloudopting.tosca.nodes;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.fabric.xmlrpc.base.Array;

import eu.cloudopting.tosca.transformer.ToscaFileManager;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class DockerContainer implements CloudOptingNode {

	@Autowired
	ToscaFileManager tfm;
	
	/* (non-Javadoc)
	 * @see eu.cloudopting.tosca.nodes.CloudOptingNode#prepare(java.lang.String)
	 */
	@Override
	public String prepare(String id){
		// here I'm a generic container ... I need to have my ID
		System.out.println("I'm in the DockerContainer.prepare for :"+id);
		
		//String customer = (String) execution.getVariable("customer");
		String customer = new String("csi");
		System.out.println("result from shell execution:"+customer);
		
		// With my ID I ask to the TFM the array of my sons
		tfm = ToscaFileManager.getInstance();
		ArrayList<String> mychildren = tfm.getChildrenOfNode(id);

		// I cycle on my sons and instantiate dynamically a class of type son to manage this part
		// that method will return a string that represent the chunk of template I need to put in the puppet file

		int i;
		ArrayList<String> templateChunks = new ArrayList<String>(); 
		for(i=0;i<mychildren.size();i++){
			String childType;
			childType = tfm.getNodeType(mychildren.get(i));
			Class childClass = null;
			try {
				childClass =  Class.forName("eu.cloudopting.tosca.nodes."+childType);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CloudOptingNode childInstance = null;
			try {
				childInstance = (CloudOptingNode) childClass.getConstructor(null).newInstance(null);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			templateChunks.add(childInstance.prepare(mychildren.get(i)));
		}
		
		
		
		// I get the puppetFile template name
		String myTemplate = tfm.getTemplateForNode(id,"PuppetTemplate");
		System.out.println("The template for DockerContainer is :"+myTemplate);
		// I merge all the template chunks from sons and all my own data and get the final template and write it
		
		Map nodeData = new HashMap();
		nodeData.put("hostname", id+"."+customer+".local");
		nodeData.put("childtemplates",templateChunks);
		Configuration cfg = new Configuration();
		Template tpl = null;
		try {
			tpl = cfg.getTemplate(myTemplate+".ftl");
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
		String puppetFile = new String(id+".pp");
		try {
			outFile = new PrintWriter(puppetFile, "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//tpl.process(nodeData, outputTempl);
			tpl.process(nodeData, outFile);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// I get the Dockerfile template name
		Map nodeDataDC = tfm.getPropertiesForNode(id);
		nodeDataDC.put("puppetFile",puppetFile);
		String myDCTemplate = tfm.getTemplateForNode(id,"DockerfileTemplate");
		System.out.println("The template for DockerContainer is :"+myDCTemplate);
		// I add the data and get the final docker template and write it
		Template tplDC = null;
		try {
			tplDC = cfg.getTemplate(myDCTemplate+".ftl");
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

//		OutputStreamWriter outputTempl = new OutputStreamWriter(System.out);
//		FileOutputStream outFile = new FileOutputStream("the-file-name");
		PrintWriter outFileDC = null;
		String dockerFile = new String(id+".dockerfile");
		try {
			outFileDC = new PrintWriter(dockerFile, "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//tpl.process(nodeData, outputTempl);
			tplDC.process(nodeDataDC, outFileDC);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
}
