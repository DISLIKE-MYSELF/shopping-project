#!/bin/bash

host="database"
port="3306"
timeout=60

echo "等待数据库启动: $host:$port"
for ((i=timeout; i>0; i--)); do
  if bash -c "echo > /dev/tcp/$host/$port" 2>/dev/null; then
    echo "数据库已就绪，启动应用..."
    exec "$@"
    exit 0
  fi
  sleep 1
done

echo "数据库连接超时"
exit 1