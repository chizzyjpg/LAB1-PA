#!/usr/bin/env bash
set -e
BASE_DIR="$(cd "$(dirname "$0")" && pwd)"
export CATALINA_HOME="$BASE_DIR/apache-tomcat-10.1.36"
"$CATALINA_HOME/bin/shutdown.sh"
