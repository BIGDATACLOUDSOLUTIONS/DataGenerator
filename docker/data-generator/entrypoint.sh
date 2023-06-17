#!/bin/bash

DATA_CATEGORY=$1

if [[ $DATA_CATEGORY == "default" ]]; then
  echo "Entering into Docker Container"
  /bin/bash
else
  echo DATA_CATEGORY=$DATA_CATEGORY
  unzip input_files.zip
  java -cp "DataGenerator.jar" com.bdcs.data.generator.DataGenerator $DATA_CATEGORY
fi
