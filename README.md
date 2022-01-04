Webservice in a Docker Container
A Springboot Rest Webservice Project that can be deployed to a Docker container.

This is a simple Springboot application created at https://start.spring.io/ and then augmented with a few features:

A single greeting REST json service accessible by GET request with optional name parameter.
Available Open API docs for the service(s) automatically created and available as json or yaml
Swagger UI page showing the available annotation driven docs and allowing test calls
Build Locally
Maven
Use Maven Wrapper
Build ./mvnw packaage will create the jar in the target folder.

Run ./mvnw spring-boot:run or java -jar target/<jar file name>

Use Maven Locally
Build mvn clean package will create the jar in the targer folder

Running Locally
Set up environment variables
Refer to Azure KeyVault (ct-s-zeaus-aagent-kv) for the following variables.

AZURE_KEYVAULT_URL
AZURE_KEYVAULT_CLIENT_ID
AZURE_TENANT_ID
Refer to CYBERARK for following environment variable.

AZURE_KEYVAULT_SECRET
Setting up environment variables locally in IntelliJ IDE:
Run > Edit Configurations... > Kotlin > ApplicationKt > Configuration > Environment Variables

Docker
Refer here for how to run the docker image locally.

CI / CD
There are 3 GitHub Actions workflows for CI / CD pipelines:

CI per PR Build (ci-per-pr.yml)
This workflow will run whenever a pull request is opened, synchronized, or closed. It builds the source code with maven and publish the java artifact (jar) to Artifactory. It also builds the docker image and publishes the docker image to Artifactory. It will then deploy the docker image to sandbox in Kubernetes. Once the PR is closed, it will delete the deployment in sandbox.
CI Build (ci.yml)
This workflow will run whenever code is being merged to the main/master branch. It will build the source code with maven and publish the java artifact (jar) to Artifactory. It also builds the docker image and publishes the docker image to artifactory. It will then create a deployment for the docker image in the aptagnext-nonprod namespace in Kubernetes. This is done by triggering a workflow to run in AAInternal/aagent-deploy-kubernetes that updates the corresponding webapp.yaml, which ArgoCD watches.
Promote Dev -> Prod (cd.yml)
This workflow will run whenever a release tag is created on the app repo. It will promote the jar and docker image from dev to prod in Artifactory (i.e. dev-releases to prod-releases, docker-dev to docker-prod). It will then create a deployment for the docker image in the aptagnext-prod namespace in Kubernetes. This is done by triggering a workflow to run in AAInternal/aagent-deploy-kubernetes that updates the corresponding webapp.yaml, which ArgoCD watches.
The workflows in this repository reference reusable workflows in AAInternal/aagent-workflow-actions. Refer to this wiki, for more information on these workflows.

Rest Service
HTML test endpoint
    localhost:8080/
Exposes links to the various "information" pages under the API

Hello World
Greeting: Hello World
Greeting: Hello Friend
Swagger UI Docs with testing
Actuator Health
Actuator Info
Open API JSON
Open API download YAML
Endpoint:
    localhost:8080/greeting
Response:
    {
      "id": 2,
      "content": "Hello, World!"
    }
Health Check / Application Info
    localhost:8080/actuator/health
    localhost:8080/actuator/info
Open API Docs
    localhost:8080/docs
You can see the full Open API at:

    localhost:8080/openapi.json - json format
    localhost:8080/openapi.json.yaml - yaml format (download)
API Spec
Currently, the api definition found in catalog-info-api.yaml refers to the api.json file found in the repo. In order to have the api definition use the one found in the kubernetes instance change the last line in catalog-info-api.yaml from:

$text: ./api.json

to

$text: https://aagent-new-service-parent.drke.ok8s.aa.com/openapi.json

For more information about how the catalog yaml file is structured, please refer to backstage's documentation: https://backstage.io/docs/features/software-catalog/descriptor-format#substitutions-in-the-descriptor-format
