#!/bin/bash
echo "Stopping all RideLink Microservices..."
if [ -f .services.pid ]; then
  kill $(cat .services.pid) 2>/dev/null
  rm .services.pid
  echo "Services stopped."
else
  # Fallback to kill by ports
  lsof -ti :8081 | xargs kill -9 2>/dev/null
  lsof -ti :8082 | xargs kill -9 2>/dev/null
  lsof -ti :8083 | xargs kill -9 2>/dev/null
  lsof -ti :8084 | xargs kill -9 2>/dev/null
  echo "Stopped any processes listening on ports 8081-8084."
fi
