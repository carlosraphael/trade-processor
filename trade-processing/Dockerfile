FROM java:8-jre

ADD ./target/trade-processing.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/trade-processing.jar"]

EXPOSE 7200