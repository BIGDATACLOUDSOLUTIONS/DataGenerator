#Check kafka is running
curl -X GET 127.0.0.1:9092

# To start container
docker run -it --rm generator:latest default


# Run a specific service of docker compose
docker-compose -f docker-compose.yml up SERVICE_NAME
eg: docker-compose -f docker-compose.yml up customers-kafka
eg:docker-compose -f docker-compose.yml --profile orders-kafka up
eg:docker-compose -f docker-compose.yml --profile orders-file up


# Attach Host Network
docker run --rm -it --network=host generator:latest


#Run generator using docker command
docker run --rm -it --network=host \
-e NUMBER_OF_MESSAGES_TO_PUBLISH=10 \
-e TARGET_TYPE=kafka \
-e OUTPUT_DATA_FORMAT=avro \
-e PRINT_DATA_ON_CONSOLE=true \
-e KAFKA_BROKERS=192.168.1.6:9092 \
-e KAFKA_TOPIC=payments-avro \
-e ZOOKEEPER=192.168.1.6:2181 \
-e SCHEMA_REGISTRY_URL=http://192.168.1.6:18081 \
generator:latest payments






















