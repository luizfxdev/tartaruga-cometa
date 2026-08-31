# 🐢 Tartaruga Cometa - Sistema de Gerenciamento de Entregas

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20Servlet-6.0-blue?style=flat-square)
![Tomcat](https://img.shields.io/badge/Tomcat-10.1-yellow?style=flat-square&logo=apache-tomcat)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)
![Gradle](https://img.shields.io/badge/Gradle-8.5-green?style=flat-square&logo=gradle)
![Docker](https://img.shields.io/badge/Docker-compose-2496ED?style=flat-square&logo=docker)
![Status](https://img.shields.io/badge/Status-Em%20refatoração-yellow?style=flat-square)

## 📋 Descrição do Projeto

Sistema web para gerenciamento e rastreamento de entregas de uma transportadora. Stack Java puro (sem framework de injeção de dependência ou ORM): `Servlet` + `JSP` + `JDBC` com `PreparedStatement`, build `Gradle`, servidor `Tomcat 10.1`, banco `PostgreSQL`.

O projeto está em refatoração para nomenclatura e domínio em **português** (`Cliente`, `Endereço`, `Produto`, `Entrega`), seguindo o fluxo em camadas `JSP → Controlador → BO → DAO → PostgreSQL`. Controlador não acessa DAO diretamente; DAO não contém regra de negócio.

---

## 🎯 Funcionalidades

| Domínio | Rota | CRUD |
|---|---|---|
| 👥 Clientes (PF/PJ) | `/cliente/` | ✅ Completo |
| 📍 Endereços (com endereço principal por cliente) | `/endereco/` | ✅ Completo |
| 📦 Produtos | `/produto/` | ✅ Completo |
| 🚚 Entregas + histórico de status | `/entrega/` | ⚠️ Ver [Limitações conhecidas](#-limitações-conhecidas) |

- Autenticação de sessão simples (tela de login)
- Busca por nome/documento/código em todas as listagens
- Histórico de mudança de status de entrega (tabela append-only, protegida por trigger)
- Connection pool via **HikariCP**
- Encoding UTF-8 ponta a ponta

---

## 🗄️ Modelo de dados

Tabelas em português, schema modular em `src/main/resources/db/`:

```
CLIENTE (1) ──< ENDERECO
CLIENTE (1) ──< ENTREGA        (id_remetente | id_destinatario)
ENDERECO (1) ──< ENTREGA        (id_endereco_origem | id_endereco_destino)
ENTREGA (1) ──< ENTREGA_PRODUTO >── (1) PRODUTO
ENTREGA (1) ──< HISTORICO_ENTREGA
```

| Tabela | Enum / observação |
|---|---|
| `cliente` | `tipo_pessoa`: `PF` \| `PJ` |
| `endereco` | `tipo_endereco`: `ORIGEM` \| `DESTINO` \| `CADASTRO` |
| `produto` | preço, peso, volume, valor declarado, estoque |
| `entrega` | `status_entrega`: `PENDENTE` → `EM_TRANSITO` → `ENTREGUE` \| `CANCELADA` \| `NAO_REALIZADA` |
| `entrega_produto` | itens da entrega, snapshot de peso/volume/valor unitário |
| `historico_entrega` | append-only (trigger bloqueia `UPDATE`/`DELETE`) |



---

## 🏗️ Arquitetura de pacotes

```
br.com.tartarugacometa/
├── cadastro/
│   ├── cliente/    Cliente, ClienteDAO, ClienteBO, ClienteControlador
│   ├── endereco/   Endereco, EnderecoDAO, EnderecoBO, EnderecoControlador
│   └── produto/    Produto, ProdutoDAO, ProdutoBO, ProdutoControlador
├── entrega/
│   ├── Entrega, EntregaDAO, EntregaBO, EntregaControlador
│   ├── item/       ItemEntrega, ItemEntregaDAO
│   └── historico/  HistoricoEntrega, HistoricoEntregaDAO
├── enums/          TipoPessoa, TipoEndereco, StatusEntrega
├── exception/      NegocioException e subclasses
├── config/         DatabaseConfig (HikariCP), ApplicationListener
├── filter/         EncodingFilter
└── util/           Mapper, Validator, geradores e validadores (CPF/CNPJ/CEP/UF)
```

---

## 🚀 Como rodar

### Opção recomendada — Docker

```bash
git clone https://github.com/luizfxdev/tartaruga-cometa.git
cd tartaruga-cometa
chmod +x start.sh
./start.sh
```

O `start.sh`:
1. Verifica e libera conflito de porta (8080 do app, 5433 do banco) automaticamente
2. Sobe `docker compose` (Postgres 15 + WAR em Tomcat 10.1)
3. Aguarda os healthchecks do banco e da aplicação
4. Publica em `http://localhost:8080/tartaruga-cometa/`

Na primeira subida, o Postgres do container roda os scripts de `src/main/resources/db/` automaticamente (schema + dados de exemplo). Para recriar o banco do zero: `docker compose down && docker volume rm tartaruga-cometa_pgdata && ./start.sh`.

O Postgres do Docker também fica acessível para ferramentas externas (pgAdmin etc.) em `localhost:5433` (usuário/senha/banco definidos em `docker-compose.yml`).

### Login

Tela de login em `/login.jsp` (credenciais fixas de demonstração no `login.js`: `admin` / `admin123`) — **não é autenticação real**, apenas controla acesso visual à área interna para demonstração de login.


---

## 🧪 Testes

```bash
./gradlew test              # unitários (JUnit 5 + Mockito + AssertJ)
./gradlew integrationTest   # integração (Testcontainers — sobe Postgres descartável)
./gradlew check             # os dois acima + relatório Jacoco
```

`integrationTest` exige Docker disponível na máquina (Testcontainers) e não roda em `./gradlew build` padrão sem Docker ativo.

---


## 🔐 Segurança

- `database.properties` versionado **não contém senha** — senha só via variável de ambiente `DB_PASSWORD`.
- Pendências de rotação de credencial e limpeza de histórico do git documentadas em `docs/SEGURANCA-CREDENCIAIS.md`.

---

## 👨‍💻 Autor

**Luiz Felipe de Oliveira**

- 🌐 GitHub: [@luizfxdev](https://github.com/luizfxdev)
- 💼 LinkedIn: [in/luizfxdev](https://www.linkedin.com/in/luizfxdev)
- 🌍 Portfólio: [luizfxdev.com.br](https://luizfxdev.com.br)

---

## 🙏 Agradecimentos

> Projeto desenvolvido como parte da **Trilha de Aceleração** proposta pela **GW Sistemas**.

---

<div align="center">

### 🐢 Tartaruga Cometa

**Posso parecer lenta, mas entrego com precisão em cada rota.**

[⬆ Voltar ao topo](#-tartaruga-cometa---sistema-de-gerenciamento-de-entregas)

</div>



