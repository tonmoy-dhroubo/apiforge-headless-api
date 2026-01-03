#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PID_DIR="$ROOT/.run/pids"
LOG_DIR="$ROOT/.run/logs"
SERVICES=(api-gateway auth-service content-service content-type-service media-service permission-service)

if [ ! -d "$PID_DIR" ]; then
  echo "No PID directory found at $PID_DIR"
  exit 0
fi

for s in "${SERVICES[@]}"; do
  pid_file="$PID_DIR/$s.pid"
  if [ ! -f "$pid_file" ]; then
    echo "$s: not running"
    continue
  fi

  pid="$(cat "$pid_file")"
  if kill -0 "$pid" 2>/dev/null; then
    log_file="$LOG_DIR/$s.log"
    if [ -f "$log_file" ]; then
      echo "$s: running (pid $pid, log $log_file)"
    else
      echo "$s: running (pid $pid)"
    fi
  else
    echo "$s: stale pid $pid"
  fi
done
