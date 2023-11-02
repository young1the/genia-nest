#!/bin/sh

echo "...Clean Docker Start..."
containers=$(docker ps -qa)
if [ -n "$containers" ]; then
  docker rm -f $containers
else
  echo "No containers to remove."
fi
images=$(docker images -q)
if [ -n "$images" ]; then
  docker rmi -f $images
else
  echo "No images to remove."
fi
sudo docker system prune --volumes --force
echo "...Clean Docker Done..."