#!/bin/bash

# Build app
mvn clean install

# Push image
docker build . -t seng4500:latest

docker stop SENG4500-P1 SENG4500-P2
docker rm SENG4500-P1 SENG4500-P2

DOCKER_ARGS="--net seng4500net --entrypoint java -itd seng4500:latest -jar /usr/local/lib/A1.jar 172.18.255.255 5000"

docker run --ip 172.18.0.21 --name SENG4500-P1 ${DOCKER_ARGS} &
docker run --ip 172.18.0.22 --name SENG4500-P2 ${DOCKER_ARGS} & wait

# Launch a terminator instance using the new layout
terminator -l foo -p hold