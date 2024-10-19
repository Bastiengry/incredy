### Keycloak + Prometheus + Graphana

- Launch keycloak with docker : "docker compose up" in directory called "docker-compose" in the workspace
                                keycloak accessible at URL "localhost:9999"    (use "localhost", not "127.0.0.1", because it will fail otherwise)
                                see file ".env" for login/password for the administration console of keycloak
- Configure keycloak: https://github.com/mateuszwlosek/oauth2-example/tree/master/oauth2-client


### VM argument for Spring Boot Profile ("dev" profile in that case)
- -Dspring.profiles.active=dev

### Install JDK21 on WSL (Debian)
- wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb
- sudo dpkg -i jdk-21_linux-x64_bin.deb
- java -version

### Spring boot in Docker
- Go in root folder of incredy project
- Build the Docker image: docker build --tag=docker-with-spring-profile:latest .
- Launch the Docker image in a container: docker run --name incredy --net host -d docker-with-spring-profile:latest -p 8081:8081 -p 9081:9081
- IN THE COMMAND ABOVE, "--net host" CONNECT DOCKER CONTAINER TO HOST NETWORK, AUTHORIZING THE COMMAND "curl localhost:8081"
- Logs of the container: docker logs incredy
- Connect to Docker image: docker exec -it incredy /bin/sh
- Stop container: docker stop incredy
- Delete container: docker rm incredy
- Delete image: docker rmi docker-with-spring-profile:latest