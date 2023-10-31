#!/bin/sh

echo "...Clean Docker Start..."
containers=$(docker ps -qa)
if [ -n "$containers" ]; then
  docker rm -f $containers
else
  echo "No containers to remove."
fi
images=$(docker ps -qa)
if [ -n "$images" ]; then
  docker rm -f $images
else
  echo "No $images to remove."
fi
echo "...Clean Docker Done..."