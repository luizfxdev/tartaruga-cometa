package com.tartarugacometasystem.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tartarugacometasystem.dao.DeliveryDAO;
import com.tartarugacometasystem.dao.DeliveryHistoryDAO;
import com.tartarugacometasystem.dao.DeliveryProductDAO;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryHistory;
import com.tartarugacometasystem.model.DeliveryProduct;
import com.tartarugacometasystem.model.DeliveryStatus;

public class DeliveryService {
    private final DeliveryDAO deliveryDAO;
    private final DeliveryProductDAO deliveryProductDAO;
    private final DeliveryHistoryDAO deliveryHistoryDAO;

    public DeliveryService() {
        this.deliveryDAO = new DeliveryDAO();
        this.deliveryProductDAO = new DeliveryProductDAO();
        this.deliveryHistoryDAO = new DeliveryHistoryDAO();
    }

    public void createDelivery(Delivery delivery) throws SQLException, IllegalArgumentException {
        validateDelivery(delivery);

        if (delivery.getTrackingCode() == null || delivery.getTrackingCode().trim().isEmpty()) {
            delivery.setTrackingCode(generateTrackingCode());
        }

        deliveryDAO.save(delivery);

        DeliveryHistory history = new DeliveryHistory(delivery.getId(), null, DeliveryStatus.PENDENTE, "SYSTEM");
        history.setObservations("Entrega criada no sistema");
        deliveryHistoryDAO.save(history);
    }

    public void updateDelivery(Delivery delivery) throws SQLException, IllegalArgumentException {
        if (delivery.getId() == null) {
            throw new IllegalArgumentException("ID da entrega é obrigatório para atualização");
        }

        Optional<Delivery> existingOptional = deliveryDAO.findById(delivery.getId());
        Delivery existingDelivery = existingOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        if (!existingDelivery.getStatus().equals(delivery.getStatus())) {
            DeliveryHistory history = new DeliveryHistory(
                delivery.getId(),
                existingDelivery.getStatus(),
                delivery.getStatus(),
                "SYSTEM"
            );
            deliveryHistoryDAO.save(history);
        }

        deliveryDAO.update(delivery);
    }

