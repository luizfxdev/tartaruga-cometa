#!/bin/bash

# ==============================================================================
# 🐢 TARTARUGA COMETA - SCRIPT DE INICIALIZAÇÃO DO SISTEMA
# ==============================================================================

# Cores para melhor visualização no terminal
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=========================================${NC}"
echo -e "${BLUE}🐢 TARTARUGA COMETA - INICIANDO SISTEMA${NC}"
echo -e "${BLUE}=========================================${NC}"
echo ""

# ==============================================================================
# [0/4] Configurações e Variáveis (extraídas dos arquivos de configuração)
# ==============================================================================

echo -e "${BLUE}⚙️ [0/4] Carregando configurações...${NC}"

# Extrair informações do database.properties
# Usamos 'grep' para encontrar a linha e 'cut' para pegar o valor após o '='
# 'sed' é usado para remover o prefixo 'jdbc:postgresql://' da URL
DB_URL_FULL=$(grep 'db.url' src/main/resources/database.properties | cut -d'=' -f2)
DB_URL_NO_PREFIX=$(echo "$DB_URL_FULL" | sed 's/jdbc:postgresql:\/\///')
DB_HOST=$(echo "$DB_URL_NO_PREFIX" | cut -d':' -f1)
DB_PORT=$(echo "$DB_URL_NO_PREFIX" | cut -d'/' -f1 | cut -d':' -f2)
DB_NAME=$(echo "$DB_URL_NO_PREFIX" | cut -d'/' -f2)
DB_USER=$(grep 'db.username' src/main/resources/database.properties | cut -d'=' -f2)
DB_PASSWORD=$(grep 'db.password' src/main/resources/database.properties | cut -d'=' -f2)

# Extrair informações do build.gradle para o Gretty/Tomcat
# 'awk' é usado para extrair o valor numérico da porta e o string do contexto
APP_PORT=$(grep 'httpPort' build.gradle | awk '{print $3}')
APP_CONTEXT=$(grep 'contextPath' build.gradle | awk -F"'" '{print $2}')
APP_URL="http://localhost:${APP_PORT}${APP_CONTEXT}/"

echo -e "    ${GREEN}✅ Configurações carregadas:${NC}"
echo -e "        ${BLUE}Banco de Dados:${NC} ${DB_HOST}:${DB_PORT}/${DB_NAME} (Usuário: ${DB_USER})"
echo -e "        ${BLUE}Aplicação Web:${NC} Porta: ${APP_PORT}, Contexto: ${APP_CONTEXT}"
echo ""

# ==============================================================================
# Funções Auxiliares
# ==============================================================================

# Função para verificar se um comando existe no PATH do sistema
command_exists () {
    type "$1" &> /dev/null ;
}

# Função para verificar e iniciar o PostgreSQL
check_and_start_postgresql() {
    echo -e "${BLUE}📦 [1/4] Verificando Banco de Dados PostgreSQL...${NC}"

    # Verifica se o cliente psql está instalado para poder testar a conectividade
    if ! command_exists psql; then
        echo -e "    ${YELLOW}⚠️  Comando 'psql' não encontrado. Não será possível verificar a conectividade com o DB.${NC}"
        echo -e "    ${YELLOW}💡 Certifique-se de ter o cliente PostgreSQL instalado (ex: sudo apt install postgresql-client).${NC}"
    fi

    # Verifica se o serviço PostgreSQL está ativo
    if ! sudo systemctl is-active --quiet postgresql; then
        echo -e "    ${YELLOW}⚠️  PostgreSQL não está rodando.${NC}"
        read -p "    Deseja tentar iniciar o PostgreSQL agora? (s/n): " start_db_now
        if [ "$start_db_now" == "s" ]; then
            echo -e "    ${BLUE}🔄 Tentando iniciar PostgreSQL...${NC}"
            if sudo systemctl start postgresql; then
                echo -e "    ${GREEN}✅ PostgreSQL iniciado com sucesso!${NC}"
            else
                echo -e "    ${RED}❌ Falha ao iniciar PostgreSQL. Verifique os logs do sistema.${NC}"
                read -p "    Deseja continuar mesmo assim? (s/n): " continuar_sem_db
                if [ "$continuar_sem_db" != "s" ]; then
                    echo -e "    ${RED}❌ Operação cancelada.${NC}"
                    exit 1
                fi
            fi
        else
            read -p "    Deseja continuar mesmo assim? (s/n): " continuar_sem_db
            if [ "$continuar_sem_db" != "s" ]; then
                echo -e "    ${RED}❌ Operação cancelada.${NC}"
                exit 1
            fi
        fi
    else
        echo -e "    ${GREEN}✅ PostgreSQL está rodando.${NC}"
    fi

    # Verificar conectividade com o banco de dados (se psql estiver disponível e PostgreSQL ativo)
    if command_exists psql && sudo systemctl is-active --quiet postgresql; then
        echo -e "    ${BLUE}🔗 Verificando conectividade com o banco de dados '${DB_NAME}'...${NC}"
        # Tenta conectar usando as credenciais do database.properties
        # O timeout é para evitar que o script fique travado se o DB não responder
        # PGPASSWORD é uma variável de ambiente que o psql reconhece para senhas
        if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c '\q' &> /dev/null; then
            echo -e "    ${GREEN}✅ Conexão com o banco de dados '${DB_NAME}' estabelecida com sucesso!${NC}"
        else
            echo -e "    ${YELLOW}⚠️  Não foi possível conectar ao banco de dados '${DB_NAME}' com as credenciais fornecidas.${NC}"
            echo -e "    ${YELLOW}💡 Verifique as configurações em 'src/main/resources/database.properties' e se o banco de dados e usuário existem.${NC}"
            read -p "    Deseja continuar mesmo assim? (s/n): " continuar_sem_conexao
            if [ "$continuar_sem_conexao" != "s" ]; then
                echo -e "    ${RED}❌ Operação cancelada.${NC}"
                exit 1
            fi
        fi
    fi
    echo ""
}

