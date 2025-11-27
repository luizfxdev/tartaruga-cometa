# 📋 Requirements - Tartaruga Cometa

## Requisitos Funcionais

### RF1 - Gerenciamento de Clientes
- **RF1.1** O sistema deve permitir cadastrar clientes (Pessoa Física ou Jurídica)
- **RF1.2** O sistema deve validar CPF para Pessoa Física
- **RF1.3** O sistema deve validar CNPJ para Pessoa Jurídica
- **RF1.4** O sistema deve permitir editar dados do cliente
- **RF1.5** O sistema deve permitir deletar cliente (se sem dependências)
- **RF1.6** O sistema deve permitir buscar cliente por nome
- **RF1.7** O sistema deve listar todos os clientes com paginação

### RF2 - Gerenciamento de Endereços
- **RF2.1** O sistema deve permitir cadastrar múltiplos endereços por cliente
- **RF2.2** O sistema deve classificar endereços (Residencial, Comercial, Entrega)
- **RF2.3** O sistema deve permitir marcar um endereço como principal
- **RF2.4** O sistema deve permitir editar endereço
- **RF2.5** O sistema deve permitir deletar endereço
- **RF2.6** O sistema deve validar CEP
- **RF2.7** O sistema deve listar endereços por cliente

### RF3 - Gerenciamento de Produtos
- **RF3.1** O sistema deve permitir cadastrar produtos
- **RF3.2** O sistema deve armazenar peso e volume do produto
- **RF3.3** O sistema deve armazenar valor declarado
- **RF3.4** O sistema deve permitir categorizar produtos
- **RF3.5** O sistema deve permitir ativar/desativar produtos
- **RF3.6** O sistema deve permitir editar produto
- **RF3.7** O sistema deve permitir deletar produto
- **RF3.8** O sistema deve buscar produtos por nome

### RF4 - Gerenciamento de Entregas
- **RF4.1** O sistema deve gerar código de rastreamento único
- **RF4.2** O sistema deve permitir criar entrega com remetente e destinatário
- **RF4.3** O sistema deve permitir definir endereço de origem e destino
- **RF4.4** O sistema deve registrar valor do frete
- **RF4.5** O sistema deve permitir editar entrega (se não entregue)
- **RF4.6** O sistema deve permitir deletar entrega (se pendente)
- **RF4.7** O sistema deve listar entregas com filtro por status

### RF5 - Rastreamento de Entregas
- **RF5.1** O sistema deve permitir rastrear entrega por código
- **RF5.2** O sistema deve exibir histórico de status
- **RF5.3** O sistema deve registrar data/hora de cada mudança de status
- **RF5.4** O sistema deve registrar usuário que fez a alteração
- **RF5.5** O sistema deve permitir adicionar observações

### RF6 - Gerenciamento de Status
- **RF6.1** O sistema deve suportar status: PENDENTE, EM_TRANSITO, ENTREGUE, CANCELADA
- **RF6.2** O sistema deve permitir atualizar status manualmente
- **RF6.3** O sistema deve permitir marcar como entregue
- **RF6.4** O sistema deve permitir marcar como não entregue
- **RF6.5** O sistema deve permitir cancelar entrega com motivo
- **RF6.6** O sistema deve manter histórico de todas as mudanças

---

## Requisitos Não-Funcionais

### RNF1 - Performance
- **RNF1.1** Tempo de resposta < 2 segundos para listagens
- **RNF1.2** Tempo de resposta < 500ms para buscas
- **RNF1.3** Pool de conexões com mínimo 5 e máximo 20 conexões
- **RNF1.4** Índices em campos de busca frequente

### RNF2 - Segurança
- **RNF2.1** Todas as páginas devem usar HTTPS em produção
- **RNF2.2** Sessões com timeout de 30 minutos
- **RNF2.3** Cookies com flag HttpOnly
- **RNF2.4** Validação de entrada em todos os formulários
- **RNF2.5** Proteção contra SQL Injection (Prepared Statements)

### RNF3 - Confiabilidade
- **RNF3.1** Backup automático do banco de dados
- **RNF3.2** Logs de todas as operações críticas
- **RNF3.3** Tratamento de exceções em todas as operações
- **RNF3.4** Recuperação automática de conexões perdidas

### RNF4 - Usabilidade
- **RNF4.1** Interface responsiva e intuitiva
- **RNF4.2** Mensagens de erro claras e em português
- **RNF4.3** Confirmação antes de deletar dados
- **RNF4.4** Feedback visual de operações em progresso

