#!/bin/bash

export PRINT_DATA_ON_CONSOLE=true
export KAFKA_BROKERS=127.0.0.1:9092
export ZOOKEEPER=127.0.0.1:2181
export SCHEMA_REGISTRY_URL=http://127.0.0.1:18081

export NUMBER_OF_MESSAGES_TO_PUBLISH=10
export TARGET_TYPE=kafka

export OUTPUT_DATA_FORMAT=avro
export KAFKA_TOPIC=payments-avro
DATA_CATEGORY=payments

java -cp "data-writer/target/package/applications/DataGenerator.jar" com.bdcs.data.generator.DataGenerator $DATA_CATEGORY

