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
public class War implements CloudOptingNode {
	@Autowired
	ToscaFileManager tfm;
	@Override
	public String prepare(HashMap<String, String> data) {
		// TODO Auto-generated method stub
		String id = data.get("id");
		System.out.println("I'm in the War.prepare for :" + id);
		tfm = ToscaFileManager.getInstance();
		String myTemplate = tfm.getTemplateForNode(id,"PuppetTemplate");
		System.out.println("The template is :"+myTemplate);
		Map nodeData = tfm.getPropertiesForNode(id);
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
StringWriter writer = new StringWriter();
//		OutputStreamWriter outputTempl = new OutputStreamWriter(System.out);
		try {
//			tpl.process(nodeData, outputTempl);
			tpl.process(nodeData, writer);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return writer.getBuffer().toString();
	}

}
