This component represent the orchestration core of the CloudOpting platform.
It uses Activiti BPMN engine to execute a process that orchestrate the deployment of the service.
It reads the TOSCA instance file (an example is found here) and with that content it
command the cloud to generate a proper new VM
open the needed ports on the new VM
generates the puppet files 
than generates the Dockerfiles that use the puppet files to build the image of the container
than push the image to the repo
generate a compose file that use the images
command Docker remotely using swarm to deploy the containers on the newly generate VM

The process is summarized in the following image and is described here