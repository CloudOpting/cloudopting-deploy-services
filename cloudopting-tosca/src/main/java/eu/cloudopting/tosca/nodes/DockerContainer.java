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

public class DockerContainer extends CloudOptingNodeImpl implements CloudOptingNode {

	@Autowired
	ToscaFileManager tfm;
	
	/* (non-Javadoc)
	 * @see eu.cloudopting.tosca.nodes.CloudOptingNode#prepare(java.lang.String)
	 */
	@Override
	public String prepare(HashMap<String, String> data){
		// here I'm a generic container ... I need to have my ID
		String id = data.get("id");
		String toscaPath = data.get("toscaPath");
		String creationPath = data.get("creationPath");
		String servicePath = data.get("servicePath");
//		System.out.println("I'm in the DockerContainer.prepare for :"+id);
		
		//String customer = (String) execution.getVariable("customer");
		String customer = new String("csi");
//		System.out.println("result from shell execution:"+customer);
		
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
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("id", mychildren.get(i));
			hm.put("toscaPath", toscaPath);
			templateChunks.add(childInstance.prepare(hm));
		}
		
		
		
		// I get the puppetFile template name
		String myTemplate = tfm.getTemplateForNode(id,"PuppetTemplate");
		System.out.println("The Puppet template for this DockerContainer is :"+myTemplate);
		// I merge all the template chunks from sons and all my own data and get the final template and write it
		
		Map nodeData = new HashMap();
		nodeData.put("hostname", id+"."+customer+".local");
		nodeData.put("childtemplates",templateChunks);
		
		String puppetFile = new String(id+".pp");
		compilePuppetTemplate(puppetFile, creationPath , toscaPath+myTemplate, nodeData);
		System.out.println("Puppet file created");
		/// DOCKERFILE PART
		
		// get the exposed ports
		ArrayList<String> exPorts = tfm.getExposedPortsOfChildren(id);
		System.out.println("The EXPOSED PORTS DockerContainer are :"+exPorts.toString());
		// I get the Dockerfile template name
		Map nodeDataDC = tfm.getPropertiesForNode(id);
		nodeDataDC.put("puppetFile",puppetFile);
		nodeDataDC.put("servicePath",servicePath);
		nodeDataDC.put("exposedPorts",exPorts);
		String myDCTemplate = tfm.getTemplateForNode(id,"DockerfileTemplate");
		System.out.println("The Dockerfile template for this DockerContainer is :"+myDCTemplate);
		// I add the data and get the final docker template and write it

		String dockerFile = new String(id+".dockerfile");
		compilePuppetTemplate(dockerFile, creationPath , toscaPath+myDCTemplate, nodeDataDC);
		System.out.println("Dockerfile created");
		return id;
	}

	private void compilePuppetTemplate(String dockerFile, String creationPath,
			String string, Map nodeDataDC) {
		// TODO Auto-generated method stub
		
	}
}
