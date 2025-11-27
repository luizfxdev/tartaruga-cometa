.PHONY: help build test clean war setup-db deploy all

help:
    @echo "Tartaruga Cometa - Comandos Disponíveis"
    @echo ""
    @echo "make build      - Compilar e construir WAR"
    @echo "make test       - Executar testes"
    @echo "make clean      - Limpar build"
    @echo "make war        - Gerar arquivo WAR"
    @echo "make setup-db   - Inicializar banco de dados"
    @echo "make all        - Compilar, testar e gerar WAR"
    @echo "make deploy     - Deploy no Tomcat"
    @echo ""

build:
    @echo "Compilando e construindo WAR..."
    ./gradlew clean build -x test
    @echo "✓ WAR construído: build/libs/tartaruga-cometa.war"

test:
    @echo "Executando testes..."
    ./gradlew test

clean:
    @echo "Limpando build..."
    ./gradlew clean
    @echo "✓ Build limpo"

war: build
    @echo "✓ WAR disponível em: build/libs/tartaruga-cometa.war"

setup-db:
    @bash setup-db.sh

all: clean build test
    @echo "✓ Pipeline completo concluído!"

deploy:
    @bash deploy.sh

.DEFAULT_GOAL := help