# Função para compilar o projeto
compile_project() {
    echo -e "${BLUE}🔨 [2/4] Compilando projeto...${NC}"

    # CORREÇÃO: Verificar se o arquivo gradlew existe no diretório atual
    if [ ! -f "./gradlew" ]; then
        echo -e "    ${RED}❌ Arquivo 'gradlew' não encontrado no diretório atual.${NC}"
        echo -e "    ${RED}💡 Certifique-se de que o Gradle Wrapper está presente na raiz do projeto.${NC}"
        exit 1
    fi

    # CORREÇÃO: Verificar se o arquivo gradlew tem permissão de execução
    if [ ! -x "./gradlew" ]; then
        echo -e "    ${RED}❌ O arquivo 'gradlew' não tem permissão de execução.${NC}"
        echo -e "    ${RED}💡 Por favor, execute 'chmod +x gradlew' no terminal e tente novamente.${NC}"
        exit 1
    fi

    echo -e "    ${BLUE}🔄 Executando './gradlew clean build'...${NC}"
    # Limpar e compilar. Redireciona a saída completa para um arquivo de log e exibe erros no console.
    ./gradlew clean build > build.log 2>&1

    if [ $? -eq 0 ]; then
        echo -e "    ${GREEN}✅ Projeto compilado com sucesso!${NC}"
        echo -e "    ${BLUE}ℹ️  Detalhes da compilação em: build.log${NC}"
    else
        echo -e "    ${RED}❌ Erro na compilação.${NC}"
        echo -e "    ${RED}ℹ️  Verifique o arquivo 'build.log' para detalhes do erro.${NC}"
        exit 1
    fi
    echo ""
}

# Função para iniciar o servidor Tomcat
start_tomcat() {
    echo -e "${BLUE}🚀 [3/4] Iniciando servidor Tomcat (via Gretty)...${NC}"
    echo ""

    # Pergunta ao usuário se deseja iniciar em segundo plano
    read -p "    Deseja iniciar o Tomcat em segundo plano (background)? (s/n): " run_in_background
    if [ "$run_in_background" == "s" ]; then
        echo -e "    ${BLUE}ℹ️  Tomcat será iniciado em segundo plano. Use 'fg' para trazê-lo de volta ou 'kill' para parar.${NC}"
        echo -e "    ${BLUE}ℹ️  Logs do Tomcat serão exibidos no terminal ou em um arquivo de log se configurado pelo Gretty.${NC}"
        ./gradlew appRun &
        TOMCAT_PID=$! # Captura o PID do processo em segundo plano
        echo -e "    ${GREEN}✅ Tomcat iniciado em segundo plano com PID: ${TOMCAT_PID}${NC}"
        echo -e "    ${BLUE}💡 Para parar, use 'kill ${TOMCAT_PID}' ou 'pkill -f 'gradlew appRun''.${NC}"
    else
        echo -e "    ${BLUE}ℹ️  Tomcat será iniciado em primeiro plano. Pressione Ctrl+C para parar.${NC}"
        ./gradlew appRun
    fi
    echo ""
}

# ==============================================================================
# Fluxo Principal do Script
# ==============================================================================

check_and_start_postgresql
compile_project
start_tomcat

echo -e "${BLUE}=========================================${NC}"
echo -e "${GREEN}✅ SISTEMA INICIADO COM SUCESSO!${NC}"
echo -e "${BLUE}=========================================${NC}"
echo ""
echo -e "${BLUE}📍 Acesse a aplicação em:${NC}"
echo -e "    ${GREEN}🌐 ${APP_URL}${NC}"
echo ""
echo -e "${BLUE}📊 Informações:${NC}"
echo -e "    ${BLUE}🗄️  PostgreSQL:${NC} ${DB_HOST}:${DB_PORT}/${DB_NAME}"
echo -e "    ${BLUE}👤 Usuário DB:${NC} ${DB_USER}"
echo -e "    ${BLUE}🌐 Tomcat:${NC} Porta ${APP_PORT}, Contexto ${APP_CONTEXT}"
echo ""
echo -e "${BLUE}🛑 Para parar o servidor (se rodando em primeiro plano):${NC}"
echo -e "    Pressione ${RED}Ctrl+C${NC}"
if [ "$run_in_background" == "s" ]; then
    echo -e "${BLUE}🛑 Para parar o servidor (se rodando em segundo plano):${NC}"
    echo -e "    Use ${RED}kill ${TOMCAT_PID}${NC} ou ${RED}pkill -f 'gradlew appRun'${NC}"
fi
echo ""
echo -e "${BLUE}=========================================${NC}"
