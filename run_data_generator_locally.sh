#!/bin/bash

export PRINT_DATA_ON_CONSOLE=true
export KAFKA_BROKERS=192.168.1.10:9092
export ZOOKEEPER=192.168.1.10:2181
export SCHEMA_REGISTRY_URL=http://192.168.1.10:18081

export NUMBER_OF_MESSAGES_TO_PUBLISH=100
export TARGET_TYPE=kafka

export OUTPUT_DATA_FORMAT=json
export KAFKA_TOPIC=invoices-json
DATA_CATEGORY=invoices

java -cp "data-writer/target/package/applications/DataGenerator.jar" com.bdcs.data.generator.DataGenerator $DATA_CATEGORY

