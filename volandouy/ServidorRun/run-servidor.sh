#!/usr/bin/env bash
set -e
BASE_DIR="$(cd "$(dirname "$0")" && pwd)"
export JAVA_HOME="$BASE_DIR/jdk-17"
export PATH="$JAVA_HOME/bin:$PATH"

echo "JAVA_HOME=$JAVA_HOME"
java -jar "$BASE_DIR/servidor.jar"
