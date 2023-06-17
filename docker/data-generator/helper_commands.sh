# To start container
docker run -it generator:latest


# Run a specific service of docker compose
docker-compose -f docker-compose.yml up SERVICE_NAME
eg: docker-compose -f docker-compose.yml up customers-kafka



docker run --rm -it --network=host generator:latest
docker run --rm -it --add-host host.docker.internal:host-gateway generator:latest
curl -X GET 127.0.0.1:9092



docker-compose -f docker-compose.yml --profile orders-kafka up





docker run --rm -it --network=host \
-e NUMBER_OF_MESSAGES_TO_PUBLISH=10 \
-e TARGET_TYPE=kafka \
-e OUTPUT_DATA_FORMAT=avro \
-e PRINT_DATA_ON_CONSOLE=true \
-e KAFKA_BROKERS=127.0.0.1:9092 \
-e KAFKA_TOPIC=payments-avro \
-e ZOOKEEPER=127.0.0.1:2181 \
-e SCHEMA_REGISTRY_URL=http://127.0.0.1:118081 \
generator:latest payments






















