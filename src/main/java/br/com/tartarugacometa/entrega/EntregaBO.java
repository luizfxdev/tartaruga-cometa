package br.com.tartarugacometa.entrega;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import br.com.tartarugacometa.cadastro.produto.Produto;
import br.com.tartarugacometa.cadastro.produto.ProdutoDAO;
import br.com.tartarugacometa.entrega.historico.HistoricoEntrega;
import br.com.tartarugacometa.entrega.historico.HistoricoEntregaDAO;
import br.com.tartarugacometa.entrega.item.ItemEntrega;
import br.com.tartarugacometa.entrega.item.ItemEntregaDAO;
import br.com.tartarugacometa.enums.StatusEntrega;
import br.com.tartarugacometa.exception.EntregaException;
import br.com.tartarugacometa.util.Conexao;
import br.com.tartarugacometa.util.DateFormatter;
import br.com.tartarugacometa.util.GeradorCodigoRastreio;

public class EntregaBO {
    private static final int MAX_TENTATIVAS_CODIGO = 3;

    private final EntregaDAO entregaDAO;
    private final ItemEntregaDAO itemEntregaDAO;
    private final HistoricoEntregaDAO historicoDAO;
    private final ProdutoDAO produtoDAO;

    public EntregaBO() {
        this.entregaDAO = new EntregaDAO();
        this.itemEntregaDAO = new ItemEntregaDAO();
        this.historicoDAO = new HistoricoEntregaDAO();
        this.produtoDAO = new ProdutoDAO();
    }

    public EntregaBO(EntregaDAO entregaDAO, ItemEntregaDAO itemEntregaDAO, HistoricoEntregaDAO historicoDAO, ProdutoDAO produtoDAO) {
        this.entregaDAO = entregaDAO;
        this.itemEntregaDAO = itemEntregaDAO;
        this.historicoDAO = historicoDAO;
        this.produtoDAO = produtoDAO;
    }

