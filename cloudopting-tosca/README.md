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

## Usage

The component will answer to a REST API that requires the following information passer as a POST call:

|call|parameter|type|meaning|
|---|---|---|---|
|/process|toscaId|String|The name of the toscafile to use (probably here we should use the ID in the DB so that we can ask the DB component to retrieve the correct TOSCA file|
||customerId|String|The name of the customer we are operating for. This is useful to create files that are related to it. Also in this case it could be wise to get the ID and ask for the name to the DB component|
||cloudId|String|The id of the cloud environment we are deploying on (in the case of CloudOpting project environment it will be either CSI's cloudstack or IMI's Azure)|

## Architecture
This component is part of the general architecture designed in the diagram below
![Diagram of the Orchestrator component](https://raw.githubusercontent.com/CloudOpting/cloudopting-deploy-services/master/cloudopting-tosca/orchestrator draft architecture.png)

This is only the set of components used in the orchestrator and component like the DB manager are likely to be used also by the Catalog component.
This component is a headless element and does not return any graphic information to the user.
At the moment the current implementation make use of an internal TOSCA file manager element that is not developed in the proper way: the component needs to have an array of TOSCA files caches and he has to return the cache ID to the process and all the calls to the manager need to add this ID so that each process will operate on his TOSCA file.
This change will be done in the next iteration of the component.

The TOSCA file manager also just make use of a graph manager, but does not leveradge on it for returning the correct ordered nodes, also this part will be developed in a next iteration; the graph manager is needed to allow the TOSCA manager to return the correctly dependent elements in the proper order.

Even if the TOSCA file manager could be implemented as an external REST component this could be an overkill in the simple actual architecture of the project; at the moment is used as a singleton object in the process. This generate a dependency on the elements that has to be validated, design team needs to vote on the better implementation.

The orchestrator also make use of the Cloud manager that has the knowledge to deploy the VM and will make use of the JClouds library.

At the moment the implementation for the Docker execution is done directly in the Task implementation as a shell command execution leveradging on the available task without any other complications.

## Settings
The orchestrator needs a folder in /cloudOptingData writable from the java process where all the file generation will be done (in this folder we will also have all the community puppet modules used in the build

To populate this folder we will use r10k tool during the CloudOpting master machine creation, this will set up the puppet environment with all the puppet modules we use in the project