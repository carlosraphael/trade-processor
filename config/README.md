# Config Service
This component plays an important role on microservice architecture once it easily provides a centralized configuration service for distributed systems. By using [Spring Cloud Config](http://cloud.spring.io/spring-cloud-config) we can get a very good embedded config server that runs as a standalone application.

For simplicity I'm just using classpath as [my repo](), but for the real world Git is better and simpler too. 

Also Spring Cloud Config allows us to change app configuration dynamically without having to rebuild or restart. By using `@RefreshScope` annotation you can easily accomplish that. Note that there are some limitations for dynamic refresh though. `@RefreshScope` doesn't work with `@Configuration` classes and doesn't affect `@Scheduled` methods.