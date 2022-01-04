FROM openjdk:11-jre-slim

COPY target/PriceApi-0.0.1-SNAPSHOT.jar ./priceapi.jar

CMD java -jar /priceapi.jar --server.port=${PRICE_PORT} --spring.redis.host=${PRICE_REDIS_HOST}