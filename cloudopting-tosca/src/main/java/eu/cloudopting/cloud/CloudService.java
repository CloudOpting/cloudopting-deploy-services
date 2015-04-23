package eu.cloudopting.cloud;

import java.util.Set;

import org.apache.log4j.Logger;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;

public class CloudService {
	private ComputeService compute;
    private String location;
    public static final Logger logger = Logger.getLogger(CloudService.class);

    /**
     *  Constructor for the CloudService Class. Requires you to specify the cloud provider string, datacenter location,  the username and the API Key.
     * @param provider
     * @param location
     * @param username
     * @param apiKey
     */

     public CloudService(String provider, String location, String username, String apiKey) {
        this.location = location;  
         ComputeServiceContext context = ContextBuilder.newBuilder(provider).credentials(username, apiKey)
					.buildView(ComputeServiceContext.class);
         compute = context.getComputeService();
         logger.info("Cloud Compute Service Context Created");
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
