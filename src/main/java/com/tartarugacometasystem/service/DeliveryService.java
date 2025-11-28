package com.tartarugacometasystem.service;

import com.tartarugacometasystem.dao.DeliveryDAO;
import com.tartarugacometasystem.dao.DeliveryHistoryDAO;
import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryHistory;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.util.DateFormatter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeliveryService {
    private DeliveryDAO deliveryDAO;
    private DeliveryHistoryDAO deliveryHistoryDAO;
    private ClientService clientService; // Para buscar dados do remetente/destinatário
    private AddressService addressService; // Para buscar dados dos endereços

    public DeliveryService() {
        this.deliveryDAO = new DeliveryDAO();
        this.deliveryHistoryDAO = new DeliveryHistoryDAO();
        this.clientService = new ClientService();
        this.addressService = new AddressService();
    }

    /**
     * Cria uma nova entrega e registra o histórico inicial.
     *
     * @param delivery O objeto Delivery a ser criado.
     * @return O objeto Delivery criado com o ID.
     * @throws SQLException           Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se a entrega for inválida.
     */
    public Delivery createDelivery(Delivery delivery) throws SQLException {
        validateDelivery(delivery);
        delivery.setStatus(DeliveryStatus.PENDENTE); // Define o status inicial
        Delivery savedDelivery = deliveryDAO.save(delivery);

        // Registra o histórico de criação
        DeliveryHistory history = new DeliveryHistory(savedDelivery.getId(), DeliveryStatus.PENDENTE, "Entrega criada", "SYSTEM");
        deliveryHistoryDAO.save(history);

        return savedDelivery;
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
        delivery.ifPresent(this::enrichDelivery); // Enriquecer se presente
        return delivery;
    }

    /**
     * Atualiza uma entrega existente e registra a alteração no histórico.
     *
     * @param delivery O objeto Delivery a ser atualizado.
     * @throws SQLException           Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se a entrega for inválida ou não existir.
     */
    public void updateDelivery(Delivery delivery) throws SQLException {
        if (delivery.getId() == null) {
            throw new IllegalArgumentException("ID da entrega é obrigatório para atualização.");
        }
        validateDelivery(delivery);
        Optional<Delivery> existingOptional = deliveryDAO.findById(delivery.getId());
        if (existingOptional.isEmpty()) {
            throw new IllegalArgumentException("Entrega com ID " + delivery.getId() + " não encontrada.");
        }

        Delivery existingDelivery = existingOptional.get();

        // Se o status mudou, registra no histórico
        if (!existingDelivery.getStatus().equals(delivery.getStatus())) {
            DeliveryHistory history = new DeliveryHistory(
                    delivery.getId(),
                    delivery.getStatus(), // Novo status
                    "Status alterado de " + existingDelivery.getStatus().getLabel() + " para " + delivery.getStatus().getLabel(),
                    "SYSTEM" // Ou o usuário logado
            );
            deliveryHistoryDAO.save(history);
        }

        deliveryDAO.update(delivery);
    }

    /**
     * Deleta uma entrega pelo ID e seu histórico associado.
     *
     * @param id O ID da entrega a ser deletada.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void deleteDelivery(Integer id) throws SQLException {
        // Primeiro, deleta o histórico associado à entrega
        deliveryHistoryDAO.deleteByDeliveryId(id);
        // Depois, deleta a entrega
        deliveryDAO.delete(id);
    }

    /**
     * Marca uma entrega como "ENTREGUE" e registra no histórico.
     *
     * @param id   ID da entrega.
     * @param user Usuário que realizou a ação.
     * @throws SQLException           Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se a entrega não for encontrada ou já estiver entregue/cancelada.
     */
    public void markAsDelivered(Integer id, String user) throws SQLException {
        Optional<Delivery> deliveryOptional = deliveryDAO.findById(id);
        if (deliveryOptional.isEmpty()) {
            throw new IllegalArgumentException("Entrega com ID " + id + " não encontrada.");
        }
        Delivery delivery = deliveryOptional.get();

        if (delivery.getStatus().equals(DeliveryStatus.ENTREGUE) || delivery.getStatus().equals(DeliveryStatus.CANCELADA)) {
            throw new IllegalArgumentException("Entrega já está " + delivery.getStatus().getLabel() + " e não pode ser marcada como entregue.");
        }

        // Atualiza o status e a data de entrega
        deliveryDAO.updateStatus(id, DeliveryStatus.ENTREGUE, "Entrega realizada", LocalDateTime.now(), null);

        // Registra no histórico
        DeliveryHistory history = new DeliveryHistory(id, DeliveryStatus.ENTREGUE, "Entrega realizada", user);
        deliveryHistoryDAO.save(history);
    }

    /**
     * Marca uma entrega como "NÃO REALIZADA" e registra no histórico.
     *
     * @param id     ID da entrega.
     * @param reason Motivo da não realização.
     * @param user   Usuário que realizou a ação.
     * @throws SQLException           Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se a entrega não for encontrada ou já estiver entregue/cancelada.
     */
    public void markAsNotDelivered(Integer id, String reason, String user) throws SQLException {
        Optional<Delivery> deliveryOptional = deliveryDAO.findById(id);
        if (deliveryOptional.isEmpty()) {
            throw new IllegalArgumentException("Entrega com ID " + id + " não encontrada.");
        }
        Delivery delivery = deliveryOptional.get();

        if (delivery.getStatus().equals(DeliveryStatus.ENTREGUE) || delivery.getStatus().equals(DeliveryStatus.CANCELADA)) {
            throw new IllegalArgumentException("Entrega já está " + delivery.getStatus().getLabel() + " e não pode ser marcada como não realizada.");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("O motivo da não realização é obrigatório.");
        }

        // Atualiza o status e o motivo da não entrega
        deliveryDAO.updateStatus(id, DeliveryStatus.NAO_REALIZADA, "Entrega não realizada: " + reason, null, reason);

        // Registra no histórico
        DeliveryHistory history = new DeliveryHistory(id, DeliveryStatus.NAO_REALIZADA, reason, user);
        deliveryHistoryDAO.save(history);
    }

    /**
     * Busca todas as entregas, enriquecendo-as com dados relacionados.
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
     * Busca entregas por termo de busca (código de rastreio, observações, etc.), enriquecendo-as.
     *
     * @param query O termo de busca.
     * @return Uma lista de entregas que correspondem à busca.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> searchDeliveries(String query) throws SQLException {
        List<Delivery> deliveries = deliveryDAO.search(query);
        deliveries.forEach(this::enrichDelivery);
        return deliveries;
    }

    /**
     * Busca entregas por status, enriquecendo-as.
     *
     * @param status O status da entrega.
     * @return Uma lista de entregas com o status especificado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> getDeliveriesByStatus(DeliveryStatus status) throws SQLException {
        List<Delivery> deliveries = deliveryDAO.getDeliveriesByStatus(status);
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
        if (delivery.getShipperId() == null) {
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
        if (delivery.getCreatedAt() != null) {
            delivery.setFormattedCreatedAt(DateFormatter.formatLocalDateTime(delivery.getCreatedAt()));
        }
        if (delivery.getUpdatedAt() != null) {
            delivery.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(delivery.getUpdatedAt()));
        }
        if (delivery.getDeliveredAt() != null) {
            delivery.setFormattedDeliveredAt(DateFormatter.formatLocalDateTime(delivery.getDeliveredAt()));
        }

        try {
            // Enriquecer com dados do remetente
            if (delivery.getShipperId() != null) {
                clientService.getClientById(delivery.getShipperId()).ifPresent(delivery::setShipper);
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
            history.forEach(h -> h.setFormattedCreatedAt(DateFormatter.formatLocalDateTime(h.getCreatedAt())));
            delivery.setHistory(history);

        } catch (SQLException e) {
            System.err.println("Erro ao enriquecer entrega " + delivery.getId() + ": " + e.getMessage());
            // Em um ambiente real, você pode querer logar isso ou lançar uma exceção mais específica
        }
    }
}
