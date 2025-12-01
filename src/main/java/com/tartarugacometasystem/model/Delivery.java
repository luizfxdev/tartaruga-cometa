package com.tartarugacometasystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Delivery {
    private Integer id;
    private String trackingCode; // Renomeado de codigoRastreio
    private DeliveryStatus status;
    private LocalDateTime creationDate; // Renomeado de dataCriacao
    private LocalDateTime deliveryDate; // Renomeado de dataEntrega
    private Integer senderId; // Renomeado de idRemetente
    private Integer recipientId; // Renomeado de idDestinatario
    private Integer originAddressId; // Renomeado de idEnderecoOrigem
    private Integer destinationAddressId; // Renomeado de idEnderecoDestino
    private BigDecimal totalValue; // Renomeado de valorTotal
    private BigDecimal freightValue; // Renomeado de valorFrete
    private BigDecimal totalWeightKg; // Renomeado de pesoTotalKg
    private BigDecimal totalVolumeM3; // Renomeado de volumeTotalM3
    private LocalDateTime updatedAt;

    // Campos formatados para exibição no JSP
    private String formattedCreationDate;
    private String formattedDeliveryDate;
    private String formattedUpdatedAt;

    // Construtor padrão
    public Delivery() {
    }

    // Construtor completo (sem IDs e datas)
    public Delivery(String trackingCode, DeliveryStatus status, Integer senderId, Integer recipientId,
                    Integer originAddressId, Integer destinationAddressId, BigDecimal totalValue,
                    BigDecimal freightValue, BigDecimal totalWeightKg, BigDecimal totalVolumeM3) {
        this.trackingCode = trackingCode;
        this.status = status;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.originAddressId = originAddressId;
        this.destinationAddressId = destinationAddressId;
        this.totalValue = totalValue;
        this.freightValue = freightValue;
        this.totalWeightKg = totalWeightKg;
        this.totalVolumeM3 = totalVolumeM3;
        this.creationDate = LocalDateTime.now(); // Define a data de criação automaticamente
    }

    // Construtor completo (com IDs e datas)
    public Delivery(Integer id, String trackingCode, DeliveryStatus status, LocalDateTime creationDate,
                    LocalDateTime deliveryDate, Integer senderId, Integer recipientId,
                    Integer originAddressId, Integer destinationAddressId, BigDecimal totalValue,
                    BigDecimal freightValue, BigDecimal totalWeightKg, BigDecimal totalVolumeM3,
                    LocalDateTime updatedAt) {
        this.id = id;
        this.trackingCode = trackingCode;
        this.status = status;
        this.creationDate = creationDate;
        this.deliveryDate = deliveryDate;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.originAddressId = originAddressId;
        this.destinationAddressId = destinationAddressId;
        this.totalValue = totalValue;
        this.freightValue = freightValue;
        this.totalWeightKg = totalWeightKg;
        this.totalVolumeM3 = totalVolumeM3;
        this.updatedAt = updatedAt;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrackingCode() { // Renomeado
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) { // Renomeado
        this.trackingCode = trackingCode;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() { // Renomeado
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) { // Renomeado
        this.creationDate = creationDate;
    }

    public LocalDateTime getDeliveryDate() { // Renomeado
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) { // Renomeado
        this.deliveryDate = deliveryDate;
    }

    public Integer getSenderId() { // Renomeado
        return senderId;
    }

    public void setSenderId(Integer senderId) { // Renomeado
        this.senderId = senderId;
    }

    public Integer getRecipientId() { // Renomeado
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) { // Renomeado
        this.recipientId = recipientId;
    }

    public Integer getOriginAddressId() { // Renomeado
        return originAddressId;
    }

    public void setOriginAddressId(Integer originAddressId) { // Renomeado
        this.originAddressId = originAddressId;
    }

    public Integer getDestinationAddressId() { // Renomeado
        return destinationAddressId;
    }

    public void setDestinationAddressId(Integer destinationAddressId) { // Renomeado
        this.destinationAddressId = destinationAddressId;
    }

    public BigDecimal getTotalValue() { // Renomeado
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) { // Renomeado
        this.totalValue = totalValue;
    }

    public BigDecimal getFreightValue() { // Renomeado
        return freightValue;
    }

    public void setFreightValue(BigDecimal freightValue) { // Renomeado
        this.freightValue = freightValue;
    }

    public BigDecimal getTotalWeightKg() { // Renomeado
        return totalWeightKg;
    }

    public void setTotalWeightKg(BigDecimal totalWeightKg) { // Renomeado
        this.totalWeightKg = totalWeightKg;
    }

    public BigDecimal getTotalVolumeM3() { // Renomeado
        return totalVolumeM3;
    }

    public void setTotalVolumeM3(BigDecimal totalVolumeM3) { // Renomeado
        this.totalVolumeM3 = totalVolumeM3;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

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
                ", status=" + status +
                ", creationDate=" + creationDate +
                ", deliveryDate=" + deliveryDate +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", originAddressId=" + originAddressId +
                ", destinationAddressId=" + destinationAddressId +
                ", totalValue=" + totalValue +
                ", freightValue=" + freightValue +
                ", totalWeightKg=" + totalWeightKg +
                ", totalVolumeM3=" + totalVolumeM3 +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
