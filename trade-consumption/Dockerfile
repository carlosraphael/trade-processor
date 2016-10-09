FROM java:8-jre

ADD ./target/trade-consumption.jar /app/
CMD ["java", "-Xmx300m", "-jar", "/app/trade-consumption.jar"]

EXPOSE 7100