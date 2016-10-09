# Trade Consumption
The trade consumption is the main component, which exposes a HTTP entry point for handling a large number of trade messages asynchronously.

This service is backed by a single producer/consumer queue in which is used for storing received trade messages in memory and then they're batch drained by the single thread consumer in order to persist them and send them to the trade processing service.

The comunication between trade conumption service and processing service is done via [Feign](http://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/1.2.0.RELEASE/#spring-cloud-feign). It's a declarative web service client, which integrates with [Ribbon](http://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/1.2.0.RELEASE/#spring-cloud-ribbon) and Eureka to provide a load balanced http client.

### Endpoint
> http://docker-host:8080/consumption/trade

> http://docker-host:8080/consumption/trades (batch consume)