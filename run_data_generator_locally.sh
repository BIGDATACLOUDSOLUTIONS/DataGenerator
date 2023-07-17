#!/bin/bash

DATA_CATEGORY=$1

java -cp "data-writer/target/package/applications/DataGenerator.jar" com.bdcs.data.generator.DataGenerator $DATA_CATEGORY

