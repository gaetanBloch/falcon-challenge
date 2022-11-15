#!/bin/sh

./mvnw clean package -pl web -am
cd web
docker build -f src/main/docker/Dockerfile.uber-jar -t quarkus/falconchallenge-uber-jvm .
docker run -i --rm -p 8080:8080 quarkus/falconchallenge-uber-jvm
