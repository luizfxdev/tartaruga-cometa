#!/bin/bash

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

echo "=========================================="
echo "Tartaruga Cometa - Deploy no Tomcat"
echo "=========================================="
echo ""

read -p "Digite o caminho do Tomcat (ex: /opt/tomcat): " TOMCAT_HOME

if [ ! -d "$TOMCAT_HOME" ]; then
    log_error "Diretório do Tomcat não encontrado: $TOMCAT_HOME"
    exit 1
fi

if [ ! -d "$TOMCAT_HOME/webapps" ]; then
    log_error "Diretório webapps não encontrado em: $TOMCAT_HOME"
    exit 1
fi

log_info "Compilando aplicação..."
cd "$PROJECT_DIR"
./gradlew clean build -x test

WAR_FILE="$PROJECT_DIR/build/libs/tartaruga-cometa.war"

if [ ! -f "$WAR_FILE" ]; then
    log_error "Arquivo WAR não encontrado: $WAR_FILE"
    exit 1
fi

log_info "Parando Tomcat..."
if [ -f "$TOMCAT_HOME/bin/shutdown.sh" ]; then
    "$TOMCAT_HOME/bin/shutdown.sh" || true
    sleep 3
fi

log_info "Removendo deploy anterior..."
rm -rf "$TOMCAT_HOME/webapps/tartaruga-cometa"
rm -f "$TOMCAT_HOME/webapps/tartaruga-cometa.war"

log_info "Copiando novo WAR..."
cp "$WAR_FILE" "$TOMCAT_HOME/webapps/"

log_info "Iniciando Tomcat..."
"$TOMCAT_HOME/bin/startup.sh"

sleep 3

log_info "Aguardando inicialização da aplicação..."
sleep 5

echo ""
log_info "Deploy concluído!"
log_info "Acesse: http://localhost:8080/tartaruga-cometa"
