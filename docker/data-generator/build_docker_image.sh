#!/bin/bash

set -ex

# Get the script's directory path
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd ${SCRIPT_DIR}

function parse_args(){
  while [[ $1 = -* ]]; do
    case $1 in
    --build)
      build_image=yes
      ;;
    --start-container)
      start_container=yes
      ;;
      *)
      exit 1
      ;;
    esac
    shift
  done

cat <<EOF
Arguments:

  build_image             = $build_image
  start-container         = $start_container
EOF
}

parse_args "$@"

DOCKER_TAG=generator
VERSION=latest
export DATA_GENERATOR_IMAGE_NAME=${DOCKER_TAG}:${VERSION}


if [[ ${build_image} == 'yes' ]]; then
  # Stop and Delete all the container attached to AIRFLOW_IMAGE
  ACTIVE_CONTAINER_ID=$(docker ps -a --filter ancestor=${DATA_GENERATOR_IMAGE_NAME} --format "{{.ID}}")
  for CONTAINER_ID in $ACTIVE_CONTAINER_ID;
  do
    docker stop ${CONTAINER_ID}
    docker rm ${CONTAINER_ID}
  done
  # Build the Airflow Image
  docker build -t ${DOCKER_TAG}:${VERSION} .
fi


if [[ ${start_container} == 'yes' ]]; then
  docker run -it --rm ${DATA_GENERATOR_IMAGE_NAME} default
fi

