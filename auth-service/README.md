# Auth Service
Here's the standalone Auth Server, which is used for user authorization and secure machine-to-machine communication. For this project it was built using Spring Cloud Security and a naive approach was taken with users in memory and without SSL. Ideally security might be implemented within Zuul.

It grants [OAuth2 tokens](https://tools.ietf.org/html/rfc6749) when requested on: `http://docker-host:8080/uaa/oauth/token`