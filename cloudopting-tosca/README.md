This component represent the orchestration core of the CloudOpting platform.
It uses Activiti BPMN engine to execute a process that orchestrate the deployment of the service.
It reads the TOSCA instance file (an example is found [here](https://raw.githubusercontent.com/CloudOpting/cloudopting-deploy-services/master/cloudopting-tosca/ClearoExampleInstance.xml)) and with that content it:

1. command the cloud to generate a proper new VM
2. open the needed ports on the new VM
3. generates the puppet files 
4. than generates the Dockerfiles that use the puppet files to build the image of the container
5. than push the image to the repo
6. generate a compose file that use the images
7. command Docker remotely using swarm to deploy the containers on the newly generate VM

The process is summarized in the following image and is described [here](https://raw.githubusercontent.com/CloudOpting/cloudopting-deploy-services/master/cloudopting-tosca/src/main/resources/processes/orchestration-process.bpmn20.xml)
![Image of the Orchestration progess](https://raw.githubusercontent.com/CloudOpting/cloudopting-deploy-services/master/cloudopting-tosca/orchestration-process.bpmn20.xml.png)
