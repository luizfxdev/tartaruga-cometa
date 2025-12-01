package com.tartarugacometasystem.service;

import com.tartarugacometasystem.dao.DeliveryDAO;
import com.tartarugacometasystem.dao.DeliveryHistoryDAO; // Para buscar histórico
import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryHistory; // Importar DeliveryHistory
import com.tartarugacometasystem.model.DeliveryStatus; // Importar DeliveryStatus
import com.tartarugacometasystem.util.DateFormatter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeliveryService {
    private DeliveryDAO deliveryDAO;
    private ClientService clientService;
    private AddressService addressService;
    private DeliveryHistoryDAO deliveryHistoryDAO; // Novo DAO para histórico

    public DeliveryService() {
        this.deliveryDAO = new DeliveryDAO();
        this.clientService = new ClientService();
        this.addressService = new AddressService();
        this.deliveryHistoryDAO = new DeliveryHistoryDAO(); // Inicializa o DAO de histórico
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
        // Define o status inicial como PENDENTE se não for fornecido
        if (delivery.getStatus() == null) {
            delivery.setStatus(DeliveryStatus.PENDING);
        }
        Delivery savedDelivery = deliveryDAO.save(delivery);

        // Cria o primeiro registro no histórico da entrega
        DeliveryHistory initialHistory = new DeliveryHistory();
        initialHistory.setDeliveryId(savedDelivery.getId());
        initialHistory.setPreviousStatus(null); // Status inicial não tem anterior
        initialHistory.setNewStatus(savedDelivery.getStatus());
        initialHistory.setChangeDate(LocalDateTime.now());
        initialHistory.setObservations("Entrega criada com status " + savedDelivery.getStatus().getLabel());
        initialHistory.setLocation("Sistema"); // Ou a localização atual, se disponível
        deliveryHistoryDAO.save(initialHistory);

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

        // Verifica se o status mudou para registrar no histórico
        if (!existingDelivery.getStatus().equals(delivery.getStatus())) {
            DeliveryHistory historyEntry = new DeliveryHistory();
            historyEntry.setDeliveryId(delivery.getId());
            historyEntry.setPreviousStatus(existingDelivery.getStatus());
            historyEntry.setNewStatus(delivery.getStatus());
            historyEntry.setChangeDate(LocalDateTime.now());
            historyEntry.setObservations("Status alterado de " + existingDelivery.getStatus().getLabel() + " para " + delivery.getStatus().getLabel());
            historyEntry.setLocation("Sistema"); // Ou a localização atual
            deliveryHistoryDAO.save(historyEntry);
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
        // Opcional: Deletar histórico de entrega e produtos associados primeiro
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
     * Valida os campos de uma entrega.
     *
     * @param delivery O objeto Delivery a ser validado.
     * @throws IllegalArgumentException Se algum campo for inválido.
     */
    private void validateDelivery(Delivery delivery) {
        if (delivery.getTrackingCode() == null || delivery.getTrackingCode().trim().isEmpty()) { // Campo renomeado
            throw new IllegalArgumentException("Código de rastreio é obrigatório.");
        }
        if (delivery.getSenderId() == null) { // Campo renomeado
            throw new IllegalArgumentException("Remetente é obrigatório.");
        }
        if (delivery.getRecipientId() == null) { // Campo renomeado
            throw new IllegalArgumentException("Destinatário é obrigatório.");
        }
        if (delivery.getOriginAddressId() == null) { // Campo renomeado
            throw new IllegalArgumentException("Endereço de origem é obrigatório.");
        }
        if (delivery.getDestinationAddressId() == null) { // Campo renomeado
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
        if (delivery.getCreationDate() != null) { // Campo renomeado
            delivery.setFormattedCreationDate(DateFormatter.formatLocalDateTime(delivery.getCreationDate()));
        }
        if (delivery.getUpdatedAt() != null) {
            delivery.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(delivery.getUpdatedAt()));
        }
        if (delivery.getDeliveryDate() != null) { // Campo renomeado
            delivery.setFormattedDeliveryDate(DateFormatter.formatLocalDateTime(delivery.getDeliveryDate()));
        }

        try {
            // Enriquecer com dados do remetente
            if (delivery.getSenderId() != null) { // Campo renomeado
                clientService.getClientById(delivery.getSenderId()).ifPresent(delivery::setSender); // Campo renomeado
            }
            // Enriquecer com dados do destinatário
            if (delivery.getRecipientId() != null) { // Campo renomeado
                clientService.getClientById(delivery.getRecipientId()).ifPresent(delivery::setRecipient); // Campo renomeado
            }
            // Enriquecer com dados do endereço de origem
            if (delivery.getOriginAddressId() != null) { // Campo renomeado
                addressService.getAddressById(delivery.getOriginAddressId()).ifPresent(delivery::setOriginAddress); // Campo renomeado
            }
            // Enriquecer com dados do endereço de destino
            if (delivery.getDestinationAddressId() != null) { // Campo renomeado
                addressService.getAddressById(delivery.getDestinationAddressId()).ifPresent(delivery::setDestinationAddress); // Campo renomeado
            }
            // Enriquecer com histórico da entrega
            List<DeliveryHistory> history = deliveryHistoryDAO.getHistoryByDeliveryId(delivery.getId());
            history.forEach(h -> {
                h.setFormattedChangeDate(DateFormatter.formatLocalDateTime(h.getChangeDate())); // Campo renomeado
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
