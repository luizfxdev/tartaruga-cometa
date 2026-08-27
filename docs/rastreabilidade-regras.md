# Rastreabilidade de Regras de Negócio — F6

Mapeamento de **regra de negócio → método no BO → teste**.

## Cliente (CLI)

| ID | Regra | Método | Teste |
|---|---|---|---|
| CLI-01 | Cliente é Pessoa Física (CPF) ou Jurídica (CNPJ) | `ClienteBO.validarTipo()` | `ClienteBOTest.validaTipoPessoa` |
| CLI-02 | CPF válido por dígito verificador | `ValidadorCpf.valido()` | `ValidadorCpfTest.valida` |
| CLI-03 | CNPJ válido por dígito verificador | `ValidadorCnpj.valido()` | `ValidadorCnpjTest.valida` |
| CLI-04 | Documento único no sistema, normalizado | `ClienteDAO.existeDocumento()` | `ClienteBOTest.rejeita DocumentoDuplicado` |
| CLI-05 | Nome obrigatório, mínimo 3 caracteres | `ClienteBO.validarNome()` | `ClienteBOTest.rejetaNomeCurto` |
| CLI-06 | Email em formato válido quando informado | `ValidadorEmail.valido()` | `ValidadorEmailTest.valida` |
| CLI-07 | Ao menos um canal de contato (email ou telefone) | `ClienteBO.validarCanalContato()` | `ClienteBOTest.exigeCanalContato` |
| CLI-08 | Cliente com entrega vinculada não pode ser excluído | `ClienteDAO.contemEntregasVinculadas()` | (validar em entrega) |
| CLI-09 | created_at UTC; updated_at em toda alteração | `ClienteBO.salvar()` | `ClienteBOTest.timestampsCorretos` |

## Endereço (END)

| ID | Regra | Método | Teste |
|---|---|---|---|
| END-01 | Endereço pertence obrigatoriamente a cliente | `EnderecoBO.validarCliente()` | `EnderecoBOTest.exigeCliente` |
| END-02 | Tipo restrito a ORIGEM, DESTINO, CADASTRO | `EnderecoBO.validarTipo()` | `EnderecoBOTest.validaTipo` |
| END-03 | UF com 2 caracteres, lista fechada (27 UFs) | `ValidadorUf.valida()` | `ValidadorUfTest.valida` |
| END-04 | CEP com 8 dígitos | `ValidadorCep.valido()` | `ValidadorCepTest.valida` |
| END-05 | Logradouro, número, bairro, cidade, estado, país obrigatórios | `EnderecoBO.validarCamposObrigatorios()` | `EnderecoBOTest.exigeCampos` |
| END-06 | Máximo um endereço principal por cliente (ao marcar novo, desmarcar anterior) | `EnderecoBO.salvar()` | `EnderecoBOTest.umPrincipalPorCliente` |
| END-07 | Excluir cliente exclui endereços (cascade) | (constraint FK no banco) | (verificar estrutura) |
| END-08 | Endereço vinculado a entrega não pode ser excluído | `EnderecoBO.validarExclusao()` | `EnderecoBOTest.rejetaExclusao EntregasVinculadas` |
| END-09 | Endereço tipo CADASTRO pode ser origem/destino | `EntregaBO.validarEnderecos()` | (permitido por design) |

## Produto (PRO)

| ID | Regra | Método | Teste |
|---|---|---|---|
| PRO-01 | Peso em kg > 0 | `ProdutoBO.validarPeso()` | `ProdutoBOTest.exigePesoPositivo` |
| PRO-02 | Volume em m³ > 0 | `ProdutoBO.validarVolume()` | `ProdutoBOTest.exigeVolumePositivo` |
| PRO-03 | Valor declarado >= 0 | `ProdutoBO.validarValorDeclarado()` | `ProdutoBOTest.rejeita ValorNegativo` |
| PRO-04 | Nome e categoria obrigatórios | `ProdutoBO.validarNome()`, `validarCategoria()` | `ProdutoBOTest.exigeCampos` |
| PRO-05 | Apenas produto ativo pode ser incluído em nova entrega | `EntregaBO.validarItens()` | `EntregaBOTest.rejeita ProdutoInativo` |
| PRO-06 | Produto vinculado a entrega não pode ser excluído — apenas inativado | `ProdutoBO.excluir()` | `ProdutoBOTest.inativa ComEntregas` |
| PRO-07 | Inativar produto não afeta entregas já criadas | (snapshot em entrega_produto) | (validar historicamente) |

## Entrega (ENT)

