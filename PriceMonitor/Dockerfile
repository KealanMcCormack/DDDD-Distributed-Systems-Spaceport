FROM openjdk:11-jre-slim

COPY target/PriceMonitor-0.0.1-SNAPSHOT.jar ./PriceMonitor.jar

CMD java -jar /PriceMonitor.jar --server.port=${PRICE_MONITOR_PORT} --pricemonitor=${PRICE_MONITOR_PORT}