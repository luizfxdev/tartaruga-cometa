# 🐢 Tartaruga Cometa - Sistema de Gerenciamento de Entregas

![Java](https://img.shields.io/badge/Java-1.8-orange?style=flat-square&logo=java)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-6.0-blue?style=flat-square)
![Tomcat](https://img.shields.io/badge/Tomcat-10-yellow?style=flat-square&logo=apache-tomcat)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?style=flat-square&logo=postgresql)
![Gradle](https://img.shields.io/badge/Gradle-8.0+-green?style=flat-square&logo=gradle)
![Status](https://img.shields.io/badge/Status-Ativo-success?style=flat-square)

## 📋 Descrição do Projeto

Sistema web completo para gerenciamento e rastreamento de entregas de uma transportadora. Desenvolvido com Jakarta EE, Servlet API e PostgreSQL, permitindo controle total de clientes, endereços, produtos e entregas com histórico de status.

## 🎯 Funcionalidades Principais

✅ Gerenciamento de Clientes (Pessoa Física e Jurídica)  
✅ Cadastro de Endereços com tipo e endereço principal  
✅ Catálogo de Produtos com peso e volume  
✅ Sistema de Entregas com rastreamento  
✅ Histórico de Status de Entregas  
✅ Busca e Filtros Avançados  
✅ Autenticação de Sessão  
✅ Encoding UTF-8 em toda aplicação

## 📊 Diagrama de Entidade-Relacionamento (DER)

```
┌─────────────┐         ┌──────────────┐         ┌──────────────┐
│   CLIENTS   │◄────────┤  ADDRESSES   │────────►│   PRODUCTS   │
├─────────────┤         ├──────────────┤         ├──────────────┤
│ id (PK)     │         │ id (PK)      │         │ id (PK)      │
│ personType  │         │ clientId(FK) │         │ name         │
│ document    │         │ addressType  │         │ description  │
│ name        │         │ street       │         │ weightKg     │
│ email       │         │ number       │         │ volumeM3     │
│ phone       │         │ isPrincipal  │         │ declaredValue│
│ createdAt   │         │ createdAt    │         │ category     │
└─────────────┘         └──────────────┘         │ active       │
      ▲                                           └──────────────┘
      │                                                   ▲
      │                                                   │
      └───────────────────┬────────────────────────────┬─┘
                          │                            │
                    ┌─────▼────────────┐       ┌──────▼───────────┐
                    │   DELIVERIES     │       │ DELIVERY_HISTORY │
                    ├──────────────────┤       ├──────────────────┤
                    │ id (PK)          │◄──────┤ id (PK)          │
                    │ trackingCode     │       │ deliveryId (FK)  │
                    │ shipperId (FK)   │       │ status           │
                    │ recipientId (FK) │       │ observations     │
                    │ originAddressId  │       │ user             │
                    │ destAddressId    │       │ changedAt        │
                    │ status           │       └──────────────────┘
                    │ freightValue     │
                    │ observations     │
                    │ createdAt        │
                    └──────────────────┘
```

## 🏗️ Estrutura do Projeto

```
tartaruga-cometa/
├── src/
│   ├── main/
│   │   ├── java/com/tartarugacometasystem/
│   │   │   ├── config/
│   │   │   │   └── ApplicationListener.java
│   │   │   ├── controller/
│   │   │   │   ├── AddressServlet.java
│   │   │   │   ├── ClientServlet.java
│   │   │   │   ├── DeliveryServlet.java
│   │   │   │   └── ProductServlet.java
│   │   │   ├── filter/
│   │   │   │   └── EncodingFilter.java
│   │   │   ├── model/
│   │   │   │   ├── Address.java
│   │   │   │   ├── Client.java
│   │   │   │   ├── Delivery.java
│   │   │   │   ├── DeliveryHistory.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── AddressType.java
│   │   │   │   ├── DeliveryStatus.java
│   │   │   │   └── PersonType.java
│   │   │   ├── service/
│   │   │   │   ├── AddressService.java
│   │   │   │   ├── ClientService.java
│   │   │   │   ├── DeliveryService.java
│   │   │   │   └── ProductService.java
│   │   │   ├── repository/
│   │   │   │   ├── AddressRepository.java
│   │   │   │   ├── ClientRepository.java
│   │   │   │   ├── DeliveryRepository.java
│   │   │   │   └── ProductRepository.java
│   │   │   ├── util/
│   │   │   │   ├── ConnectionPool.java
│   │   │   │   └── Mapper.java
│   │   │   └── exception/
│   │   │       └── DatabaseException.java
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       ├── pages/
│   │       │   ├── addresses/
│   │       │   ├── clients/
│   │       │   ├── deliveries/
│   │       │   └── products/
│   │       ├── css/
│   │       ├── js/
│   │       ├── images/
│   │       ├── index.jsp
│   │       └── error.jsp
│   └── test/
│       └── java/com/tartarugacometasystem/
├── database/
│   └── schema.sql
├── gradle/
├── build.gradle
├── gradlew
├── gradlew.bat
├── README.md
├── requirements.md
├── api-endpoints.md
├── database-schema.md
└── use-cases.md
```

## 🔧 Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| Java | 1.8 | Linguagem Principal |
| Jakarta EE | 6.0 | Framework Web |
| Apache Tomcat | 10 | Servidor de Aplicação |
| PostgreSQL | 15+ | Banco de Dados |
| Gradle | 8.0+ | Gerenciador de Build |
| JDBC | - | Acesso ao Banco |
| JSP | - | Camada de Apresentação |

## 📦 Dependências Principais

```gradle
dependencies {
    implementation 'jakarta.servlet:jakarta.servlet-api:6.0.0'
    implementation 'org.postgresql:postgresql:42.7.1'
    implementation 'com.zaxxer:HikariCP:5.0.1'
    testImplementation 'junit:junit:4.13.2'
}
```

## 🚀 Instalação e Configuração

### Pré-requisitos

- Java 11+ instalado
- PostgreSQL 15+ instalado e rodando
- Apache Tomcat 10+ instalado
- Git instalado

### 1️⃣ Clonar o Repositório

```bash
git clone https://github.com/luizfxdev/tartaruga-cometa.git
cd tartaruga-cometa
```

### 2️⃣ Configurar Banco de Dados

**Criar Banco de Dados**
```bash
psql -U postgres -c "CREATE DATABASE tartaruga_cometa;"
```

**Executar Script SQL**
```bash
psql -U postgres -d tartaruga_cometa -f database/schema.sql
```

**Verificar Criação das Tabelas**
```bash
psql -U postgres -d tartaruga_cometa -c "\dt"
```

### 3️⃣ Configurar Variáveis de Conexão

Edite o arquivo `src/main/java/com/tartarugacometasystem/util/ConnectionPool.java`:

```java
// Altere estas variáveis conforme seu ambiente
private static final String DB_URL = "jdbc:postgresql://localhost:5432/tartaruga_cometa";
private static final String DB_USER = "postgres";
private static final String DB_PASSWORD = "sua_senha";
private static final int POOL_SIZE = 10;
```

### 4️⃣ Build da Aplicação

```bash
./gradlew clean build
```

O arquivo WAR será gerado em: `build/libs/tartaruga-cometa.war`

### 5️⃣ Deploy no Tomcat

**Parar o Tomcat**
```bash
sudo service tomcat10 stop
```

**Limpar Deployment Anterior**
```bash
sudo rm -rf /var/lib/tomcat10/webapps/tartaruga-cometa*
sudo rm -rf /var/lib/tomcat10/work/Catalina/localhost/tartaruga-cometa
```

**Copiar WAR**
```bash
sudo cp build/libs/tartaruga-cometa.war /var/lib/tomcat10/webapps/
```

**Iniciar o Tomcat**
```bash
sudo service tomcat10 start
```

**Aguardar Inicialização**
```bash
sleep 15
```

**Verificar Deploy**
```bash
curl -I http://localhost:8080/tartaruga-cometa/
```
Resposta esperada: `HTTP/1.1 200`

## 🌐 Acesso à Aplicação

**URL Principal:** http://localhost:8080/tartaruga-cometa/

### Endpoints Principais

- **Clientes:** http://localhost:8080/tartaruga-cometa/clients/
- **Endereços:** http://localhost:8080/tartaruga-cometa/addresses/
- **Produtos:** http://localhost:8080/tartaruga-cometa/products/
- **Entregas:** http://localhost:8080/tartaruga-cometa/deliveries/

## ✅ Testes Realizados

### Testes de Build
✅ Compilação com Java 1.8 bem-sucedida  
✅ Geração do WAR sem erros  
✅ Resolução de conflitos Jakarta EE vs javax.servlet

### Testes de Deploy
✅ Deploy no Tomcat 10 bem-sucedido  
✅ Aplicação respondendo HTTP 200  
✅ Sessão criada com cookie TARTARUGACOMETASESSION  
✅ Encoding UTF-8 funcionando

### Testes de Funcionalidade
✅ Servlets carregando corretamente  
✅ Filter de encoding aplicado  
✅ Listener da aplicação inicializando  
✅ Páginas JSP renderizando

### Testes de Banco de Dados
✅ Conexão com PostgreSQL estabelecida  
✅ Pool de conexões HikariCP funcionando  
✅ Tabelas criadas conforme schema.sql  
✅ Relacionamentos entre tabelas validados

## 📝 Comandos Úteis

### Desenvolvimento

```bash
# Build limpo
./gradlew clean build

# Build sem testes
./gradlew build -x test

# Executar testes
./gradlew test

# Build e executa a aplicação no Tomcat embarcado via Gretty
./gradlew tomcatRunWar

# Ver logs do Tomcat
sudo tail -f /var/lib/tomcat10/logs/catalina.out

# Verificar status do Tomcat
sudo service tomcat10 status

# Reiniciar Tomcat
sudo service tomcat10 restart
```

### Banco de Dados

```bash
# Conectar ao banco
psql -U postgres -d tartaruga_cometa

# Listar tabelas
\dt

# Ver estrutura de uma tabela
\d clients

# Executar query
SELECT * FROM clients;

# Sair
\q
```

## 📥 Download

### Arquivo WAR Executável

O arquivo WAR está disponível em:

- **[Releases do GitHub](https://github.com/seu-usuario/tartaruga-cometa/releases)** - Versão mais recente
- **Build Local**: `build/libs/tartaruga-cometa.war` (após executar `./gradlew build`)

**Tamanho:** ~5MB
**Versão:** 1.0.0
**Data:** 27 de Novembro de 2025

---

## 📚 Documentação Adicional

- [API Endpoints](api-endpoints.md) - Documentação completa de endpoints
- [Requirements](requirements.md) - Requisitos funcionais e não-funcionais

---

## 👨‍💻 Autor

**Luiz Felipe de Oliveira**
- GitHub: [@luizfxdev](https://github.com/luizfxdev)
- LinkedIn: [in/luizfxdev](https://www.linkedin.com/in/luizfxdev)
- Portfólio: [luizfxdev.com.br](https://luizfxdev.com.br)

---

> Projeto desenvolvido como parte da Trilha de Aceleração proposta pela **GW Sistemas**.

---

## 📄 Licença

Este projeto está sob a licença MIT.

## 🔗 Links Importantes

- [Repositório GitHub](https://github.com/luizfxdev/tartaruga-cometa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Jakarta EE Documentation](https://jakarta.ee/specifications/)
- [Apache Tomcat Documentation](https://tomcat.apache.org/tomcat-10.0-doc/)



---


<div align="center">
🐢 Tartaruga Cometa - 
Sistema de Gerenciamento de Entregas

</div>
<div align="center">

⬆ Voltar ao topo
</div>


