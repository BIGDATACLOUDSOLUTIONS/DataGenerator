#!/bin/bash

DATA_CATEGORY=payments

java -cp "data-writer/target/package/applications/DataGenerator.jar" com.bdcs.data.generator.DataGenerator $DATA_CATEGORY

