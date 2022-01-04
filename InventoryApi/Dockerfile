FROM openjdk:11-jre-slim

COPY target/InventoryApi-0.0.1-SNAPSHOT.jar ./inventoryapi.jar

CMD java -jar /inventoryapi.jar --server.port=${INVENTORY_PORT} --spring.redis.host=${INVENTORY_REDIS_HOST}