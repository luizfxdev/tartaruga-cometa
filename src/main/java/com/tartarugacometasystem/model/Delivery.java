package com.tartarugacometasystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List; // Para o histórico

public class Delivery {
    private Integer id;
    private String trackingCode;
    private Integer senderId; // ID do cliente remetente
    private Integer recipientId; // ID do cliente destinatário
    private Integer originAddressId; // ID do endereço de origem
    private Integer destinationAddressId; // ID do endereço de destino
    private BigDecimal totalValue;
    private BigDecimal freightValue;
    private BigDecimal totalWeightKg;
    private BigDecimal totalVolumeM3;
    private DeliveryStatus status;
    private String observations;
    private LocalDateTime creationDate;
    private LocalDateTime deliveryDate; // Data de entrega efetiva
    private String reasonNotDelivered; // Motivo da não entrega
    private LocalDateTime updatedAt;

    // Campos para objetos relacionados (para enriquecimento)
    private Client sender; // Objeto Cliente Remetente
    private Client recipient; // Objeto Cliente Destinatário
    private Address originAddress; // Objeto Endereço de Origem
    private Address destinationAddress; // Objeto Endereço de Destino
    private List<DeliveryHistory> history; // Histórico da entrega

    // Campos formatados para exibição no JSP
    private String formattedCreationDate;
    private String formattedDeliveryDate;
    private String formattedUpdatedAt;

    // Construtor padrão
    public Delivery() {
    }

    // Construtor completo (sem IDs e datas)
    public Delivery(String trackingCode, Integer senderId, Integer recipientId, Integer originAddressId,
                    Integer destinationAddressId, BigDecimal totalValue, BigDecimal freightValue,
                    BigDecimal totalWeightKg, BigDecimal totalVolumeM3, DeliveryStatus status,
                    String observations, LocalDateTime deliveryDate, String reasonNotDelivered) {
        this.trackingCode = trackingCode;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.originAddressId = originAddressId;
        this.destinationAddressId = destinationAddressId;
        this.totalValue = totalValue;
        this.freightValue = freightValue;
        this.totalWeightKg = totalWeightKg;
        this.totalVolumeM3 = totalVolumeM3;
        this.status = status;
        this.observations = observations;
        this.deliveryDate = deliveryDate;
        this.reasonNotDelivered = reasonNotDelivered;
    }

    // Construtor completo (com IDs e datas)
    public Delivery(Integer id, String trackingCode, Integer senderId, Integer recipientId, Integer originAddressId,
                    Integer destinationAddressId, BigDecimal totalValue, BigDecimal freightValue,
                    BigDecimal totalWeightKg, BigDecimal totalVolumeM3, DeliveryStatus status,
                    String observations, LocalDateTime creationDate, LocalDateTime deliveryDate,
                    String reasonNotDelivered, LocalDateTime updatedAt) {
        this.id = id;
        this.trackingCode = trackingCode;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.originAddressId = originAddressId;
        this.destinationAddressId = destinationAddressId;
        this.totalValue = totalValue;
        this.freightValue = freightValue;
        this.totalWeightKg = totalWeightKg;
        this.totalVolumeM3 = totalVolumeM3;
        this.status = status;
        this.observations = observations;
        this.creationDate = creationDate;
        this.deliveryDate = deliveryDate;
        this.reasonNotDelivered = reasonNotDelivered;
        this.updatedAt = updatedAt;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public Integer getOriginAddressId() {
        return originAddressId;
    }

    public void setOriginAddressId(Integer originAddressId) {
        this.originAddressId = originAddressId;
    }

    public Integer getDestinationAddressId() {
        return destinationAddressId;
    }

    public void setDestinationAddressId(Integer destinationAddressId) {
        this.destinationAddressId = destinationAddressId;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getFreightValue() {
        return freightValue;
    }

    public void setFreightValue(BigDecimal freightValue) {
        this.freightValue = freightValue;
    }

    public BigDecimal getTotalWeightKg() {
        return totalWeightKg;
    }

    public void setTotalWeightKg(BigDecimal totalWeightKg) {
        this.totalWeightKg = totalWeightKg;
    }

    public BigDecimal getTotalVolumeM3() {
        return totalVolumeM3;
    }

    public void setTotalVolumeM3(BigDecimal totalVolumeM3) {
        this.totalVolumeM3 = totalVolumeM3;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getReasonNotDelivered() {
        return reasonNotDelivered;
    }

    public void setReasonNotDelivered(String reasonNotDelivered) {
        this.reasonNotDelivered = reasonNotDelivered;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Getters e Setters para objetos relacionados (NOVO)
    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public Client getRecipient() {
        return recipient;
    }

    public void setRecipient(Client recipient) {
        this.recipient = recipient;
    }

    public Address getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(Address originAddress) {
        this.originAddress = originAddress;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(Address destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public List<DeliveryHistory> getHistory() {
        return history;
    }

    public void setHistory(List<DeliveryHistory> history) {
        this.history = history;
    }

    // Getters e Setters para campos formatados
    public String getFormattedCreationDate() {
        return formattedCreationDate;
    }

    public void setFormattedCreationDate(String formattedCreationDate) {
        this.formattedCreationDate = formattedCreationDate;
    }

    public String getFormattedDeliveryDate() {
        return formattedDeliveryDate;
    }

    public void setFormattedDeliveryDate(String formattedDeliveryDate) {
        this.formattedDeliveryDate = formattedDeliveryDate;
    }

    public String getFormattedUpdatedAt() {
        return formattedUpdatedAt;
    }

    public void setFormattedUpdatedAt(String formattedUpdatedAt) {
        this.formattedUpdatedAt = formattedUpdatedAt;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", trackingCode='" + trackingCode + '\'' +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", originAddressId=" + originAddressId +
                ", destinationAddressId=" + destinationAddressId +
                ", totalValue=" + totalValue +
                ", freightValue=" + freightValue +
                ", totalWeightKg=" + totalWeightKg +
                ", totalVolumeM3=" + totalVolumeM3 +
                ", status=" + status +
                ", observations='" + observations + '\'' +
                ", creationDate=" + creationDate +
                ", deliveryDate=" + deliveryDate +
                ", reasonNotDelivered='" + reasonNotDelivered + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
