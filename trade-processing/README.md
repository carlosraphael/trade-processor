# Trade Processing
Naive service that generates a very basic "real-time" metric from the received trade messages. It' been taken periodic snapshots from the metrics and sent them through a websocket endpoint. 

PS: Ideally I would have used Elasticsearch to make aggregation on the trade JSON and delivery a better output for the client. Next version certainly I'm going to improve this part once my initial focus was to get the whole microservice architecture in place without many fancy things.

### Endpoint
> http://docker-host:8080/processing/trade (simple http endpoint that exposes stored trade messages)

> ws://docker-host:8080/processing/real-time (websocket endpoint that exposes simple metrics)

> ws://docker-host:8080/processing/real-time/history (websocket endpoint that exposes metric history)