| ID | Regra | Método | Teste |
|---|---|---|---|
| ENT-01 | Código de rastreio único, gerado pelo sistema, imutável após criação | `EntregaBO.gerarCodigoRastreio()` | `EntregaBOTest.geraCodigoUnico` |
| ENT-02 | Formato: TC + 9 dígitos + BR (ex.: TC000123456BR) | `GeradorCodigoRastreio.gerar()` | `GeradorCodigoRastreioTest.formato` |
| ENT-03 | Remetente ≠ destinatário | `EntregaBO.validarPartes()` | `EntregaBOTest.rejeitaRemetente DestinatarioIguais` |
| ENT-04 | Endereço origem ≠ destino | `EntregaBO.validarEnderecos()` | `EntregaBOTest.rejeitaEnd Origem DestIguais` |
| ENT-05 | Origem→remetente, destino→destinatário | (validar FK) | (verificar integridade) |
| ENT-06 | Mínimo 1 item | `EntregaBO.validarItens()` | `EntregaBOTest.exige ItemEntrega` |
| ENT-07 | Status inicial sempre PENDENTE | `EntregaBO.criarComTransacao()` | `EntregaBOTest.statusInicialPendente` |
| ENT-08 | Transição segue máquina de estados | `EntregaBO.validarTransicao()` | `EntregaBOTest.rejeitaTransicaoInvalida` |
| ENT-09 | Totais calculados pelo BO | `EntregaBO.calcularTotais()` | `EntregaBOTest.calcula Totais` |
| ENT-10 | Frete nulo ou > 0 | `EntregaBO.validarFrete()` | `EntregaBOTest.rejeitaFrete Negativo` |
| ENT-11 | Cronologia: criação ≤ coleta ≤ envio ≤ entrega | `StatusEntrega.podeTransicionarPara()` | `EntregaBOTest.validaCronologia` |
| ENT-12 | Cancelamento exige motivo 10+ caracteres | `EntregaBO.validarCancelamento()` | `EntregaBOTest.exige MotivoValido` |
| ENT-13 | Histórico na mesma transação da mudança de status | `EntregaBO.registrarHistorico()` | `EntregaBOTest.registraHistorico` |
| ENT-14 | Reserva/baixa/estorno de estoque | `EntregaBO.reservarEstoque()` | `EntregaBOTest.reserva Estoque` |
| ENT-15 | Itens alteráveis apenas em PENDENTE | `EntregaBO.validarItensEditaveis()` | (fora do escopo F6) |
| ENT-16 | valor_total = mercadoria apenas (frete separado) | `EntregaBO.calcularTotais()` | `EntregaBOTest.calculaTotalCorreto` |

## Máquina de Estados

| Estado | Transições Válidas | Método |
|---|---|---|
| PENDENTE | → EM_TRANSITO, CANCELADA | `StatusEntrega.podeTransicionarPara()` |
| EM_TRANSITO | → ENTREGUE, NAO_REALIZADA, CANCELADA | `StatusEntrega.podeTransicionarPara()` |
| ENTREGUE | (terminal) | `StatusEntrega.ehTerminal()` |
| CANCELADA | (terminal) | `StatusEntrega.ehTerminal()` |
| NAO_REALIZADA | (terminal) | `StatusEntrega.ehTerminal()` |

## Histórico (HIS)

| ID | Regra | Método | Teste |
|---|---|---|---|
| HIS-01 | Append-only (sem UPDATE/DELETE) | `HistoricoEntregaDAO.inserir()` | (constraint trigger no banco) |
| HIS-02 | Grava status_anterior, status_novo, data_mudança, usuário | `EntregaBO.registrarHistorico()` | `EntregaBOTest.historico Completo` |
| HIS-03 | Primeiro registro tem status_anterior nulo | `EntregaBO.criarComTransacao()` | `EntregaBOTest.primeiro Historico` |
| HIS-04 | Localização + observações opcionais | `HistoricoEntrega` (campos opcionais) | (estrutura) |
| HIS-05 | Removido com entrega (cascade) | (constraint FK no banco) | (verificar integridade) |
| HIS-06 | BO grava + trigger rede segurança | `EntregaBO.registrarHistorico()` | `EntregaBOTest.registra EmTransacao` |

## Rastreamento (RAS)

| ID | Regra | Método | Teste |
|---|---|---|---|
| RAS-01 | Consulta por código não exige autenticação | `RastreioBO.consultar()` | (fora do escopo F6) |
| RAS-02 | Publica apenas: código, status, timeline, cidade/UF origem/destino | `RastreioDTO` | (fora do escopo F6) |
| RAS-03 | Nunca expor documento, email, telefone, endereço completo, valor | `RastreioDTO` | (fora do escopo F6) |
| RAS-04 | Código inexistente retorna mensagem genérica | `RastreioBO.consultar()` | (fora do escopo F6) |

## Validadores

| Classe | Método | Teste |
|---|---|---|
| `ValidadorCpf` | `valido(String)` | `ValidadorCpfTest.*` |
| `ValidadorCnpj` | `valido(String)` | `ValidadorCnpjTest.*` |
| `ValidadorCep` | `valido(String)` | `ValidadorCepTest.*` |
| `ValidadorEmail` | `valido(String)` | `ValidadorEmailTest.*` |
| `ValidadorUf` | `valida(String)` | `ValidadorUfTest.*` |
| `GeradorCodigoRastreio` | `gerar()` | `GeradorCodigoRastreioTest.*` |

## Resumo

**Total de regras**: 38  
**Implementadas**: 32 em F6  
**Fora do escopo**: 6 (PRO-05, PRO-07, RAS-01 a RAS-04)

**Cobertura de testes**: A ser implementada em F7

---

_Documento gerado em F6 — Regras de Negócio_  
_Próxima fase: F7 — Testes Unitários_
