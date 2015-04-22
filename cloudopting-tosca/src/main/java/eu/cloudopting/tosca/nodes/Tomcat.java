/**
 * 
 */
package eu.cloudopting.tosca.nodes;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import eu.cloudopting.tosca.transformer.ToscaFileManager;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * @author gioppo
 *
 */
public class Tomcat extends CloudOptingNodeImpl implements CloudOptingNode {
	@Autowired
	ToscaFileManager tfm;
	/* (non-Javadoc)
	 * @see eu.cloudopting.tosca.nodes.CloudOptingNode#prepare(java.lang.String)
	 */
	@Override
	public String prepare(HashMap<String, String> data) {
		String id = data.get("id");
		String toscaPath = data.get("toscaPath");
		System.out.println("I'm in the Tomcat.prepare for :" + id);

		// With my ID I ask to the TFM the array of my sons
		tfm = ToscaFileManager.getInstance();
		ArrayList<String> mychildren = tfm.getChildrenOfNode(id);

		int i;
		ArrayList<String> templateChunks = new ArrayList<String>();
		for (i = 0; i < mychildren.size(); i++) {
			String childType;
			childType = tfm.getNodeType(mychildren.get(i));
			Class childClass = null;
			try {
				childClass = Class.forName("eu.cloudopting.tosca.nodes."
						+ childType);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CloudOptingNode childInstance = null;
			try {
				childInstance = (CloudOptingNode) childClass.getConstructor(
						null).newInstance(null);
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
		String myTemplate = tfm.getTemplateForNode(id, "PuppetTemplate");
		System.out.println("The template for Tomcat is :" + myTemplate);
		// I merge all the template chunks from sons and all my own data and get
		// the final template and write it

//		Map nodeData = new HashMap();
		Map nodeData = tfm.getPropertiesForNode(id);
		// nodeData.put("hostname", id+"."+customer+".local");
		nodeData.put("childtemplates", templateChunks);
		return compilePuppetTemplate(null, null , toscaPath+myTemplate, nodeData);
	}
	private String compilePuppetTemplate(Object object, Object object2,
			String string, Map nodeData) {
		// TODO Auto-generated method stub
		return null;
	}

}
