# Workout Tracker
A Spring-Boot-JPA based REST API utilizing Spring Security with JWT for authentication and authorization.
You will require a mysql database for data storage. You can provide a local installation or docker container. Docker setup for mysql has been mentioned in instructions below.

## Build from code
`mvn clean package`

## Create Docker Image
`docker build --file Dockerfile --tag workout-tracker-service .`

## Tag and Push to Docker Hub
docker tag workout-tracker-service:latest agrahul89/workout-tracker-service:<tag>
docker push agrahul89/workout-tracker-service

## Create a MySQL container
`docker run -d -p 3306:3306 --name mysql-container -e MYSQL_ROOT_PASSWORD=p@ssw0rd -e MYSQL_DATABASE=iiht -e MYSQL_USER=iiht -e MYSQL_PASSWORD=tracker mysql:latest`

## Link with MySQL Container
`docker run -t --name workout-tracker-service-container --link mysql-container:mysql -p 8080:8080 workout-tracker-service`

## Verify containers are linked
* `docker exec -it workout-tracker-service-container bash`
* `cat /etc/hosts`
