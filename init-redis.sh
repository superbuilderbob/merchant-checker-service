#!/bin/sh
set -e
set -x
set -m

# Run main process in the background to unblock script execution
/usr/bin/redis-stack-server &

# Define PID
REDIS_PID=$!

# Wait for Redis to be ready
until redis-cli ping; do
  sleep 1
done

# Once Redis is ready, create Redis index
redis-cli FT.CREATE myIndex SCHEMA title TEXT body TEXT

# Wait forever to prevent main process from exiting and dev-redis container exiting unexpectedly
wait $REDIS_PID