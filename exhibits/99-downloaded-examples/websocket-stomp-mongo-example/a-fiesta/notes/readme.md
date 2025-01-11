# Example-1. Spring boot app that uses mongo and both a REST and messaging based API.

## Introduction
A very clean and informative example with a lot of clues on how to connect up the mongodb-changestream to the STOMP based messaging interface. However it is missing all the mongodb connectivity configs and setup which would need to be added to make this example workl

## Setup
The [downloaded file spring-boot-ws-mongo-master.zip](../downloaded/spring-boot-ws-mongo-master.zip) in the [downloaded](../downloaded) folder is unzipped, cleaned up and setup in the [example folder](../example)  
The project coordinates in the pom are -:  
```
	<groupId>com.example</groupId>
	<artifactId>spring-boot-stomp-websocket</artifactId>
```
However as mentioned earlier, it is not a full working example, specifically it is missing the spring boot connectivity to MongoDb and which I plan to add and see if I can the example up and running.


## Resources
This code was downloaded from [this github repo](https://github.com/codefiesta/spring-boot-ws-mongo)