### RNF5 - Compatibilidade
- **RNF5.1** Compatível com Java 8+
- **RNF5.2** Compatível com PostgreSQL 12+
- **RNF5.3** Compatível com Apache Tomcat 9+
- **RNF5.4** Suporte a navegadores modernos (Chrome, Firefox, Safari, Edge)

### RNF6 - Manutenibilidade
- **RNF6.1** Código bem documentado
- **RNF6.2** Padrão MVC implementado
- **RNF6.3** Separação clara de responsabilidades
- **RNF6.4** Testes unitários para lógica de negócio

### RNF7 - Escalabilidade
- **RNF7.1** Arquitetura preparada para múltiplas instâncias
- **RNF7.2** Banco de dados normalizado para crescimento
- **RNF7.3** Índices otimizados para grandes volumes

---

## Critérios de Aceitação

### CA1 - Cadastro de Cliente
- [ ] Deve aceitar Pessoa Física com CPF válido
- [ ] Deve aceitar Pessoa Jurídica com CNPJ válido
- [ ] Deve rejeitar CPF/CNPJ duplicados
- [ ] Deve validar email
- [ ] Deve validar telefone
- [ ] Deve exibir mensagem de sucesso

### CA2 - Cadastro de Endereço
- [ ] Deve permitir múltiplos endereços por cliente
- [ ] Deve validar CEP
- [ ] Deve permitir marcar como principal
- [ ] Deve remover principal anterior ao marcar novo
- [ ] Deve exibir endereço na lista

### CA3 - Criação de Entrega
- [ ] Deve gerar código único
- [ ] Deve validar remetente e destinatário diferentes
- [ ] Deve validar endereços válidos
- [ ] Deve registrar data/hora de criação
- [ ] Deve iniciar com status PENDENTE

### CA4 - Rastreamento
- [ ] Deve encontrar por código exato
- [ ] Deve exibir histórico completo
- [ ] Deve mostrar data/hora de cada evento
- [ ] Deve mostrar observações

---

## Regras de Negócio

### RN1 - Clientes
- **RN1.1** CPF deve conter exatamente 11 dígitos numéricos
- **RN1.2** CNPJ deve conter exatamente 14 dígitos numéricos
- **RN1.3** Documento (CPF/CNPJ) deve ser único no sistema
- **RN1.4** Email deve ser válido (formato padrão)
- **RN1.5** Telefone deve conter pelo menos 10 dígitos
- **RN1.6** Cliente não pode ser deletado se tiver entregas associadas

### RN2 - Endereços
- **RN2.1** Cliente pode ter múltiplos endereços
- **RN2.2** Cliente deve ter no máximo um endereço principal
- **RN2.3** CEP deve conter 8 dígitos numéricos
- **RN2.4** Estado deve ser uma UF válida (2 caracteres)
- **RN2.5** Ao marcar endereço como principal, outros devem ser desmarcados automaticamente
- **RN2.6** Endereço não pode ser deletado se usado em entregas ativas

### RN3 - Produtos
- **RN3.1** Peso deve ser maior que zero
- **RN3.2** Volume deve ser maior que zero
- **RN3.3** Valor declarado deve ser maior ou igual a zero
- **RN3.4** Nome do produto deve ser único
- **RN3.5** Produtos inativos não aparecem em novas entregas

### RN4 - Entregas
- **RN4.1** Código de rastreamento deve ser único e gerado automaticamente
- **RN4.2** Remetente e destinatário devem ser diferentes
- **RN4.3** Endereço de origem deve pertencer ao remetente
- **RN4.4** Endereço de destino deve pertencer ao destinatário
- **RN4.5** Valor do frete deve ser maior que zero
- **RN4.6** Entrega só pode ser editada se status for PENDENTE
- **RN4.7** Entrega só pode ser deletada se status for PENDENTE

### RN5 - Status de Entregas
- **RN5.1** Entrega inicia sempre com status PENDENTE
- **RN5.2** Transições válidas de status:
  - PENDENTE → EM_TRANSITO
  - PENDENTE → CANCELADA
  - EM_TRANSITO → ENTREGUE
  - EM_TRANSITO → CANCELADA
- **RN5.3** Status ENTREGUE é final (não permite mudanças)
- **RN5.4** Status CANCELADA é final (não permite mudanças)
- **RN5.5** Toda mudança de status deve ser registrada no histórico
- **RN5.6** Data/hora de mudança deve ser registrada automaticamente

