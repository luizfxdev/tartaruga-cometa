package com.tartarugacometasystem.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.dao.DeliveryDAO;
import com.tartarugacometasystem.dao.DeliveryHistoryDAO; // Manter este import
import com.tartarugacometasystem.model.Delivery; // REMOVIDO: Unused Import
import com.tartarugacometasystem.model.DeliveryHistory; // REMOVIDO: Unused Import
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.util.DateFormatter;

public class DeliveryService {
    private final DeliveryDAO deliveryDAO;
    private final ClientService clientService;
    private final AddressService addressService;
    private final DeliveryHistoryDAO deliveryHistoryDAO; // Campo final

    public DeliveryService() {
        this.deliveryDAO = new DeliveryDAO();
        this.clientService = new ClientService();
        this.addressService = new AddressService();
        this.deliveryHistoryDAO = new DeliveryHistoryDAO(); // CORRIGIDO: Inicialização do campo final
    }

    /**
     * Cria uma nova entrega.
     *
     * @param delivery O objeto Delivery a ser criado.
     * @return O objeto Delivery criado com o ID.
     * @throws SQLException             Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se a entrega for inválida.
     */
    public Delivery createDelivery(Delivery delivery) throws SQLException {
        validateDelivery(delivery);
        delivery.setStatus(DeliveryStatus.PENDING); // Status inicial
        Delivery createdDelivery = deliveryDAO.save(delivery);

        // Registrar histórico da criação
        DeliveryHistory historyEntry = new DeliveryHistory();
        historyEntry.setDeliveryId(createdDelivery.getId());
        historyEntry.setPreviousStatus(null); // Não havia status anterior
        historyEntry.setNewStatus(DeliveryStatus.PENDING);
        historyEntry.setChangeDate(LocalDateTime.now());
        historyEntry.setLocation("Sistema"); // Ou a localização inicial
        deliveryHistoryDAO.save(historyEntry);

        return createdDelivery;
    }

    /**
     * Busca uma entrega pelo ID.
     *
     * @param id O ID da entrega.
     * @return Um Optional contendo o Delivery se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Delivery> getDeliveryById(Integer id) throws SQLException {
        Optional<Delivery> delivery = deliveryDAO.findById(id);
        delivery.ifPresent(this::enrichDelivery);
        return delivery;
    }

    /**
     * Atualiza uma entrega existente.
     *
     * @param delivery O objeto Delivery a ser atualizado.
     * @throws SQLException             Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se a entrega for inválida ou não existir.
     */
    public void updateDelivery(Delivery delivery) throws SQLException {
        if (delivery.getId() == null) {
            throw new IllegalArgumentException("ID da entrega é obrigatório para atualização.");
        }
        validateDelivery(delivery);

        Optional<Delivery> existingDeliveryOpt = deliveryDAO.findById(delivery.getId());
        if (existingDeliveryOpt.isEmpty()) {
            throw new IllegalArgumentException("Entrega com ID " + delivery.getId() + " não encontrada.");
        }
        Delivery existingDelivery = existingDeliveryOpt.get();

        // Se o status mudou, registrar no histórico
        if (!existingDelivery.getStatus().equals(delivery.getStatus())) {
            DeliveryHistory historyEntry = new DeliveryHistory();
            historyEntry.setDeliveryId(delivery.getId());
            historyEntry.setPreviousStatus(existingDelivery.getStatus());
            historyEntry.setNewStatus(delivery.getStatus());
            historyEntry.setChangeDate(LocalDateTime.now());
            historyEntry.setLocation("Sistema"); // Ou a localização atual
            deliveryHistoryDAO.save(historyEntry);
        }

        deliveryDAO.update(delivery);
    }

    /**
     * Atualiza o status de uma entrega.
     *
     * @param deliveryId O ID da entrega.
     * @param newStatus O novo status da entrega.
     * @param reason O motivo da mudança de status (opcional, para status como NOT_PERFORMED).
     * @param user O usuário que realizou a mudança (para registro no histórico).
     * @throws SQLException Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se a entrega não for encontrada ou o status for inválido.
     */
    public void updateDeliveryStatus(Integer deliveryId, DeliveryStatus newStatus, String reason, String user) throws SQLException {
        Optional<Delivery> deliveryOpt = deliveryDAO.findById(deliveryId);
        if (deliveryOpt.isEmpty()) {
            throw new IllegalArgumentException("Entrega com ID " + deliveryId + " não encontrada.");
        }
        Delivery delivery = deliveryOpt.get();

        DeliveryStatus oldStatus = delivery.getStatus();
        delivery.setStatus(newStatus);
        delivery.setReasonNotDelivered(reason); // Define o motivo, se houver

        // Se o status mudou, registrar no histórico
        if (!oldStatus.equals(newStatus)) {
            DeliveryHistory historyEntry = new DeliveryHistory();
            historyEntry.setDeliveryId(delivery.getId());
            historyEntry.setPreviousStatus(oldStatus);
            historyEntry.setNewStatus(newStatus);
            historyEntry.setChangeDate(LocalDateTime.now());
            historyEntry.setLocation(user != null && !user.isEmpty() ? user : "Sistema"); // Registrar quem fez a mudança
            deliveryHistoryDAO.save(historyEntry);
        }

        // Se o status for DELIVERED, definir a data de entrega
        if (newStatus == DeliveryStatus.DELIVERED && delivery.getDeliveryDate() == null) {
            delivery.setDeliveryDate(LocalDateTime.now());
        } else if (newStatus != DeliveryStatus.DELIVERED) {
            // Se o status não for DELIVERED, garantir que a data de entrega seja nula
            delivery.setDeliveryDate(null);
        }

        deliveryDAO.update(delivery);
    }