    public void deleteDelivery(Integer deliveryId) throws SQLException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new IllegalArgumentException("ID da entrega inválido");
        }

        deliveryProductDAO.deleteByDeliveryId(deliveryId);
        deliveryDAO.delete(deliveryId);
    }

    public Optional<Delivery> getDeliveryById(Integer deliveryId) throws SQLException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new IllegalArgumentException("ID da entrega inválido");
        }

        Optional<Delivery> delivery = deliveryDAO.findById(deliveryId);
        if (delivery.isPresent()) {
            List<DeliveryProduct> products = deliveryProductDAO.findByDeliveryId(deliveryId);
            delivery.get().setProducts(products);
        }
        return delivery;
    }

    public Optional<Delivery> getDeliveryByTrackingCode(String trackingCode) throws SQLException {
        if (trackingCode == null || trackingCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de rastreamento não pode estar vazio");
        }

        Optional<Delivery> delivery = deliveryDAO.findByTrackingCode(trackingCode.trim());
        if (delivery.isPresent()) {
            List<DeliveryProduct> products = deliveryProductDAO.findByDeliveryId(delivery.get().getId());
            delivery.get().setProducts(products);
        }
        return delivery;
    }

    public List<Delivery> getAllDeliveries() throws SQLException {
        return deliveryDAO.findAll();
    }

    public List<Delivery> getDeliveriesByStatus(DeliveryStatus status) throws SQLException {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }

        return deliveryDAO.findByStatus(status);
    }

    public List<Delivery> getDeliveriesByShipperId(Integer shipperId) throws SQLException {
        if (shipperId == null || shipperId <= 0) {
            throw new IllegalArgumentException("ID do remetente inválido");
        }

        return deliveryDAO.findByShipperId(shipperId);
    }

    public List<Delivery> getDeliveriesByRecipientId(Integer recipientId) throws SQLException {
        if (recipientId == null || recipientId <= 0) {
            throw new IllegalArgumentException("ID do destinatário inválido");
        }

        return deliveryDAO.findByRecipientId(recipientId);
    }

    public List<Delivery> getDeliveriesByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Datas de início e fim são obrigatórias");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }

        return deliveryDAO.findByDateRange(startDate, endDate);
    }

    public void addProductToDelivery(Integer deliveryId, DeliveryProduct deliveryProduct) 
            throws SQLException, IllegalArgumentException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new IllegalArgumentException("ID da entrega inválido");
        }

        Optional<Delivery> deliveryOptional = deliveryDAO.findById(deliveryId);
        Delivery delivery = deliveryOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        validateDeliveryProduct(deliveryProduct);

        Optional<DeliveryProduct> existing = deliveryProductDAO.findByDeliveryAndProduct(deliveryId, deliveryProduct.getProductId());

        if (existing.isPresent()) {
            throw new IllegalArgumentException("Produto já adicionado a esta entrega");
        }

        deliveryProduct.setDeliveryId(deliveryId);
        deliveryProductDAO.save(deliveryProduct);

        List<DeliveryProduct> products = deliveryProductDAO.findByDeliveryId(deliveryId);
        delivery.setProducts(products);
        delivery.updateTotals();
        deliveryDAO.update(delivery);
    }

    public void removeProductFromDelivery(Integer deliveryProductId) throws SQLException {
        if (deliveryProductId == null || deliveryProductId <= 0) {
            throw new IllegalArgumentException("ID do produto da entrega inválido");
        }

        Optional<DeliveryProduct> deliveryProductOptional = deliveryProductDAO.findById(deliveryProductId);
        DeliveryProduct deliveryProduct = deliveryProductOptional.orElseThrow(() -> new IllegalArgumentException("Produto da entrega não encontrado"));

        Optional<Delivery> deliveryOptional = deliveryDAO.findById(deliveryProduct.getDeliveryId());
        Delivery delivery = deliveryOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        deliveryProductDAO.delete(deliveryProductId);

        List<DeliveryProduct> products = deliveryProductDAO.findByDeliveryId(delivery.getId());
        delivery.setProducts(products);
        delivery.updateTotals();
        deliveryDAO.update(delivery);
    }

    public void updateDeliveryProduct(DeliveryProduct deliveryProduct) throws SQLException, IllegalArgumentException {
        if (deliveryProduct.getId() == null) {
            throw new IllegalArgumentException("ID do produto da entrega é obrigatório");
        }

        validateDeliveryProduct(deliveryProduct);

        Optional<DeliveryProduct> existingOptional = deliveryProductDAO.findById(deliveryProduct.getId());
        DeliveryProduct existing = existingOptional.orElseThrow(() -> new IllegalArgumentException("Produto da entrega não encontrado"));

        deliveryProductDAO.update(deliveryProduct);

        Optional<Delivery> deliveryOptional = deliveryDAO.findById(existing.getDeliveryId());
        Delivery delivery = deliveryOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        List<DeliveryProduct> products = deliveryProductDAO.findByDeliveryId(delivery.getId());
        delivery.setProducts(products);
        delivery.updateTotals();
        deliveryDAO.update(delivery);
    }

    public List<DeliveryProduct> getDeliveryProducts(Integer deliveryId) throws SQLException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new IllegalArgumentException("ID da entrega inválido");
        }

        return deliveryProductDAO.findByDeliveryId(deliveryId);
    }

    public void updateDeliveryStatus(Integer deliveryId, DeliveryStatus newStatus, String observations, String user) 
            throws SQLException, IllegalArgumentException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new IllegalArgumentException("ID da entrega inválido");
        }

        if (newStatus == null) {
            throw new IllegalArgumentException("Novo status não pode ser nulo");
        }

        Optional<Delivery> deliveryOptional = deliveryDAO.findById(deliveryId);
        Delivery delivery = deliveryOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        DeliveryStatus previousStatus = delivery.getStatus();

        if (previousStatus.equals(newStatus)) {
            throw new IllegalArgumentException("Novo status é igual ao status atual");
        }

        validateStatusTransition(previousStatus, newStatus);

        delivery.setStatus(newStatus);

        switch (newStatus) {
            case EM_TRANSITO:
                delivery.setShipmentDate(LocalDateTime.now());
                break;
            case ENTREGUE:
                delivery.setDeliveryDate(LocalDateTime.now());
                break;
            case CANCELADA:
                delivery.setCancellationDate(LocalDateTime.now());
                break;
            case PENDENTE:
                delivery.setPickupDate(null);
                delivery.setShipmentDate(null);
                break;
            default:
                break;
        }

        deliveryDAO.update(delivery);

        DeliveryHistory history = new DeliveryHistory(deliveryId, previousStatus, newStatus, user);
        history.setObservations(observations);
        deliveryHistoryDAO.save(history);
    }

    public List<DeliveryHistory> getDeliveryHistory(Integer deliveryId) throws SQLException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new IllegalArgumentException("ID da entrega inválido");
        }

        return deliveryHistoryDAO.findByDeliveryId(deliveryId);
    }

    public void cancelDelivery(Integer deliveryId, String reason, String user) 
            throws SQLException, IllegalArgumentException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new IllegalArgumentException("ID da entrega inválido");
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Motivo do cancelamento é obrigatório");
        }

        Optional<Delivery> deliveryOptional = deliveryDAO.findById(deliveryId);
        Delivery delivery = deliveryOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        if (delivery.getStatus().equals(DeliveryStatus.ENTREGUE)) {
            throw new IllegalArgumentException("Não é possível cancelar uma entrega já realizada");
        }

        delivery.setStatus(DeliveryStatus.CANCELADA);
        delivery.setCancellationDate(LocalDateTime.now());
        delivery.setCancellationReason(reason);

        deliveryDAO.update(delivery);

        DeliveryHistory history = new DeliveryHistory(deliveryId, delivery.getStatus(), DeliveryStatus.CANCELADA, user);
        history.setObservations("Cancelamento: " + reason);
        deliveryHistoryDAO.save(history);
    }

    public void markAsDelivered(Integer deliveryId, String user) throws SQLException, IllegalArgumentException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new IllegalArgumentException("ID da entrega inválido");
        }

        Optional<Delivery> deliveryOptional = deliveryDAO.findById(deliveryId);
        Delivery delivery = deliveryOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        if (!delivery.getStatus().equals(DeliveryStatus.EM_TRANSITO)) {
            throw new IllegalArgumentException("Apenas entregas em trânsito podem ser marcadas como entregues");
        }

        delivery.setStatus(DeliveryStatus.ENTREGUE);
        delivery.setDeliveryDate(LocalDateTime.now());

        deliveryDAO.update(delivery);

        DeliveryHistory history = new DeliveryHistory(deliveryId, DeliveryStatus.EM_TRANSITO, DeliveryStatus.ENTREGUE, user);
        history.setObservations("Entrega realizada");
        deliveryHistoryDAO.save(history);
    }

    public void markAsNotDelivered(Integer deliveryId, String reason, String user) 
            throws SQLException, IllegalArgumentException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new IllegalArgumentException("ID da entrega inválido");
        }

        Optional<Delivery> deliveryOptional = deliveryDAO.findById(deliveryId);
        Delivery delivery = deliveryOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        if (!delivery.getStatus().equals(DeliveryStatus.EM_TRANSITO)) {
            throw new IllegalArgumentException("Apenas entregas em trânsito podem ser marcadas como não entregues");
        }

        delivery.setStatus(DeliveryStatus.NAO_REALIZADA);

        deliveryDAO.update(delivery);

        DeliveryHistory history = new DeliveryHistory(deliveryId, DeliveryStatus.EM_TRANSITO, DeliveryStatus.NAO_REALIZADA, user);
        history.setObservations("Não realizada: " + (reason != null ? reason : ""));
        deliveryHistoryDAO.save(history);
    }

    private void validateDelivery(Delivery delivery) {
        if (delivery.getShipperId() == null || delivery.getShipperId() <= 0) {
            throw new IllegalArgumentException("ID do remetente é obrigatório");
        }

        if (delivery.getRecipientId() == null || delivery.getRecipientId() <= 0) {
            throw new IllegalArgumentException("ID do destinatário é obrigatório");
        }

        if (delivery.getShipperId().equals(delivery.getRecipientId())) {
            throw new IllegalArgumentException("Remetente e destinatário não podem ser a mesma pessoa");
        }

        if (delivery.getOriginAddressId() == null || delivery.getOriginAddressId() <= 0) {
            throw new IllegalArgumentException("ID do endereço de origem é obrigatório");
        }

        if (delivery.getDestinationAddressId() == null || delivery.getDestinationAddressId() <= 0) {
            throw new IllegalArgumentException("ID do endereço de destino é obrigatório");
        }

        if (delivery.getOriginAddressId().equals(delivery.getDestinationAddressId())) {
            throw new IllegalArgumentException("Endereço de origem e destino não podem ser iguais");
        }
    }

    private void validateDeliveryProduct(DeliveryProduct deliveryProduct) {
        if (deliveryProduct.getProductId() == null || deliveryProduct.getProductId() <= 0) {
            throw new IllegalArgumentException("ID do produto é obrigatório");
        }

        if (deliveryProduct.getQuantity() == null || deliveryProduct.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        if (deliveryProduct.getUnitWeightKg() == null || deliveryProduct.getUnitWeightKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Peso unitário deve ser maior que zero");
        }

        if (deliveryProduct.getUnitVolumeM3() == null || deliveryProduct.getUnitVolumeM3().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Volume unitário deve ser maior que zero");
        }

        if (deliveryProduct.getUnitValue() == null || deliveryProduct.getUnitValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor unitário deve ser maior que zero");
        }
    }

    private void validateStatusTransition(DeliveryStatus currentStatus, DeliveryStatus newStatus) {
        List<DeliveryStatus> validTransitions = new ArrayList<>();

        switch (currentStatus) {
            case PENDENTE:
                validTransitions.add(DeliveryStatus.EM_TRANSITO);
                validTransitions.add(DeliveryStatus.CANCELADA);
                break;
            case EM_TRANSITO:
                validTransitions.add(DeliveryStatus.ENTREGUE);
                validTransitions.add(DeliveryStatus.NAO_REALIZADA);
                validTransitions.add(DeliveryStatus.CANCELADA);
                break;
            case ENTREGUE:
                // Nenhuma transição permitida
                break;
            case CANCELADA:
                // Nenhuma transição permitida
                break;
            case NAO_REALIZADA:
                validTransitions.add(DeliveryStatus.EM_TRANSITO);
                validTransitions.add(DeliveryStatus.CANCELADA);
                break;
            default:
                break;
        }

        if (!validTransitions.contains(newStatus)) {
            throw new IllegalArgumentException(
                "Transição inválida de " + currentStatus + " para " + newStatus
            );
        }
    }

    private String generateTrackingCode() {
        return "TG" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
