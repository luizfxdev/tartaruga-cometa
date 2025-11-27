# 📡 API Endpoints - Tartaruga Cometa

## Índice

1. [Clientes](#clientes)
2. [Endereços](#endereços)
3. [Produtos](#produtos)
4. [Entregas](#entregas)

---

## Clientes

### Listar Todos os Clientes

**GET** `/clients/`

Retorna lista de todos os clientes cadastrados.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/clients/
```

**Response:** `200 OK`

---

### Buscar Cliente por Nome

**GET** `/clients/search?q={searchTerm}`

Busca clientes pelo nome.

```bash
curl -X GET "http://localhost:8080/tartaruga-cometa/clients/search?q=João"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| q | string | Sim | Termo de busca |

**Response:** `200 OK`

---

### Visualizar Cliente

**GET** `/clients/view/{id}`

Exibe detalhes de um cliente específico.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/clients/view/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID do cliente |

**Response:** `200 OK`

---

### Formulário Novo Cliente

**GET** `/clients/new`

Exibe formulário para criar novo cliente.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/clients/new
```

**Response:** `200 OK`

---

### Formulário Editar Cliente

**GET** `/clients/edit/{id}`

Exibe formulário para editar cliente existente.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/clients/edit/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID do cliente |

**Response:** `200 OK`

---

### Criar/Atualizar Cliente

**POST** `/clients/save`

Cria novo cliente ou atualiza existente.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/clients/save \
  -d "personType=FISICA" \
  -d "document=12345678901" \
  -d "name=João Silva" \
  -d "email=joao@example.com" \
  -d "phone=11999999999"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Não | ID (deixar vazio para novo) |
| personType | string | Sim | FISICA ou JURIDICA |
| document | string | Sim | CPF ou CNPJ |
| name | string | Sim | Nome completo |
| email | string | Sim | Email válido |
| phone | string | Sim | Telefone |

**Response:** `302 Found` (Redirect para `/clients/`)

**Erros:**
- `400 Bad Request` - Dados inválidos
- `409 Conflict` - Cliente duplicado

---

### Deletar Cliente

**POST** `/clients/delete/{id}`

Deleta um cliente.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/clients/delete/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID do cliente |

**Response:** `302 Found` (Redirect para `/clients/`)

**Erros:**
- `404 Not Found` - Cliente não existe
- `409 Conflict` - Cliente tem dependências

---

## Endereços

### Listar Endereços

**GET** `/addresses/`

Lista todos os endereços ou de um cliente específico.

```bash
curl -X GET "http://localhost:8080/tartaruga-cometa/addresses/?clientId=1"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| clientId | integer | Não | Filtrar por cliente |

**Response:** `200 OK`

---

### Listar Endereços por Cliente

**GET** `/addresses/client/{clientId}`

Lista todos os endereços de um cliente.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/addresses/client/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| clientId | integer | Sim | ID do cliente |

**Response:** `200 OK`

---

### Formulário Novo Endereço

**GET** `/addresses/new?clientId={clientId}`

Exibe formulário para criar novo endereço.

```bash
curl -X GET "http://localhost:8080/tartaruga-cometa/addresses/new?clientId=1"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| clientId | integer | Sim | ID do cliente |

**Response:** `200 OK`

---

### Formulário Editar Endereço

**GET** `/addresses/edit/{id}`

Exibe formulário para editar endereço existente.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/addresses/edit/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID do endereço |

**Response:** `200 OK`

---

### Criar/Atualizar Endereço

**POST** `/addresses/save`

Cria novo endereço ou atualiza existente.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/addresses/save \
  -d "clientId=1" \
  -d "addressType=RESIDENCIAL" \
  -d "street=Rua das Flores" \
  -d "number=123" \
  -d "complement=Apto 45" \
  -d "neighborhood=Centro" \
  -d "city=São Paulo" \
  -d "state=SP" \
  -d "zipCode=01310100" \
  -d "reference=Próximo ao metrô" \
  -d "isPrincipal=true"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Não | ID (deixar vazio para novo) |
| clientId | integer | Sim | ID do cliente |
| addressType | string | Sim | RESIDENCIAL, COMERCIAL, ENTREGA |
| street | string | Sim | Nome da rua |
| number | string | Sim | Número |
| complement | string | Não | Complemento |
| neighborhood | string | Sim | Bairro |
| city | string | Sim | Cidade |
| state | string | Sim | Estado (2 caracteres) |
| zipCode | string | Sim | CEP |
| reference | string | Não | Ponto de referência |
| isPrincipal | boolean | Não | Marcar como principal |

**Response:** `302 Found` (Redirect para `/addresses/client/{clientId}`)

---

### Deletar Endereço

**POST** `/addresses/delete/{id}`

Deleta um endereço.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/addresses/delete/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID do endereço |

**Response:** `302 Found`

---

### Definir Endereço Principal

**POST** `/addresses/set-principal/{id}`

Define um endereço como principal para um cliente.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/addresses/set-principal/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID do endereço |

**Response:** `302 Found`

---

## Produtos

### Listar Todos os Produtos

**GET** `/products/`

Retorna lista de todos os produtos.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/products/
```

**Response:** `200 OK`

---

### Buscar Produto por Nome

**GET** `/products/search?q={searchTerm}`

Busca produtos pelo nome.

```bash
curl -X GET "http://localhost:8080/tartaruga-cometa/products/search?q=Eletrônico"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| q | string | Sim | Termo de busca |

**Response:** `200 OK`

---

### Visualizar Produto

**GET** `/products/view/{id}`

Exibe detalhes de um produto.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/products/view/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID do produto |

**Response:** `200 OK`

---

### Formulário Novo Produto

**GET** `/products/new`

Exibe formulário para criar novo produto.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/products/new
```

**Response:** `200 OK`

---

### Formulário Editar Produto

**GET** `/products/edit/{id}`

Exibe formulário para editar produto.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/products/edit/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID do produto |

**Response:** `200 OK`

---

### Criar/Atualizar Produto

**POST** `/products/save`

Cria novo produto ou atualiza existente.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/products/save \
  -d "name=Notebook Dell" \
  -d "description=Notebook 15 polegadas" \
  -d "weightKg=2.5" \
  -d "volumeM3=0.015" \
  -d "declaredValue=3500.00" \
  -d "category=ELETRÔNICOS" \
  -d "active=true"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Não | ID (deixar vazio para novo) |
| name | string | Sim | Nome do produto |
| description | string | Sim | Descrição |
| weightKg | decimal | Sim | Peso em kg |
| volumeM3 | decimal | Sim | Volume em m³ |
| declaredValue | decimal | Sim | Valor declarado |
| category | string | Sim | Categoria |
| active | boolean | Não | Ativo/Inativo |

**Response:** `302 Found` (Redirect para `/products/`)

---

### Deletar Produto

**POST** `/products/delete/{id}`

Deleta um produto.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/products/delete/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID do produto |

**Response:** `302 Found`

---

## Entregas

### Listar Entregas

**GET** `/deliveries/`

Lista todas as entregas ou filtra por status.

```bash
curl -X GET "http://localhost:8080/tartaruga-cometa/deliveries/?status=PENDENTE"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| status | string | Não | PENDENTE, EM_TRANSITO, ENTREGUE, CANCELADA |

**Response:** `200 OK`

---

### Visualizar Entrega

**GET** `/deliveries/view/{id}`

Exibe detalhes de uma entrega com histórico.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/deliveries/view/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID da entrega |

**Response:** `200 OK`

---

### Rastrear Entrega

**GET** `/deliveries/track/{trackingCode}`

Rastreia uma entrega pelo código de rastreamento.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/deliveries/track/TC20251127001
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| trackingCode | string | Sim | Código de rastreamento |

**Response:** `200 OK`

---

### Buscar Entrega

**GET** `/deliveries/search?q={trackingCode}`

Busca entrega pelo código de rastreamento.

```bash
curl -X GET "http://localhost:8080/tartaruga-cometa/deliveries/search?q=TC20251127001"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| q | string | Sim | Código de rastreamento |

**Response:** `200 OK`

---

### Formulário Nova Entrega

**GET** `/deliveries/new`

Exibe formulário para criar nova entrega.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/deliveries/new
```

**Response:** `200 OK`

---

### Formulário Editar Entrega

**GET** `/deliveries/edit/{id}`

Exibe formulário para editar entrega.

```bash
curl -X GET http://localhost:8080/tartaruga-cometa/deliveries/edit/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID da entrega |

**Response:** `200 OK`

---

### Criar/Atualizar Entrega

**POST** `/deliveries/save`

Cria nova entrega ou atualiza existente.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/deliveries/save \
  -d "trackingCode=TC20251127001" \
  -d "shipperId=1" \
  -d "recipientId=2" \
  -d "originAddressId=1" \
  -d "destinationAddressId=2" \
  -d "status=PENDENTE" \
  -d "freightValue=150.00" \
  -d "observations=Frágil - Cuidado"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Não | ID (deixar vazio para novo) |
| trackingCode | string | Sim | Código único de rastreamento |
| shipperId | integer | Sim | ID do cliente remetente |
| recipientId | integer | Sim | ID do cliente destinatário |
| originAddressId | integer | Sim | ID do endereço de origem |
| destinationAddressId | integer | Sim | ID do endereço de destino |
| status | string | Sim | PENDENTE, EM_TRANSITO, ENTREGUE, CANCELADA |
| freightValue | decimal | Sim | Valor do frete |
| observations | string | Não | Observações |

**Response:** `302 Found` (Redirect para `/deliveries/`)

---

### Deletar Entrega

**POST** `/deliveries/delete/{id}`

Deleta uma entrega.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/deliveries/delete/1
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID da entrega |

**Response:** `302 Found`

---

### Atualizar Status da Entrega

**POST** `/deliveries/update-status/{id}`

Atualiza o status de uma entrega.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/deliveries/update-status/1 \
  -d "status=EM_TRANSITO" \
  -d "observations=Saiu para entrega" \
  -d "user=operador1"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID da entrega |
| status | string | Sim | Novo status |
| observations | string | Não | Observações |
| user | string | Não | Usuário que fez a alteração |

**Response:** `302 Found`

---

### Cancelar Entrega

**POST** `/deliveries/cancel/{id}`

Cancela uma entrega.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/deliveries/cancel/1 \
  -d "reason=Cliente solicitou cancelamento" \
  -d "user=operador1"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID da entrega |
| reason | string | Não | Motivo do cancelamento |
| user | string | Não | Usuário que cancelou |

**Response:** `302 Found`

---

### Marcar como Entregue

**POST** `/deliveries/mark-delivered/{id}`

Marca uma entrega como entregue.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/deliveries/mark-delivered/1 \
  -d "user=entregador1"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID da entrega |
| user | string | Não | Usuário que entregou |

**Response:** `302 Found`

---

### Marcar como Não Entregue

**POST** `/deliveries/mark-not-delivered/{id}`

Marca uma entrega como não entregue.

```bash
curl -X POST http://localhost:8080/tartaruga-cometa/deliveries/mark-not-delivered/1 \
  -d "reason=Endereço não encontrado" \
  -d "user=entregador1"
```

**Parâmetros:**
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| id | integer | Sim | ID da entrega |
| reason | string | Não | Motivo da não entrega |
| user | string | Não | Usuário que tentou entregar |

**Response:** `302 Found`

---

## Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 302 | Found - Redirecionamento |
| 400 | Bad Request - Dados inválidos |
| 404 | Not Found - Recurso não encontrado |
| 409 | Conflict - Conflito (duplicação, dependência) |
| 500 | Internal Server Error - Erro no servidor |

---

## Tratamento de Erros

Todos os erros retornam com mensagens em `request.setAttribute("error", mensagem)` e redirecionam para página anterior ou `/error.jsp`.

**Exemplo de Erro:**

```bash
# Cliente não encontrado
HTTP/1.1 404 Not Found
Location: /tartaruga-cometa/clients/

# Cliente com dependências
HTTP/1.1 409 Conflict
Location: /tartaruga-cometa/clients/

# Dados inválidos
HTTP/1.1 400 Bad Request
Location: /tartaruga-cometa/clients/new
```

---

## Observações Importantes

- Todos os endpoints POST realizam redirect após operação
- Encoding UTF-8 aplicado em toda aplicação via `EncodingFilter`
- Sessão gerenciada via cookie `TARTARUGACOMETASESSION`
- Validação de dados realizada no lado do servidor
- Transações de banco garantem consistência dos dados