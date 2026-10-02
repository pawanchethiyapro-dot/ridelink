#!/bin/bash
echo "=========================================================="
echo " Starting RideLink Microservices Platform..."
echo "=========================================================="

mkdir -p logs

echo "1. Starting Account Service on port 8081..."
(cd account-service && mvn spring-boot:run > ../logs/account-service.log 2>&1) &
PID_ACCOUNT=$!

echo "2. Starting Driver & Vehicle Service on port 8082..."
(cd driver-service && mvn spring-boot:run > ../logs/driver-service.log 2>&1) &
PID_DRIVER=$!

echo "3. Starting Ride Management Service on port 8083..."
(cd ride-service && mvn spring-boot:run > ../logs/ride-service.log 2>&1) &
PID_RIDE=$!

echo "4. Starting Fare & Payment Service on port 8084..."
(cd fare-service && mvn spring-boot:run > ../logs/fare-service.log 2>&1) &
PID_FARE=$!

echo "PIDs: Account=$PID_ACCOUNT, Driver=$PID_DRIVER, Ride=$PID_RIDE, Fare=$PID_FARE"
echo "$PID_ACCOUNT $PID_DRIVER $PID_RIDE $PID_FARE" > .services.pid

echo "All services are starting in background."
echo "Check logs in logs/ folder (e.g. tail -f logs/ride-service.log)"
echo "Run ./stop-all.sh to terminate all services."
