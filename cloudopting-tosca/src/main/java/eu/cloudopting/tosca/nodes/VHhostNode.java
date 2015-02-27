package eu.cloudopting.tosca.nodes;

/**
 * @author gioppo
 * This class is responsible to execute the deploy of the new virtual host interpreting the info of the TOSCA file
 * Data needed are the:
 * - cloud management class so that we can invoke the library that interact with the cloud
 * - the info on the sla that are required to create the correct parameters to the cloud lib
 * - the vmimage property
 * - the list of ports collected from the different nodes to be able to open the firewall and port forward correctly
 * - We also need to set the HOSTNAME of this VM and manage to associate it to the customer otherwise it will be a mess to manage in the future
 * 
 */
public class VHhostNode {
	
	
	/**
	 * This is the generic method we will have in all cloudopting nodes to do the deploy operations
	 * we need to understand if we can have aome asynch operation (like in this case we need to wait for the cloud execution
	 */
	public void deploy(){
		
	}

}
