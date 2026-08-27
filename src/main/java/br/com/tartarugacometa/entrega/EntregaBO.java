package br.com.tartarugacometa.entrega;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.com.tartarugacometa.entrega.historico.HistoricoEntrega;
import br.com.tartarugacometa.entrega.historico.HistoricoEntregaDAO;
import br.com.tartarugacometa.entrega.item.ItemEntrega;
import br.com.tartarugacometa.entrega.item.ItemEntregaDAO;
import br.com.tartarugacometa.enums.StatusEntrega;
import br.com.tartarugacometa.exception.EntregaException;
import br.com.tartarugacometa.util.Conexao;
import br.com.tartarugacometa.util.DateFormatter;

public class EntregaBO {
    private final EntregaDAO entregaDAO;
    private final ItemEntregaDAO itemEntregaDAO;
    private final HistoricoEntregaDAO historicoDAO;

    public EntregaBO() {
        this.entregaDAO = new EntregaDAO();
        this.itemEntregaDAO = new ItemEntregaDAO();
        this.historicoDAO = new HistoricoEntregaDAO();
    }

    public EntregaBO(EntregaDAO entregaDAO, ItemEntregaDAO itemEntregaDAO, HistoricoEntregaDAO historicoDAO) {
        this.entregaDAO = entregaDAO;
        this.itemEntregaDAO = itemEntregaDAO;
        this.historicoDAO = historicoDAO;
    }

    public Entrega criarComTransacao(Entrega entrega) throws EntregaException {
        validar(entrega);
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                entregaDAO.inserir(conexao, entrega);

                HistoricoEntrega historico = new HistoricoEntrega();
                historico.setDeliveryId(entrega.getId());
                historico.setPreviousStatus(null);
                historico.setNewStatus(StatusEntrega.PENDENTE);
                historico.setChangeDate(LocalDateTime.now());
                historico.setLocation("Sistema");
                historicoDAO.inserir(conexao, historico);

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

    public void atualizarStatusComTransacao(Integer id, StatusEntrega novoStatus, String motivo) throws EntregaException {
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                Optional<Entrega> entregaOpt = entregaDAO.buscarPorId(conexao, id);
                if (entregaOpt.isEmpty()) {
                    throw new EntregaException("Entrega não encontrada.");
                }

                Entrega entrega = entregaOpt.get();
                StatusEntrega statusAnterior = entrega.getStatus();
                entrega.setStatus(novoStatus);
                entrega.setReasonNotDelivered(motivo);
                if (novoStatus == StatusEntrega.ENTREGUE) {
                    entrega.setDeliveryDate(LocalDateTime.now());
                }

                entregaDAO.atualizar(conexao, entrega);

                HistoricoEntrega historico = new HistoricoEntrega();
                historico.setDeliveryId(id);
                historico.setPreviousStatus(statusAnterior);
                historico.setNewStatus(novoStatus);
                historico.setChangeDate(LocalDateTime.now());
                historico.setObservations(motivo);
                historicoDAO.inserir(conexao, historico);

                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw new EntregaException("Não foi possível atualizar o status da entrega.", e);
            }
        } catch (SQLException e) {
            throw new EntregaException("Falha de conexão com o banco.", e);
        }
    }

    public Entrega createDelivery(Entrega delivery) throws SQLException {
        try {
            return criarComTransacao(delivery);
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
                historicoDAO.excluirPorEntregaId(conexao, id);
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

    private void validar(Entrega entrega) throws EntregaException {
        if (entrega.getTrackingCode() == null || entrega.getTrackingCode().trim().isEmpty()) {
            throw new EntregaException("Código de rastreamento é obrigatório.");
        }
        if (entrega.getTotalValue() == null) {
            throw new EntregaException("Valor total é obrigatório.");
        }
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
