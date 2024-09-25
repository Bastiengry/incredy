KEYCLOAK :
----------
- Launch keycloak with docker : "docker compose up" in directory called "docker-compose" in the workspace
                                keycloak accessible at URL "localhost:9999"    (use "localhost", not "127.0.0.1", because it will fail otherwise)
                                see file ".env" for login/password for the administration console of keycloak
- Configure keycloak: https://github.com/mateuszwlosek/oauth2-example/tree/master/oauth2-client
