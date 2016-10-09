# Market Trade Processor
This project is a prototype, coded with no much time available, capable of handling a large number of trade messages per second by receiving them through a HTTP endpoint, which takes JSON as payload. By prototype I mean something functional in processing lots of trade, but generally some things act naively.

## Overview
This project was built using [*microservices*](http://microservices.io/patterns/microservices.html) approach on top of *Java*, which leverages some well known open-source technologies like *Spring, Hibernate, Eureka, Zuul, Feign, OAuth2, Maven, JUnit, Mockito, Gatling, Lombok, Mysql, Docker compose*.

Some of the main characteristics of a microservices-based application are functional decomposition, well-defined interfaces and single resposibility. In other words, microservice allows us to get loose coupling, high cohesion, agility, flexibility and scalability.

Here're some patterns related to microservice:

![Microservice Patterns](http://microservices.io/i/PatternsRelatedToMicroservices.jpg)

With this in mind the trade processor was split into the following Maven modules:

* [config](https://github.com/carlosraphael/trade-processor/tree/master/config)
* [registry](https://github.com/carlosraphael/trade-processor/tree/master/registry)
* [gateway](https://github.com/carlosraphael/trade-processor/tree/master/gateway)
* [auth-service](https://github.com/carlosraphael/trade-processor/tree/master/auth-service)
* [trade-consumption](https://github.com/carlosraphael/trade-processor/tree/master/trade-consumption)
* [trade-processing](https://github.com/carlosraphael/trade-processor/tree/master/trade-processing)
* [monitoring](https://github.com/carlosraphael/trade-processor/tree/master/monitoring)

Inside each module you can find a brief doc around them.

## Requirements
* Java 8
* Maven 3
* Docker compose 1.8

## How to run
1. Fork the project
2. Build: `mvn install`
3. Before running docker make sure all exposed ports in the [docker compose file](https://github.com/carlosraphael/trade-processor/blob/master/docker-compose.yml) are really available
4. Run *docker-compose* to start up all services together: `docker-compose up`
5. Allow some seconds until all services are running and properly registered within Service Discovery

### Security
Now you've got everything up and running you **must authenticate** in order to be able to call the main entrypoint. With the following info you must be able to get the OAuth2 token:

* Username: **demo**
* Password: **demo**
* Endpoint: **http://docker-host:8080/uaa/oauth/token**

Using Postman to get oauth token:

![Postman token](https://github.com/carlosraphael/trade-processor/blob/master/postman_oauth2_token.PNG)

### Load testing
Optionally you can get the load-testing Maven project to inject random trade data using the following command: 

`mvn gatling:test -Doauth2.token=YOUR-GENERATED-OAUTH-TOKEN`

**Remember to do the security step to get a valid oauth token.**

### Useful endpoints
> http://docker-host:8080/consumption/trade (**main entry point**)

```json
{
  "userId" : 134256,
  "currencyFrom" : "EUR",
  "currencyTo" : "GBP",
  "amountSell" : 1000.0,
  "amountBuy" : 747.1,
  "rate" : 0.7471,
  "timePlaced" : "24-JAN-15 10:27:44",
  "originatingCountry" : "FR"
}
```

> http://docker-host:8080/consumption/trades (batch consume)

> http://docker-host:8080/processing/trade (simple http entry point that exposes stored trade messages)

> ws://docker-host:8080/processing/real-time (websocket entry point that exposes the latest metrics)

> ws://docker-host:8080/processing/real-time/history (websocket entry point that exposes metric history)

Note that all entry points are taking into account the API Gateway port.

# References
1. http://microservices.io/patterns/microservices.html
2. https://www.infoq.com/articles/microservices-intro
3. http://martinfowler.com/articles/microservices.html
4. https://spring.io/blog/2015/07/14/microservices-with-spring
