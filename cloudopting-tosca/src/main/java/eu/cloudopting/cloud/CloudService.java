package eu.cloudopting.cloud;

import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.cloudstack.CloudStackApi;
import org.jclouds.cloudstack.CloudStackContext;
import org.jclouds.cloudstack.domain.VirtualMachine;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.cloudstack.domain.VirtualMachine.State;

public class CloudService {
	private ComputeService compute;
    private String location;
    public static final Logger logger = Logger.getLogger(CloudService.class);
    private CloudStackApi client;
    /**
     *  Constructor for the CloudService Class. Requires you to specify the cloud provider string, datacenter location,  the username and the API Key.
     * @param provider
     * @param location
     * @param username
     * @param apiKey
     */

     public CloudService(String provider, String location, String secretKey, String apiKey) {
        this.location = location;  
 //        ComputeServiceContext context = ContextBuilder.newBuilder(provider).credentials(username, apiKey)
//					.buildView(ComputeServiceContext.class);
 //        compute = context.getComputeService();
   //      logger.info("Cloud Compute Service Context Created");
         
         
         Properties prop = new Properties();
         prop.put(Constants.PROPERTY_ENDPOINT, location);
         ContextBuilder builder = ContextBuilder.newBuilder(provider)
         .credentials(apiKey, secretKey)
         // .modules(modules)
         .overrides(prop);
         CloudStackContext context = builder.buildView(CloudStackContext.class);
//         Object providerContext = context.getProviderSpecificContext();
         client = context.getApi();
         
     }
    
     public String runningVM() throws Exception {
    	 Set<VirtualMachine> listVirtualMachines = client.getVirtualMachineApi().listVirtualMachines();
    	 for ( VirtualMachine vm : listVirtualMachines ) {
    	 if ( vm.getState() == State.RUNNING ) {
    	 return vm.getId();
    	 }
    	 }
    	 throw new Exception("Not found the running vm");
    	 }
     
     
/**
* Acquire Servers by specifying specs
* @param groupName
* @param os
* @param osVersion
* @param ram
* @param count
* @throws Exception
*/
public void aquireServer(String groupName, String os, String osVersion, Integer ram, Integer count) throws Exception {

     TemplateBuilder templateBuilder = compute.templateBuilder();
     Template template = templateBuilder
                         .locationId(this.location)
                         .os64Bit(true)
                         .osDescriptionMatches(os)
                         .osVersionMatches(osVersion)
                         .minRam(ram)
                         .build();

      logger.info("Acquiring "+ count+ " server(s).");
      Set<? extends NodeMetadata> nodes = compute.createNodesInGroup(groupName,count, template);
      logger.info(nodes.size() + " server(s) acquired!");
}

}
