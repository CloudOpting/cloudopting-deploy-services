package eu.cloudopting.tosca.nodes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import eu.cloudopting.tosca.operations.ToscaOperationImpl;
import eu.cloudopting.tosca.transformer.ToscaFileManager;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class CloudOptingNodeImpl implements CloudOptingNode {
	ToscaOperationImpl toi = new ToscaOperationImpl();
	ToscaFileManager tfm = ToscaFileManager.getInstance();

	public String execute(HashMap<String, String> data) {
		// TODO Auto-generated method stub
		String id = data.get("id");
		String operation = this.tfm.getOperationForNode(id, "Install");
		System.out.println("Invoking method :"+operation+" on node: "+id);
		Class partypes[] = new Class[1];
        partypes[0] = data.getClass();
        Method meth = null;
        try {
			meth = this.toi.getClass().getMethod(operation, partypes);
			System.out.println(meth.toString());
			System.out.println(meth.getParameterTypes().toString());
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return (String) meth.invoke(this.toi,data);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String prepare(HashMap<String, String> data) {
		// TODO Auto-generated method stub
		return null;
	}


}
