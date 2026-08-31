#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

DB_USER="${DB_USER:-tartaruga_user}"
DB_NAME="${DB_NAME:-tartaruga_cometa_db}"
export APP_HOST_PORT="${APP_HOST_PORT:-8080}"
export DB_HOST_PORT="${DB_HOST_PORT:-5433}"
APP_URL="http://localhost:${APP_HOST_PORT}/tartaruga-cometa"

echo "Iniciando Tartaruga Cometa..."

if ! command -v docker &>/dev/null; then
    echo "ERRO: Docker não encontrado. Instale Docker e tente novamente."
    exit 1
fi

if ! docker compose version &>/dev/null; then
    echo "ERRO: docker compose não encontrado."
    exit 1
fi

liberar_porta() {
    local porta="$1"

    local container
    container="$(docker ps --format '{{.Names}} {{.Ports}}' | awk -v p=":${porta}->" '$0 ~ p {print $1; exit}')"
    if [ -n "$container" ]; then
        echo "Porta ${porta} em uso pelo container Docker '${container}'. Parando esse container..."
        docker stop "$container" >/dev/null
        return
    fi

    if command -v fuser &>/dev/null && fuser "${porta}/tcp" &>/dev/null; then
        echo "Porta ${porta} em uso por um processo local. Encerrando processo..."
        fuser -k "${porta}/tcp" &>/dev/null || true
        sleep 1
    fi
}

for porta in "$APP_HOST_PORT" "$DB_HOST_PORT"; do
    if (echo >/dev/tcp/127.0.0.1/"$porta") &>/dev/null; then
        liberar_porta "$porta"
    fi
done

echo "Subindo banco e aplicação..."
docker compose up --build -d

echo "Aguardando banco ficar saudável..."
MAX=30
COUNT=0
until docker compose exec -T db pg_isready -U "$DB_USER" -d "$DB_NAME" &>/dev/null; do
    COUNT=$((COUNT + 1))
    if [ "$COUNT" -ge "$MAX" ]; then
        echo "ERRO: Banco não respondeu em ${MAX} tentativas."
        docker compose logs db
        exit 1
    fi
    sleep 2
done

echo "Aguardando aplicação ficar saudável..."
COUNT=0
until docker compose ps app --format '{{.Health}}' 2>/dev/null | grep -q healthy; do
    COUNT=$((COUNT + 1))
    if [ "$COUNT" -ge "$MAX" ]; then
        echo "ERRO: Aplicação não respondeu em ${MAX} tentativas."
        docker compose logs app
        exit 1
    fi
    sleep 2
done

echo "Sistema disponível em ${APP_URL}"
echo "Para parar: docker compose down"