    /**
     * Deleta uma entrega pelo ID.
     *
     * @param id O ID da entrega a ser deletada.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void deleteDelivery(Integer id) throws SQLException {
        // Deletar histórico de entrega primeiro para manter a integridade referencial
        deliveryHistoryDAO.deleteByDeliveryId(id);
        // deliveryProductDAO.deleteByDeliveryId(id); // Se houver um DAO para DeliveryProduct
        deliveryDAO.delete(id);
    }

    /**
     * Busca todas as entregas, enriquecendo-as com dados formatados e objetos relacionados.
     *
     * @return Uma lista de todas as entregas.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> getAllDeliveries() throws SQLException {
        List<Delivery> deliveries = deliveryDAO.getAll();
        deliveries.forEach(this::enrichDelivery);
        return deliveries;
    }

    /**
     * Busca entregas por status, enriquecendo-as com dados formatados e objetos relacionados.
     *
     * @param status O status da entrega.
     * @return Uma lista de entregas com o status especificado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> getDeliveriesByStatus(DeliveryStatus status) throws SQLException {
        List<Delivery> deliveries = deliveryDAO.findByStatus(status);
        deliveries.forEach(this::enrichDelivery);
        return deliveries;
    }

    /**
     * Busca entregas por um termo de busca (código de rastreio, status ou observações).
     *
     * @param searchTerm O termo de busca.
     * @return Uma lista de entregas que correspondem à busca.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> search(String searchTerm) throws SQLException {
        List<Delivery> deliveries = deliveryDAO.search(searchTerm); // CORRIGIDO: Chamada para deliveryDAO.search()
        deliveries.forEach(this::enrichDelivery);
        return deliveries;
    }

    /**
     * Valida os campos de uma entrega.
     *
     * @param delivery O objeto Delivery a ser validado.
     * @throws IllegalArgumentException Se algum campo for inválido.
     */
    private void validateDelivery(Delivery delivery) {
        if (delivery.getTrackingCode() == null || delivery.getTrackingCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Código de rastreio é obrigatório.");
        }
        if (delivery.getSenderId() == null) {
            throw new IllegalArgumentException("Remetente é obrigatório.");
        }
        if (delivery.getRecipientId() == null) {
            throw new IllegalArgumentException("Destinatário é obrigatório.");
        }
        if (delivery.getOriginAddressId() == null) {
            throw new IllegalArgumentException("Endereço de origem é obrigatório.");
        }
        if (delivery.getDestinationAddressId() == null) {
            throw new IllegalArgumentException("Endereço de destino é obrigatório.");
        }
        if (delivery.getTotalValue() == null || delivery.getTotalValue().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor total deve ser um número positivo.");
        }
        if (delivery.getFreightValue() == null || delivery.getFreightValue().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor do frete deve ser um número positivo.");
        }
        if (delivery.getTotalWeightKg() == null || delivery.getTotalWeightKg().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Peso total deve ser um número positivo.");
        }
        if (delivery.getTotalVolumeM3() == null || delivery.getTotalVolumeM3().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Volume total deve ser um número positivo.");
        }
        if (delivery.getStatus() == null) {
            throw new IllegalArgumentException("Status da entrega é obrigatório.");
        }
    }

    /**
     * Enriquecer um objeto Delivery com dados formatados e objetos relacionados.
     *
     * @param delivery O objeto Delivery a ser enriquecido.
     */
    private void enrichDelivery(Delivery delivery) {
        if (delivery == null) return;

        // Formata datas
        if (delivery.getCreationDate() != null) {
            delivery.setFormattedCreationDate(DateFormatter.formatLocalDateTime(delivery.getCreationDate()));
        }
        if (delivery.getUpdatedAt() != null) {
            delivery.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(delivery.getUpdatedAt()));
        }
        if (delivery.getDeliveryDate() != null) {
            delivery.setFormattedDeliveryDate(DateFormatter.formatLocalDateTime(delivery.getDeliveryDate()));
        }

        try {
            // Enriquecer com dados do remetente
            if (delivery.getSenderId() != null) {
                clientService.getClientById(delivery.getSenderId()).ifPresent(delivery::setSender);
            }
            // Enriquecer com dados do destinatário
            if (delivery.getRecipientId() != null) {
                clientService.getClientById(delivery.getRecipientId()).ifPresent(delivery::setRecipient);
            }
            // Enriquecer com dados do endereço de origem
            if (delivery.getOriginAddressId() != null) {
                addressService.getAddressById(delivery.getOriginAddressId()).ifPresent(delivery::setOriginAddress);
            }
            // Enriquecer com dados do endereço de destino
            if (delivery.getDestinationAddressId() != null) {
                addressService.getAddressById(delivery.getDestinationAddressId()).ifPresent(delivery::setDestinationAddress);
            }
            // Enriquecer com histórico da entrega
            List<DeliveryHistory> history = deliveryHistoryDAO.getHistoryByDeliveryId(delivery.getId());
            history.forEach(h -> {
                h.setFormattedChangeDate(DateFormatter.formatLocalDateTime(h.getChangeDate()));
                if (h.getPreviousStatus() != null) {
                    h.setFormattedPreviousStatus(h.getPreviousStatus().getLabel());
                }
                if (h.getNewStatus() != null) {
                    h.setFormattedNewStatus(h.getNewStatus().getLabel());
                }
            });
            delivery.setHistory(history);

        } catch (SQLException e) {
            System.err.println("Erro ao enriquecer entrega " + delivery.getId() + ": " + e.getMessage());
            // Em um ambiente real, você pode querer logar isso ou lançar uma exceção mais específica
        }
    }
}