### RN6 - Histórico
- **RN6.1** Cada mudança de status gera um registro no histórico
- **RN6.2** Histórico deve incluir usuário responsável pela mudança
- **RN6.3** Histórico deve permitir observações opcionais
- **RN6.4** Histórico não pode ser editado ou deletado
- **RN6.5** Histórico deve ser ordenado por data/hora decrescente

---

## Fluxos de Validação

### Validação de CPF
```
1. Remover caracteres não numéricos
2. Verificar se contém 11 dígitos
3. Verificar se todos os dígitos são iguais (CPF inválido)
4. Calcular primeiro dígito verificador
5. Calcular segundo dígito verificador
6. Comparar com os dígitos informados
```

### Validação de CNPJ
```
1. Remover caracteres não numéricos
2. Verificar se contém 14 dígitos
3. Calcular primeiro dígito verificador
4. Calcular segundo dígito verificador
5. Comparar com os dígitos informados
```

### Validação de CEP
```
1. Remover caracteres não numéricos
2. Verificar se contém 8 dígitos
3. Aceitar formato 12345-678 ou 12345678
```

### Validação de Email
```
1. Verificar presença de @
2. Verificar domínio após @
3. Verificar formato válido (regex)
4. Aceitar caracteres válidos antes e depois do @
```

---

## Restrições e Limitações

### Restrições Técnicas
- **RT1** Sistema deve rodar em Java 8 ou superior
- **RT2** Banco de dados PostgreSQL 12 ou superior
- **RT3** Servidor de aplicação Tomcat 9 ou superior
- **RT4** Pool de conexões limitado a 20 conexões simultâneas
- **RT5** Sessões limitadas a 30 minutos de inatividade

### Restrições de Negócio
- **RB1** Apenas usuários autenticados podem acessar o sistema
- **RB2** Clientes não podem ser remetente e destinatário da mesma entrega
- **RB3** Entregas entregues ou canceladas não podem ser modificadas
- **RB4** Códigos de rastreamento seguem padrão TC + AAAAMMDD + sequencial

### Limitações Conhecidas
- **LIM1** Sistema não possui autenticação de usuários (implementação futura)
- **LIM2** Sistema não envia notificações por email/SMS
- **LIM3** Sistema não possui relatórios gerenciais
- **LIM4** Sistema não possui API REST pública
- **LIM5** Sistema não possui integração com transportadoras

---

## Priorização de Requisitos

### Prioridade Alta (Must Have)
- RF1 - Gerenciamento de Clientes
- RF2 - Gerenciamento de Endereços
- RF4 - Gerenciamento de Entregas
- RF5 - Rastreamento de Entregas
- RF6 - Gerenciamento de Status
- RNF2 - Segurança
- RNF3 - Confiabilidade

### Prioridade Média (Should Have)
- RF3 - Gerenciamento de Produtos
- RNF1 - Performance
- RNF4 - Usabilidade
- RNF6 - Manutenibilidade

### Prioridade Baixa (Could Have)
- RNF7 - Escalabilidade
- Relatórios gerenciais
- Notificações automáticas
- Integração com transportadoras

### Não Implementado (Won't Have)
- Autenticação e autorização de usuários
- API REST pública
- Aplicativo mobile
- Sistema de pagamentos online

---

## Glossário

| Termo | Definição |
|-------|-----------|
| **CPF** | Cadastro de Pessoa Física - documento de identificação brasileiro |
| **CNPJ** | Cadastro Nacional de Pessoa Jurídica - documento de empresas brasileiras |
| **CEP** | Código de Endereçamento Postal - código postal brasileiro |
| **Remetente** | Cliente que envia a entrega |
| **Destinatário** | Cliente que recebe a entrega |
| **Código de Rastreamento** | Identificador único de cada entrega (formato: TC + AAAAMMDD + sequencial) |
| **Status** | Estado atual da entrega (PENDENTE, EM_TRANSITO, ENTREGUE, CANCELADA) |
| **Endereço Principal** | Endereço padrão do cliente, usado como sugestão em entregas |
| **Frete** | Valor cobrado pelo serviço de entrega |
| **Observações** | Notas adicionais sobre entregas ou mudanças de status |

---

## Referências

- [Padrão MVC](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
- [Jakarta EE Specification](https://jakarta.ee/specifications/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Apache Tomcat Documentation](https://tomcat.apache.org/)
- [HikariCP Connection Pool](https://github.com/brettwooldridge/HikariCP)

---

**Projeto:** Sistema de Gerenciamento de Entregas - Tartaruga Cometa  
**Versão:** 1.0  
**Data:** 27 de Novembro de 2025  
**Desenvolvido como:** Trilha de Aceleração - GW Sistemas