    public Entrega criarComTransacao(Entrega entrega, List<ItemEntrega> itens, String usuario) throws EntregaException {
        entrega.setItens(itens);
        validarCriacao(entrega);
        calcularTotais(entrega);
        gerarCodigoRastreio(entrega);

        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                entrega.setStatus(StatusEntrega.PENDENTE);
                entregaDAO.inserir(conexao, entrega);

                for (ItemEntrega item : itens) {
                    item.setDeliveryId(entrega.getId());
                    itemEntregaDAO.inserir(conexao, item);
                    reservarEstoque(conexao, item.getProductId(), item.getQuantity());
                }

                registrarHistorico(conexao, entrega, null, StatusEntrega.PENDENTE, usuario, null);
                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw new EntregaException("Não foi possível criar a entrega.", e);
            }
        } catch (SQLException e) {
            throw new EntregaException("Falha de conexão com o banco.", e);
        }
        return entrega;
    }

    public void transicionarStatus(Integer id, StatusEntrega novoStatus, String usuario, String observacao) throws EntregaException {
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                Optional<Entrega> entregaOpt = entregaDAO.buscarPorId(conexao, id);
                if (entregaOpt.isEmpty()) {
                    throw new EntregaException("Entrega não encontrada.");
                }

                Entrega entrega = entregaOpt.get();
                StatusEntrega statusAnterior = entrega.getStatus();

                validarTransicao(statusAnterior, novoStatus);

                if (novoStatus == StatusEntrega.CANCELADA) {
                    validarCancelamento(observacao);
                    entrega.setReasonNotDelivered(observacao);
                }

                if (novoStatus == StatusEntrega.ENTREGUE) {
                    entrega.setDeliveryDate(LocalDateTime.now(ZoneOffset.UTC));
                }

                entrega.setStatus(novoStatus);
                entregaDAO.atualizar(conexao, entrega);
                registrarHistorico(conexao, entrega, statusAnterior, novoStatus, usuario, observacao);

                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw new EntregaException("Não foi possível transicionar status.", e);
            }
        } catch (SQLException e) {
            throw new EntregaException("Falha de conexão com o banco.", e);
        }
    }

    public void atualizarStatusComTransacao(Integer id, StatusEntrega novoStatus, String motivo) throws EntregaException {
        transicionarStatus(id, novoStatus, "SISTEMA", motivo);
    }

    public Entrega createDelivery(Entrega delivery) throws SQLException {
        try {
            return criarComTransacao(delivery, delivery.getItens() != null ? delivery.getItens() : List.of(), "SISTEMA");
        } catch (EntregaException e) {
            throw new SQLException(e);
        }
    }

    public Optional<Entrega> getDeliveryById(Integer id) throws SQLException {
        try (Connection conexao = Conexao.abrir()) {
            Optional<Entrega> entrega = entregaDAO.buscarPorId(conexao, id);
            if (entrega.isPresent()) {
                enriquecer(entrega.get());
            }
            return entrega;
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateDelivery(Entrega delivery) throws SQLException {
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                entregaDAO.atualizar(conexao, delivery);
                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw e;
            }
        }
    }

    public void deleteDelivery(Integer id) throws SQLException {
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                itemEntregaDAO.excluirPorEntregaId(conexao, id);
                entregaDAO.excluir(conexao, id);
                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw e;
            }
        }
    }

    public List<Entrega> getAllDeliveries() throws SQLException {
        try (Connection conexao = Conexao.abrir()) {
            List<Entrega> entregas = entregaDAO.buscarTodos(conexao);
            for (Entrega entrega : entregas) {
                enriquecer(entrega);
            }
            return entregas;
        }
    }

    public List<Entrega> getDeliveriesByStatus(StatusEntrega status) throws SQLException {
        try (Connection conexao = Conexao.abrir()) {
            return entregaDAO.buscarPorStatus(conexao, status);
        }
    }

    public Optional<Entrega> getDeliveryByTrackingCode(String trackingCode) throws SQLException {
        try (Connection conexao = Conexao.abrir()) {
            return entregaDAO.buscarPorCodigoRastreamento(conexao, trackingCode);
        }
    }

    public void updateDeliveryStatus(Integer id, StatusEntrega newStatus, String reasonNotDelivered, String updatedBy) throws SQLException {
        try {
            atualizarStatusComTransacao(id, newStatus, reasonNotDelivered);
        } catch (EntregaException e) {
            throw new SQLException(e);
        }
    }

    public List<Entrega> search(String searchTerm) throws SQLException {
        try (Connection conexao = Conexao.abrir()) {
            return entregaDAO.pesquisar(conexao, searchTerm);
        }
    }

    private void validarCriacao(Entrega entrega) throws EntregaException {
        validarPartes(entrega);
        validarEnderecos(entrega);
        validarItens(entrega);
        validarFrete(entrega);
    }

    private void validarPartes(Entrega entrega) throws EntregaException {
        if (entrega.getSenderId() == null || entrega.getRecipientId() == null) {
            throw new EntregaException("Remetente e destinatário são obrigatórios.");
        }
        if (entrega.getSenderId().equals(entrega.getRecipientId())) {
            throw new EntregaException("Remetente e destinatário devem ser diferentes.");
        }
    }

    private void validarEnderecos(Entrega entrega) throws EntregaException {
        if (entrega.getOriginAddressId() == null || entrega.getDestinationAddressId() == null) {
            throw new EntregaException("Endereço de origem e destino são obrigatórios.");
        }
        if (entrega.getOriginAddressId().equals(entrega.getDestinationAddressId())) {
            throw new EntregaException("Endereço de origem e destino devem ser diferentes.");
        }
    }

    private void validarItens(Entrega entrega) throws EntregaException {
        if (entrega.getItens() == null || entrega.getItens().isEmpty()) {
            throw new EntregaException("A entrega precisa de pelo menos um produto.");
        }
        for (ItemEntrega item : entrega.getItens()) {
            if (item.getQuantity() == null || item.getQuantity() < 1) {
                throw new EntregaException("Quantidade de item deve ser no mínimo 1.");
            }
        }
    }

    private void validarFrete(Entrega entrega) throws EntregaException {
        if (entrega.getFreightValue() != null) {
            if (entrega.getFreightValue().compareTo(BigDecimal.ZERO) < 0) {
                throw new EntregaException("Valor de frete não pode ser negativo.");
            }
        }
    }

    private void validarTransicao(StatusEntrega atual, StatusEntrega novo) throws EntregaException {
        if (!atual.podeTransicionarPara(novo)) {
            throw new EntregaException("Não é possível mudar a entrega de " + atual.getRotulo() + " para " + novo.getRotulo() + ".");
        }
    }

    private void validarCancelamento(String motivo) throws EntregaException {
        if (motivo == null || motivo.trim().length() < 10) {
            throw new EntregaException("Motivo de cancelamento deve ter no mínimo 10 caracteres.");
        }
    }

    private void calcularTotais(Entrega entrega) throws EntregaException {
        if (entrega.getItens() == null || entrega.getItens().isEmpty()) {
            throw new EntregaException("A entrega precisa de pelo menos um produto.");
        }

        BigDecimal valor = BigDecimal.ZERO;
        BigDecimal peso = BigDecimal.ZERO;
        BigDecimal volume = BigDecimal.ZERO;

        for (ItemEntrega item : entrega.getItens()) {
            BigDecimal qtd = BigDecimal.valueOf(item.getQuantity());
            item.setSubtotal(item.getUnitValue().multiply(qtd));
            valor = valor.add(item.getSubtotal());
            peso = peso.add(item.getUnitWeightKg().multiply(qtd));
            volume = volume.add(item.getUnitVolumeM3().multiply(qtd));
        }

        entrega.setTotalValue(valor.setScale(2, RoundingMode.HALF_UP));
        entrega.setTotalWeightKg(peso.setScale(3, RoundingMode.HALF_UP));
        entrega.setTotalVolumeM3(volume.setScale(4, RoundingMode.HALF_UP));
    }

    private void gerarCodigoRastreio(Entrega entrega) throws EntregaException {
        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS_CODIGO; tentativa++) {
            String codigo = GeradorCodigoRastreio.gerar();
            try (Connection conexao = Conexao.abrir()) {
                if (!entregaDAO.existeCodigoRastreio(conexao, codigo)) {
                    entrega.setTrackingCode(codigo);
                    return;
                }
            } catch (SQLException e) {
                throw new EntregaException("Erro ao gerar código de rastreio.", e);
            }
        }
        throw new EntregaException("Não foi possível gerar um código de rastreio único após " + MAX_TENTATIVAS_CODIGO + " tentativas.");
    }

    private void reservarEstoque(Connection conexao, Integer produtoId, Integer quantidade) throws SQLException {
        Optional<Produto> produtoOpt = produtoDAO.buscarPorId(conexao, produtoId);
        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            int novoEstoque = produto.getStockQuantity() - quantidade;
            if (novoEstoque < 0) {
                throw new SQLException("Estoque insuficiente para o produto.");
            }
            produto.setStockQuantity(novoEstoque);
            produtoDAO.atualizar(conexao, produto);
        }
    }

    private void registrarHistorico(Connection conexao, Entrega entrega, StatusEntrega anterior, StatusEntrega novo, String usuario, String observacao) throws SQLException {
        HistoricoEntrega historico = new HistoricoEntrega();
        historico.setDeliveryId(entrega.getId());
        historico.setPreviousStatus(anterior);
        historico.setNewStatus(novo);
        historico.setChangeDate(LocalDateTime.now(ZoneOffset.UTC));
        historico.setUser(usuario);
        historico.setObservations(observacao);
        historicoDAO.inserir(conexao, historico);
    }

    private void enriquecer(Entrega entrega) {
        if (entrega == null) return;

        if (entrega.getCreationDate() != null) {
            entrega.setFormattedCreationDate(DateFormatter.formatLocalDateTime(entrega.getCreationDate()));
        }
        if (entrega.getUpdatedAt() != null) {
            entrega.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(entrega.getUpdatedAt()));
        }
        if (entrega.getDeliveryDate() != null) {
            entrega.setFormattedDeliveryDate(DateFormatter.formatLocalDateTime(entrega.getDeliveryDate()));
        }
    }
}
