#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PID_DIR="$ROOT/.run/pids"
SERVICES=(api-gateway auth-service content-service content-type-service media-service permission-service)

if [ ! -d "$PID_DIR" ]; then
  echo "No PID directory found at $PID_DIR"
  exit 0
fi

for s in "${SERVICES[@]}"; do
  pid_file="$PID_DIR/$s.pid"
  if [ ! -f "$pid_file" ]; then
    echo "$s: no pid file"
    continue
  fi

  pid="$(cat "$pid_file")"
  if kill -0 "$pid" 2>/dev/null; then
    kill "$pid"
    for _ in {1..30}; do
      if kill -0 "$pid" 2>/dev/null; then
        sleep 1
      else
        break
      fi
    done
    if kill -0 "$pid" 2>/dev/null; then
      kill -9 "$pid" || true
    fi
    echo "$s: stopped"
  else
    echo "$s: pid $pid not running"
  fi

  rm -f "$pid_file"
done
