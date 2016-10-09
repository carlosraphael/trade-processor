# Registry
Another important component is the Service registration and discovery. It allows automatic detection of network locations for service instances, which could have dynamically assigned addresses because of auto-scaling, failures or upgrades.

In this project I'm using the [Netflix Eureka](http://cloud.spring.io/spring-cloud-netflix/). Eureka is a really good example of the [client-side discovery pattern](http://microservices.io/patterns/client-side-discovery.html), when client is responsible for determining locations of available service instances.