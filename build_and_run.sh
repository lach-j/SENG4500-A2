#!/bin/bash

# Build app
sudo -u $SUDO_USER mvn clean install

# Push image
docker build . -t seng4500:latest

docker stop SENG4500-P1 SENG4500-P2
docker rm SENG4500-P1 SENG4500-P2

# Launch a terminator instance using the new layout
terminator -l foo -p hold