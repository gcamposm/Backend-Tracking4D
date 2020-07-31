#!/bin/bash
# Add permissions with:  chmod +x dockerize.sh
# Then run it with: ./dockerize.sh

# Compiling Java Project
mvn clean package

# Cleaning
docker stop backendtracking
docker rm backendtracking
docker image rm backendtracking

# Building
docker build -t backendtracking .
docker-compose up -d