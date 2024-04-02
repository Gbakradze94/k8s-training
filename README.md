# k8s-training
## How to run this project

## Docker
Make sure you start docker on your machine.  If you are using
ubuntu on WSL, like me, then you need to make sure docker daemon
is up and running by typing the following command:
$ sudo service docker start

If you are not sure whether you have installed docker or not, you
can check this by running this command from your terminal:
$ docker --version

### to build and run docker image:
$ cd song-service <br/>
$ ./gradlew bootBuildImage

After running this command, if it goes all right you will
get a message 'Successfully built image 'docker.io/library/song-service:latest''.


### Create a docker network
Before running your docker image, it is a good idea to create 
docker network with this command:

$ docker network create song-network

$ docker network ls

### To run postgres image using docker: 
$ docker run -d \
--name song-postgres \
--net song-network \
-e POSTGRES_USER=user \
-e POSTGRES_PASSWORD=password \
-e POSTGRES_DB=songservicedb \
-p 5432:5432 \
postgres:14.4

### To remove the container when no longer needed:
$ docker rm -fv song-service

### To build docker image:
First run:

$ ./gradlew bootJar
Then build docker image: 
$ docker build -t song-service .

To run docker container for song-service:
$ docker run -d \
--name song-service \
--net song-network \
-p 8081:8081 \
-e SPRING_DATASOURCE_URL=
jdbc:postgresql://song-postgres:5432/songservicedb \
-e SPRING_PROFILES_ACTIVE=testdata \
song-service

### After you are done, you can delete both containers:
$ docker rm -f song-service song-postgres