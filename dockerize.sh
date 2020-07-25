#!/bin/bash
# Add permissions with:  chmod +x dockerize.sh
# Then run it with: ./dockerize.sh

# Compiling Java Project
mvn clean package

# Cleaning
docker stop backendnimbolu
docker stop postgresnimbolu
docker stop mongonimbolu
docker rm backendnimbolu
docker rm postgresnimbolu
docker rm mongonimbolu
docker image rm backendnimbolu

# Building
docker build -t backendnimbolu .
docker-compose up -d