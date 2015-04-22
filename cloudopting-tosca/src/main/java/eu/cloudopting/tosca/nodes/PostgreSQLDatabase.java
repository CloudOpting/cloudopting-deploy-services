/**
 * 
 */
package eu.cloudopting.tosca.nodes;

import java.io.IOException;
import java.io.StringWriter;
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
public class PostgreSQLDatabase extends CloudOptingNodeImpl implements CloudOptingNode {
	@Autowired
	ToscaFileManager tfm;
	/* (non-Javadoc)
	 * @see eu.cloudopting.tosca.nodes.CloudOptingNode#prepare(java.lang.String)
	 */
	@Override
	public String prepare(HashMap<String, String> data) {
		String id = data.get("id");
		String toscaPath = data.get("toscaPath");
		System.out.println("Working on :"+id);
		tfm = ToscaFileManager.getInstance();
		// TODO the ClearoPostgreSQLDB must be passes in the hash
		String myTemplate = tfm.getTemplateForNode("ClearoPostgreSQLDB","PuppetTemplate");
		System.out.println("The template is :"+myTemplate);
		Map nodeData = tfm.getPropertiesForNode(id);
		return compilePuppetTemplate(null, null , toscaPath+myTemplate, nodeData);
	}
	private String compilePuppetTemplate(Object object, Object object2,
			String string, Map nodeData) {
		// TODO Auto-generated method stub
		return null;
	}

}
