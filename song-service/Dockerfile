FROM eclipse-temurin:17 AS builder
# OpenJDK base image for the first stage
WORKDIR workspace
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} song-service.jar
RUN java -Djarmode=layertools -jar song-service.jar extract

FROM eclipse-temurin:17

# Creates a "spring" user
# Configures "spring" as the current user. This is for security purposes,
# because containers run using root user by default, potentially letting them
# get root access to the Docker host. This is why we create a non-privileged user
# to mitigate this security risk. Following the principle of least privilege.
RUN useradd spring
USER spring

WORKDIR workspace

# Copies the application JAR file from the local machine
# into the image inside the “workspace” folder
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./


# Uses the Spring Boot Launcher to start the application from the layers rather than an uber-JAR
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]