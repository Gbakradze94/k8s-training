# k8s-training
## How to run this project

## Docker
Make sure you start docker on your machine.  If you are using
ubuntu on WSL, like me, then you need to make sure docker daemon
is up and running by typing the following command: <br/>
``` $ sudo service docker start ```

If you are not sure whether you have installed docker or not, you
can check this by running this command from your terminal:
``` $ docker --version ```

### to build and run docker image: <br/>
``` $ cd song-service  ``` <br/>
``` $ ./gradlew bootBuildImage ```

After running this command, if it goes all right you will
get a message 'Successfully built image 'docker.io/library/song-service:latest''.


### Create a docker network
Before running your docker image, it is a good idea to create 
docker network with this command:

``` $ docker network create song-network ```

``` $ docker network ls ```

### To run postgres image using docker: <br/>
```
$ docker run -d \
--name song-postgres \
--net song-network \
-e POSTGRES_USER=user \
-e POSTGRES_PASSWORD=password \
-e POSTGRES_DB=song_service_db \
-p 5432:5431 \
postgres:14.4
```

### To remove the container when no longer needed: <br/>
``` $ docker rm -fv song-service ```

### To build docker image: 
First run: <br/>

``` $ ./gradlew bootJar ```
Then build docker image: 
``` $ docker build -t song-service . ```

To run docker container for song-service: <br/>
```
$ docker run -d \
--name song-service \
--net song-network \
-p 8081:8081 \
-e SPRING_DATASOURCE_URL=
jdbc:postgresql://song-postgres:5432/song_service_db \
-e SPRING_PROFILES_ACTIVE=testdata \
song-service
```

### After you are done, you can delete both containers: <br/>
``` $ docker rm -f song-service song-postgres ```

## Using Kubernetes
In order to use kubernetes, make sure you have installed it on your machine.
To create a new kubernetes cluster named <b>song</b> on top of Docker, and to
declare the resource limits for CPU and memory, you need to run: <br/>
``` minikube start --cpus 2 --memory 4g --driver docker --profile song ```

To get the list of all nodes in the cluster: <br/>
``` kubectl get nodes ```
The cluster which we made with the command above, is composed of a single node, which
hosts the Control Plane and acts as a worker node for deploying containerized workloads.

To get all available contexts with which you can interact: <br/>
``` kubectl config get-contexts ```
To get current cotext: <br/>
``` kubectl config current-context ```
To change context: <br/>
``` kubectl config use-context song ```

At any time, you can stop the cluster with ``` minikube stop --profile song  ``` and start it 
again with ``` minikube start --profile song ```. If you ever
want to delete it and start over, you can run the ``` minikube delete --profile song ``` command.
<br/>
To deploy PostgreSQL in your cluster, navigate to song-deployment/kubernetes/platform/development
folder and run:
``` kubectl apply -f services ```
The result will be a Pod running a PostgreSQL container in your local Kubernetes cluster.
You can check it with the following command: <br/>
``` kubectl get pod ``` <br/>

If at any point you need to undeploy the database, you can 
run the ``` kubectl delete -f services  ``` command from the same folder.

### Creating deployment for the Spring Boot Applications
For both of the microservices (song-service and resource-service), in order to create
a kubernetes deployment, we need to create k8s folder inside the projects' root directories,
and include two yml files, which will be deployment descriptors.
We need to add deployment.yml file. <br/>
The <b>spec</b> section of a Deployment manifest contains a selector part to define a strategy for
identifying which objects should be scaled by a ReplicaSet.

By default, minikube does not have access to your local container images, so it will
not find the images you have just built for song-service and resource-service.
You can manually import the image into your local cluster: <br/>
``` minikube image load song-service --profile song ``` <br/>
Same command goes for resource-service, only the name will be changed.
Now that you have a deployment manifest we need to apply it by running: <br/>
``` kubectl apply -f k8s/deployment.yml ``` <br/>
The command is processed by the Kubernetes Control Plane, which will create and maintain all the related objects
in the cluster. You can verify which objects have been created with the following command: <br/>
``` kubectl get all -l app=song-service ``` <br/>
To get logs: <br/>
``` kubectl logs deployment/catalog-service ```

To apply service manifest: <br/>
``` kubectl apply -f k8s/service.yml ```
``` kubectl get svc -l app=song-service ``` <br/>
Other applications within the cluster can reach this service either by
ClusterIP or its name, but what about us?  How can we reach it? One way is to use
port forwarding feature offered by kubernetes: <br/>
``` kubectl port-forward service/song-service 8081:81 ```
Forwarding from 127.0.0.1:8081 -> 8081
Forwarding from [::1]:8081 -> 8081 

To view pods for song-service: <br/>
``` kubectl get pods -l app=song-service ```
Or to view all pods:
``` kubectl get pods ```

If you delete one of the pods by running: <br/>
``` kubectl delete pod song-service-{pod-number-generated-by-k8s} ``` <br/>
you will notice that after running ``` kubectl get pods -l app=song-service ``` one
more time, you will get still two pods running, because <b>replica</b> is set to 2 in
deployment.yml file.  You will notice that one of the pods has the AGE of several seconds,
because it was just created by kubernetes.
<br/>
To delete all the kubernetes objects and manifests, open the project folder and run: <br/>
``` kubectl delete -f k8s ```  <br/>
To delete kubernetes services as well, navigate to song-deployment/kubernetes/platform/development folder
and run: <br/>
``` kubectl delete -f services ```

To create Helm chart named song: <br/>
``` helm create song-chart```