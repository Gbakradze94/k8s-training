# How to run this project

## Docker
Make sure you start docker on your machine.  If you are using
ubuntu on WSL, like me, then you need to make sure docker daemon
is up and running by typing the following command:
$ sudo service docker start

If you are not sure whether you have installed docker or not, you
can check this by running this command from your terminal:
$ docker --version

### to build and run docker image:
``` $ cd resource-service <br/> ```
``` $ ./gradlew bootBuildImage ```

After running this command, if it goes all right you will
get a message 'Successfully built image 'docker.io/library/resource-service:latest''.


### Create a docker network
Before running your docker image, it is a good idea to create
docker network with this command:

``` $ docker network create song-network ```

``` $ docker network ls ```

### To run postgres image using docker:
```
$ docker run -d \
--name resource-postgres \
--net song-network \
-e POSTGRES_USER=user \
-e POSTGRES_PASSWORD=password \
-e POSTGRES_DB=resource_service_db \
-p 5432:5432 \
postgres:14.4
```

### To build docker image:
First run:

``` $ ./gradlew bootJar ```
Then build docker image:
``` $ docker build -t resource-service . ```

### To run docker container for resource-service:
```
$ docker run -d \
--name resource-service \
--net song-network \
-p 8082:8082 \
-e SPRING_DATASOURCE_URL=
jdbc:postgresql://song-postgres:5432/resuorce_service_db \
-e SPRING_PROFILES_ACTIVE=testdata \
resource-service
```

### To remove the container when no longer needed:
```$ docker rm -fv resource-service```


### After you are done, you can delete both containers:
```$ docker rm -f resource-service resource-postgres```

## Using Dockerfile
When creating Dockerfile, for the performance efficiency 
purposes, it is recommended to use multi-layered approach.
By default, Spring Boot applications are packaged as JAR artifacts made up of the following layers, 
starting from the lowest:
* dependencies
* spring-boot-loader
* snapshot-dependencies
* application

Comments about Dockerfile scripts are inside the Dockerfile.

## Docker Compose
Use can use docker compose to manage container lifecycle.
After docker-compose.yml is ready, you can start the containers
by running:
<br/>
`` docker-compose up -d ``
<br/>
To stop and remove the containers: <br/>
``` docker-compose down ```
