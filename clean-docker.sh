#!/bin/sh

echo "...Clean Docker..."
docker rm -f $(docker ps -qa)
docker rmi $(docker images -q)