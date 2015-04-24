package eu.cloudopting.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.cloudopting.cloud.CloudService;

@Controller
public class CloudController {
    
	
	

	
    @RequestMapping("/cloud")
    public String createCompute() {
    	CloudService cloudService = new CloudService("cloudstack", "https://84.240.187.252:8443/client/", "vkWj7MV9z0PIV6FEQyoqzB46CJ4fOziqSaCTyT1gCSa-FXsygKuePsJ__e2gJjEsug0k9Pt7WVDPWWIcEZ0wjA", "OOmWvl5fK_-WA1efI9-xyk8_mD-ZruqPDcNCQl1MqC6D8s3enzRssRRXnLwMhko93BfSBcIC3M8ZuPOsMCeSRQ");
//    	cloudService.aquireServer(groupName, os, osVersion, ram, count);
    	try {
			String res = cloudService.runningVM();
			System.out.println(res);
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
}
