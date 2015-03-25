/**
 * 
 */
package eu.cloudopting.tosca.nodes;

import java.util.HashMap;

/**
 * @author gioppo
 *
 */
public class MySQL implements CloudOptingNode {

	/* (non-Javadoc)
	 * @see eu.cloudopting.tosca.nodes.CloudOptingNode#prepare(java.lang.String)
	 */
	@Override
	public String prepare(HashMap<String, String> data) {
		String id = data.get("id");
		return id;
		// TODO Auto-generated method stub

	}

}
