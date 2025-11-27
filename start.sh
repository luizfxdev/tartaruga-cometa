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

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

echo "=========================================="
echo "Tartaruga Cometa - Sistema de Entregas"
echo "=========================================="
echo ""

# Verificar Java
log_info "Verificando Java..."
if ! command -v java &> /dev/null; then
    log_error "Java não encontrado. Instale Java 8 ou superior."
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[^"]*')
log_info "Java $JAVA_VERSION encontrado"

# Verificar PostgreSQL
log_info "Verificando PostgreSQL..."
if ! command -v psql &> /dev/null; then
    log_warn "PostgreSQL não encontrado no PATH"
    log_warn "Certifique-se de que PostgreSQL está instalado e rodando"
else
    log_info "PostgreSQL encontrado"
fi

# Criar diretório de logs
if [ ! -d "$PROJECT_DIR/logs" ]; then
    mkdir -p "$PROJECT_DIR/logs"
    log_info "Diretório de logs criado"
fi

echo ""
echo "Escolha uma opção:"
echo "1) Compilar e construir WAR"
echo "2) Executar testes"
echo "3) Compilar + Testes + WAR"
echo "4) Inicializar banco de dados"
echo "5) Limpar build"
echo "6) Executar tudo"
echo ""
read -p "Digite a opção (1-6): " option

case $option in
    1)
        log_info "Compilando e construindo WAR..."
        cd "$PROJECT_DIR"
        ./gradlew clean build -x test
        log_info "WAR construído com sucesso"
        log_info "Localização: build/libs/tartaruga-cometa.war"
        ;;
    2)
        log_info "Executando testes..."
        cd "$PROJECT_DIR"
        ./gradlew test
        ;;
    3)
        log_info "Compilando, testando e construindo WAR..."
        cd "$PROJECT_DIR"
        ./gradlew clean build
        log_info "WAR construído com sucesso"
        log_info "Localização: build/libs/tartaruga-cometa.war"
        ;;
    4)
        log_info "Inicializando banco de dados..."
        bash "$PROJECT_DIR/setup-db.sh"
        ;;
    5)
        log_info "Limpando build..."
        cd "$PROJECT_DIR"
        ./gradlew clean
        log_info "Build limpo"
        ;;
    6)
        log_info "Executando pipeline completo..."
        cd "$PROJECT_DIR"

        log_info "Limpando build anterior..."
        ./gradlew clean

        log_info "Compilando, testando e construindo WAR..."
        ./gradlew build

        log_info "Pipeline completo concluído!"
        log_info "WAR disponível em: build/libs/tartaruga-cometa.war"
        ;;
    *)
        log_error "Opção inválida"
        exit 1
        ;;
esac

echo ""
log_info "Operação concluída com sucesso!"
