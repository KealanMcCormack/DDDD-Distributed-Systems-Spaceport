FROM openjdk:11-jre-slim

COPY target/Market-1.0.0-SNAPSHOT.jar /market.jar

CMD java -jar /market.jar --priceHost=${PRICE_HOST} --pricePort=${PRICE_PORT} --inventoryHost=${INVENTORY_HOST} --inventoryPort=${INVENTORY_PORT} --server.port=${MARKET_PORT}