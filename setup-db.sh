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
echo "Tartaruga Cometa - Setup do Banco de Dados"
echo "=========================================="
echo ""

DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="tartaruga_cometa"
DB_USER="postgres"
DB_PASSWORD="postgres"

log_info "Configurações:"
echo "  Host: $DB_HOST"
echo "  Porta: $DB_PORT"
echo "  Banco: $DB_NAME"
echo "  Usuário: $DB_USER"
echo ""

read -p "Deseja prosseguir? (s/n): " confirm
if [ "$confirm" != "s" ] && [ "$confirm" != "S" ]; then
    log_info "Operação cancelada"
    exit 0
fi

log_info "Testando conexão com PostgreSQL..."
if ! PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -tc "SELECT 1" > /dev/null 2>&1; then
    log_error "Não foi possível conectar ao PostgreSQL"
    log_error "Verifique se PostgreSQL está rodando e a senha está correta"
    exit 1
fi
log_info "Conexão bem-sucedida!"

log_info "Criando banco de dados..."
PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME'" | grep -q 1 || \
PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -c "CREATE DATABASE $DB_NAME;"
log_info "Banco de dados criado/verificado"

log_info "Executando schema.sql..."
PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$PROJECT_DIR/src/main/resources/schema.sql" > /dev/null
log_info "Schema criado com sucesso"

log_info "Carregando dados de exemplo..."
PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$PROJECT_DIR/src/main/resources/data-sample.sql" > /dev/null
log_info "Dados de exemplo carregados"

log_info "Verificando tabelas criadas..."
TABLE_COUNT=$(PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tc "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public'" | xargs)

echo ""
log_info "Banco de dados inicializado com sucesso!"
log_info "Total de tabelas criadas: $TABLE_COUNT"
