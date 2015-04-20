package eu.cloudopting.tosca.nodes;

import java.io.IOException;
import java.io.OutputStreamWriter;
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

public class ApacheModule extends CloudOptingNodeImpl implements CloudOptingNode{
	@Autowired
	ToscaFileManager tfm;
	@Override
	public String prepare(HashMap<String, String> data) {
		// TODO Auto-generated method stub
		String id = data.get("id");
		String toscaPath = data.get("toscaPath");
		System.out.println("I'm in the ApacheVirtualHost.prepare for :" + id);
		tfm = ToscaFileManager.getInstance();
		String myTemplate = tfm.getTemplateForNode(id,"PuppetTemplate");
		System.out.println("The template is :"+myTemplate);
		Map nodeData = tfm.getPropertiesForNode(id);
		return compilePuppetTemplate(null, null , toscaPath+myTemplate, nodeData);
	}

}
