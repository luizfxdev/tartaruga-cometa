#!/bin/bash

echo "========================================="
echo "🐢 TARTARUGA COMETA - INICIANDO SISTEMA"
echo "========================================="
echo ""

# [1/3] Verificar PostgreSQL
echo "📦 [1/3] Verificando Banco de Dados..."
if ! sudo systemctl is-active --quiet postgresql; then
    echo "   ⚠️  PostgreSQL não está rodando"
    echo "   💡 Inicie manualmente com: sudo systemctl start postgresql"
    read -p "   Deseja continuar mesmo assim? (s/n): " continuar
    if [ "$continuar" != "s" ]; then
        echo "   ❌ Operação cancelada"
        exit 1
    fi
else
    echo "   ✅ PostgreSQL está rodando"
fi
echo ""

# [2/3] Compilar projeto
echo "🔨 [2/3] Compilando projeto..."
./gradlew clean build > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "   ✅ Projeto compilado com sucesso"
else
    echo "   ❌ Erro na compilação"
    exit 1
fi
echo ""

# [3/3] Iniciar servidor Tomcat
echo "🚀 [3/3] Iniciando servidor Tomcat..."
echo ""

./gradlew appRun

echo ""
echo "========================================="
echo "✅ SISTEMA INICIADO COM SUCESSO!"
echo "========================================="
echo ""
echo "📍 Acesse a aplicação em:"
echo "   🌐 http://localhost:8080/tartaruga-cometa/"
echo ""
echo "📊 Informações:"
echo "   🗄️  PostgreSQL na porta 5432"
echo "   📁 Database: tartaruga_cometa"
echo "   🌐 Tomcat na porta 8080"
echo ""
echo "🛑 Para parar o servidor:"
echo "   Pressione Ctrl+C"
echo ""
echo "========================================="
