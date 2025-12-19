# 🐢 Tartaruga Cometa - Sistema de Gerenciamento de Entregas

![Java](https://img.shields.io/badge/Java-11-orange?style=flat-square&logo=java)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-9-blue?style=flat-square)
![Tomcat](https://img.shields.io/badge/Tomcat-10-yellow?style=flat-square&logo=apache-tomcat)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?style=flat-square&logo=postgresql)
![Gradle](https://img.shields.io/badge/Gradle-8.0+-green?style=flat-square&logo=gradle)
![Version](https://img.shields.io/badge/Version-2.0-success?style=flat-square)
![Status](https://img.shields.io/badge/Status-Ativo-success?style=flat-square)

## 📋 Descrição do Projeto

Sistema web completo para gerenciamento e rastreamento de entregas de uma transportadora. Desenvolvido com Jakarta EE, Servlet API e PostgreSQL, com frontend moderno e responsivo utilizando arquitetura CSS modular e tema claro/escuro.

**Versão 2.0** - Focada em melhorias de UI/UX, padronização visual e experiência do usuário.

---

## 🎯 Funcionalidades Principais

### Gerenciamento Completo
✅ **Clientes** - Cadastro de Pessoa Física e Jurídica  
✅ **Endereços** - Múltiplos endereços por cliente com tipo e principal  
✅ **Produtos** - Catálogo com peso, volume e valor declarado  
✅ **Entregas** - Sistema completo de rastreamento  
✅ **Histórico** - Tracking de status de cada entrega  

### Interface e Usabilidade
✅ **Design Moderno** - Interface limpa e profissional  
✅ **Tema Claro/Escuro** - Alternância de temas (em desenvolvimento)  
✅ **Responsivo** - Adaptável para desktop, tablet e mobile  
✅ **Busca Avançada** - Filtros em todas as listagens  
✅ **Feedback Visual** - Alerts, badges e estados de hover  

### Segurança e Performance
✅ **Autenticação de Sessão**  
✅ **Encoding UTF-8** em toda aplicação  
✅ **Connection Pool** - HikariCP para otimização  
✅ **Validação de Dados** - Client-side e Server-side  


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

## 🆕 Novidades da Versão 2.0

### 🎨 Refinamento Visual Completo

#### Tabelas Legíveis e Organizadas
- Grid system moderno com breakpoints responsivos
- Hierarquia visual clara com cores e espaçamentos otimizados
- Hover states e feedback interativo
- Badges coloridos para status e tipos

#### Padronização de Componentes
- Botões unificados (`.custom-btn`) em todo o sistema
- Cards compactos para visualização de detalhes
- Formulários consistentes com validação visual
- Breadcrumb navigation em todas as páginas de listagem

#### Arquitetura CSS Modular
```
css/
├── base/          # Reset, variáveis, tipografia, alerts
├── components/    # Botões, cards, forms, tables, modals
├── layout/        # Header, footer, grid, breadcrumb
├── pages/         # Home, login, dashboard, details
├── themes/        # Light/Dark (em desenvolvimento)
└── main.css       # Import central
```

### 📱 Melhorias de UX

- **Navigation Menu** - Menu hexagonal na página inicial
- **Stats Dashboard** - Métricas em tempo real (∞ Entregas, 100% Satisfação, 150+ Rotas)
- **Details View** - Layout em grid compacto para informações
- **Form Actions** - Botões de ação sempre visíveis
- **Search Bars** - Busca em tempo real em todas as listagens

---

## 🚀 Instalação Rápida

### Pré-requisitos

- ✅ Java 11+ instalado
- ✅ PostgreSQL 15+ instalado e rodando
- ✅ Apache Tomcat 10+ instalado
- ✅ Git instalado

### Instalação Automática (Recomendado)

```bash
# 1. Clonar o repositório
git clone https://github.com/luizfxdev/tartaruga-cometa.git
cd tartaruga-cometa

# 2. Configurar variáveis (edite se necessário)
nano src/main/java/com/tartarugacometasystem/util/ConnectionPool.java

# 3. Executar script de inicialização
chmod +x start.sh
./start.sh
```

O script `start.sh` irá:
1. ✅ Criar o banco de dados PostgreSQL
2. ✅ Executar o schema SQL
3. ✅ Compilar a aplicação com Gradle
4. ✅ Gerar o arquivo WAR
5. ✅ Fazer o deploy no Tomcat
6. ✅ Iniciar o servidor

---

## 🌐 Acesso à Aplicação

**URL Principal:** http://localhost:8080/tartaruga-cometa/

### Páginas Principais

| Módulo | URL | Descrição |
|--------|-----|-----------|
| 🏠 **Home** | `/` | Dashboard com menu hexagonal |
| 👥 **Clientes** | `/clients/` | Gerenciamento de clientes |
| 📍 **Endereços** | `/addresses/` | Cadastro de endereços |
| 📦 **Produtos** | `/products/` | Catálogo de produtos |
| 🚚 **Entregas** | `/deliveries/` | Rastreamento de entregas |

### Funcionalidades em Desenvolvimento

🔄 **Em andamento:**
- ⏳ Theme Toggle (Claro/Escuro)
- ⏳ Menu "Sobre"
- ⏳ Menu "Rastrear"
- ⏳ Menu "Serviços"
- ⏳ Menu "Unidades"
- ⏳ Menu "Cotação"
- ⏳ Menu "Contato"

---

## ✅ Changelog v2.0

### 🎨 Melhorias de UI/UX

#### Tabelas
- ✅ Grid system responsivo
- ✅ Hover states com feedback visual
- ✅ Badges coloridos para status
- ✅ Alinhamento otimizado

#### Botões
- ✅ Padronização `.custom-btn`
- ✅ Estados hover/active/disabled
- ✅ Ícones integrados

#### Formulários
- ✅ Layout em grid (.form-row)
- ✅ Validação visual
- ✅ Placeholders informativos
- ✅ Labels consistentes

#### Detalhes (View)
- ✅ Cards compactos
- ✅ Grid layout 2 colunas
- ✅ Hierarquia visual clara
- ✅ Links para entidades relacionadas

### 📐 Arquitetura

- ✅ CSS modular (8 categorias)
- ✅ Breadcrumb componentizado
- ✅ Header/Footer tags
- ✅ Main.css centralizado

### 🐛 Correções

- ✅ EL expression errors
- ✅ Theme toggle funcional
- ✅ Logo switching
- ✅ Posicionamento hexágonos (4 cima, 3 baixo)
- ✅ Encoding UTF-8

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

## 👨‍💻 Autor

**Luiz Felipe de Oliveira**

[![GitHub](https://img.shields.io/badge/GitHub-luizfxdev-181717?style=for-the-badge&logo=github)](https://github.com/luizfxdev)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-luizfxdev-0A66C2?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/in/luizfxdev)
[![Portfolio](https://img.shields.io/badge/Portfolio-luizfxdev.com.br-FF6B6B?style=for-the-badge&logo=google-chrome&logoColor=white)](https://luizfxdev.com.br)

---


<div align="center">

### 🐢 Tartaruga Cometa

**Posso parecer lenta, mas entrego com precisão em cada rota.**

[![Version](https://img.shields.io/badge/version-2.0-green)](https://github.com/luizfxdev/tartaruga-cometa/releases)

[⬆ Voltar ao topo](#-tartaruga-cometa---sistema-de-gerenciamento-de-entregas)

</div>


