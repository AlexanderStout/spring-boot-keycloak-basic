## Description
This is a basic use of keycloak with Spring Security

## Requirements
### Keycloak
Download keycloak from the official [download](https://www.keycloak.org/downloads) page  
You can start it in dev mode using this command:  

Linux / Mac
```console
bin/kc.sh start-dev
```  

Windows
```console
bin/kc.bat start-dev
```

Keycloak must be reachable on _localhost:8080_ which is its default so no configuration should be needed
#### Realm
Create a realm called **SpringBootKeycloak**

#### Client
Create a client called **login-app** and add a valid redirect url **http://localhost:8081/***
