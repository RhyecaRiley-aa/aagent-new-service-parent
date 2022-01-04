# From custom aagent base image. See Dockerfile in AAInternal/aagent-common-logging
FROM packages.aa.com/docker-prod-ae/aagent-base-image:0.0.4

ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-javaagent:/dd_agent.jar","-jar","/app.jar","--add-opens","java.base/java.util.concurrent=ALL-UNNAMED"]

# Datadog apm_non_local_traffic
EXPOSE 8126

# Port for DogStatsD unless dogstatsd_non_local_traffic is set to true
EXPOSE 8125
