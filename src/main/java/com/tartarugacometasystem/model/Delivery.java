package com.tartarugacometasystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Delivery {
    private Integer id;
    private String trackingCode;
    private Integer shipperId;
    private Integer recipientId;
    private Integer originAddressId;
    private Integer destinationAddressId;
    private BigDecimal totalValue;
    private BigDecimal freightValue;
    private BigDecimal totalWeightKg;
    private BigDecimal totalVolumeM3;
    private DeliveryStatus status;
    private String observations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deliveredAt;
    private String reasonNotDelivered;
    private LocalDateTime deliveryDate; // NOVO

    // Campos para objetos relacionados
    private Client shipper;
    private Client recipient;
    private Address originAddress;
    private Address destinationAddress;
    private List<DeliveryHistory> history; // NOVO

    // Campos formatados
    private String formattedCreatedAt;
    private String formattedUpdatedAt;
    private String formattedDeliveredAt;
    private String formattedDeliveryDate; // NOVO

    // Construtor padrão
    public Delivery() {
    }

    // Construtor completo (sem IDs e datas)
    public Delivery(String trackingCode, Integer shipperId, Integer recipientId, 
                    Integer originAddressId, Integer destinationAddressId, 
                    BigDecimal totalValue, BigDecimal freightValue,
                    BigDecimal totalWeightKg, BigDecimal totalVolumeM3, 
                    DeliveryStatus status, String observations) {
        this.trackingCode = trackingCode;
        this.shipperId = shipperId;
        this.recipientId = recipientId;
        this.originAddressId = originAddressId;
        this.destinationAddressId = destinationAddressId;
        this.totalValue = totalValue;
        this.freightValue = freightValue;
        this.totalWeightKg = totalWeightKg;
        this.totalVolumeM3 = totalVolumeM3;
        this.status = status;
        this.observations = observations;
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

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getReasonNotDelivered() {
        return reasonNotDelivered;
    }

    public void setReasonNotDelivered(String reasonNotDelivered) {
        this.reasonNotDelivered = reasonNotDelivered;
    }

    // NOVOS GETTERS/SETTERS
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<DeliveryHistory> getHistory() {
        return history;
    }

    public void setHistory(List<DeliveryHistory> history) {
        this.history = history;
    }

    public String getFormattedDeliveryDate() {
        return formattedDeliveryDate;
    }

    public void setFormattedDeliveryDate(String formattedDeliveryDate) {
        this.formattedDeliveryDate = formattedDeliveryDate;
    }

    public String getFormattedDeliveredAt() {
        return formattedDeliveredAt;
    }

    public void setFormattedDeliveredAt(String formattedDeliveredAt) {
        this.formattedDeliveredAt = formattedDeliveredAt;
    }

    // Getters/Setters dos objetos relacionados
    public Client getShipper() {
        return shipper;
    }

    public void setShipper(Client shipper) {
        this.shipper = shipper;
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

    public String getFormattedCreatedAt() {
        return formattedCreatedAt;
    }

    public void setFormattedCreatedAt(String formattedCreatedAt) {
        this.formattedCreatedAt = formattedCreatedAt;
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
               ", shipperId=" + shipperId +
               ", recipientId=" + recipientId +
               ", status=" + status +
               '}';
    }
}