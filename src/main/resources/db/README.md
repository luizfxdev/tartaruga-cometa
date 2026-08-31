# Banco de Dados - Tartaruga Cometa

Scripts SQL modulares para criação do schema PostgreSQL.

## Execução

Para recriar o banco do zero:

```bash
psql -U tartaruga_user -d tartaruga_cometa_db -v ON_ERROR_STOP=1 \
  -f db/00-banco-e-tipos.sql \
  -f db/01-cliente.sql \
  -f db/02-endereco.sql \
  -f db/03-produto.sql \
  -f db/04-entrega.sql \
  -f db/05-entrega-produto.sql \
  -f db/06-historico-entrega.sql \
  -f db/07-views.sql \
  -f db/08-funcoes-triggers.sql \
  -f db/09-indices.sql
```

Ou com dados de desenvolvimento:

```bash
psql -U tartaruga_user -d tartaruga_cometa_db -v ON_ERROR_STOP=1 \
  -f db/00-banco-e-tipos.sql \
  -f db/01-cliente.sql \
  -f db/02-endereco.sql \
  -f db/03-produto.sql \
  -f db/04-entrega.sql \
  -f db/05-entrega-produto.sql \
  -f db/06-historico-entrega.sql \
  -f db/07-views.sql \
  -f db/08-funcoes-triggers.sql \
  -f db/09-indices.sql \
  -f db/99-seed-dev.sql
```

## Estrutura dos Scripts

- **00-banco-e-tipos.sql** — Tipos ENUM (tipo_pessoa, status_entrega)
- **01-cliente.sql** — Tabela cliente com constraints e índices
- **02-endereco.sql** — Tabela endereco com FK para cliente
- **03-produto.sql** — Tabela produto
- **04-entrega.sql** — Tabela entrega com múltiplas FKs e CHECKs de cronologia
- **05-entrega-produto.sql** — Tabela de associação entrega-produto (ItemEntrega)
- **06-historico-entrega.sql** — Tabela de histórico de entrega (append-only)
- **07-views.sql** — Views vw_entregas_completas, vw_estatisticas_entregas
- **08-funcoes-triggers.sql** — Triggers de updated_at e bloqueio de alteração do histórico. O trigger `trigger_mudanca_status` já existe manualmente no banco de produção e não está recriado aqui.
- **09-indices.sql** — Índices críticos para busca e listagem

## Convenção de Nomes

- Chaves primárias: `pk_<tabela>`
- Chaves estrangeiras: `fk_<tabela>_<referencia>`
- Unique: `uq_<tabela>_<coluna>`
- Check: `chk_<tabela>_<regra>`
- Índices: `idx_<tabela>_<colunas>`
- Triggers: `trg_<acao>_<tabela>`

## Política de Migrações

Futuras alterações ao schema devem:

1. Criar script numerado em `migration/`
2. Testar em banco de desenvolvimento
3. Documentar mudança em relatório de release

A pasta `legacy/schema.sql` preserva o baseline original.
