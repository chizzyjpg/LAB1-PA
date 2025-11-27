#!/usr/bin/env bash
set -e

BASE_DIR="$(cd "$(dirname "$0")" && pwd)"

export CATALINA_HOME="$BASE_DIR/apache-tomcat-10.1.36"
export CATALINA_BASE="$CATALINA_HOME"

# Java portable si está
if [[ -x "$BASE_DIR/jdk-17/bin/java" ]]; then
  export JAVA_HOME="$BASE_DIR/jdk-17"
  export JRE_HOME="$JAVA_HOME"
  export PATH="$JAVA_HOME/bin:$PATH"
  echo "Usando Java portable: $JAVA_HOME"
else
  echo "Usando Java del sistema..."
fi

echo "CATALINA_HOME=$CATALINA_HOME"
echo "JAVA_HOME=$JAVA_HOME"

# 1) Levantar Tomcat en segundo plano
"$CATALINA_HOME/bin/startup.sh"

# 2) Esperar unos segundos a que levante
echo "Esperando que Tomcat arranque..."
sleep 7

# 3) URL de tu app
URL="http://localhost:8080/volandouy-oficial/home"

# 4) Intentar abrir el navegador según el sistema
if command -v xdg-open >/dev/null 2>&1; then
  xdg-open "$URL" >/dev/null 2>&1 &
elif command -v gnome-open >/dev/null 2>&1; then
  gnome-open "$URL" >/dev/null 2>&1 &
elif command -v open >/dev/null 2>&1; then  # macOS
  open "$URL" >/dev/null 2>&1 &
else
  echo "No pude abrir el navegador automáticamente."
  echo "Abrí tu navegador y pegá esta URL:"
  echo "  $URL"
fi
