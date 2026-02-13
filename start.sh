#!/bin/bash

set -e

BACKEND_DIR="backend"
FRONTEND_DIR="frontend"
BACKEND_PORT=8080
FRONTEND_PORT=5173
FRONTEND_URL="http://localhost:5173"

echo "Starting Healthcare Billing System..."
echo ""

if [ ! -d "$BACKEND_DIR" ]; then
  echo "Backend directory not found"
  exit 1
fi

if [ ! -d "$FRONTEND_DIR" ]; then
  echo "Frontend directory not found"
  exit 1
fi

echo "Starting backend..."
cd $BACKEND_DIR
./gradlew run &
BACKEND_PID=$!
cd ..
echo "Backend started on port $BACKEND_PORT"
echo ""

echo "Starting frontend..."
cd $FRONTEND_DIR

if [ ! -d "node_modules" ]; then
  echo "Installing frontend dependencies..."
  npm install
fi

npm run dev &
FRONTEND_PID=$!
cd ..
echo "Frontend started on port $FRONTEND_PORT"
echo ""

echo "Opening frontend in browser..."
sleep 3

if command -v open >/dev/null 2>&1; then
  open $FRONTEND_URL
elif command -v xdg-open >/dev/null 2>&1; then
  xdg-open $FRONTEND_URL
elif command -v start >/dev/null 2>&1; then
  start $FRONTEND_URL
else
  echo "Please open manually: $FRONTEND_URL"
fi

echo ""
echo "Application is running"
echo "Backend:  http://localhost:$BACKEND_PORT"
echo "Frontend: $FRONTEND_URL"
echo ""
echo "Press Ctrl+C to stop"

trap "echo 'Stopping...'; kill $BACKEND_PID $FRONTEND_PID; exit 0" SIGINT

wait
