package com.tartarugacometasystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Delivery {
    private Integer id;
    private String trackingCode;
    private Integer shipperId;
    private Integer recipientId;
    private Integer originAddressId;
    private Integer destinationAddressId;
    private DeliveryStatus status;
    private BigDecimal totalValue;
    private BigDecimal totalWeightKg;
    private BigDecimal totalVolumeM3;
    private BigDecimal freightValue;
    private String observations;
    private LocalDateTime createdAt;
    private LocalDateTime pickupDate;
    private LocalDateTime shipmentDate;
    private LocalDateTime deliveryDate;
    private LocalDateTime cancellationDate;
    private String cancellationReason;
    private LocalDateTime updatedAt;
    private List<DeliveryProduct> products;

    public Delivery() {
        this.products = new ArrayList<>();
        this.status = DeliveryStatus.PENDENTE;
        this.totalValue = BigDecimal.ZERO;
        this.totalWeightKg = BigDecimal.ZERO;
        this.totalVolumeM3 = BigDecimal.ZERO;
    }

    public Delivery(String trackingCode, Integer shipperId, Integer recipientId,
                   Integer originAddressId, Integer destinationAddressId) {
        this();
        this.trackingCode = trackingCode;
        this.shipperId = shipperId;
        this.recipientId = recipientId;
        this.originAddressId = originAddressId;
        this.destinationAddressId = destinationAddressId;
    }

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

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
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

    public BigDecimal getFreightValue() {
        return freightValue;
    }

    public void setFreightValue(BigDecimal freightValue) {
        this.freightValue = freightValue;
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

    public LocalDateTime getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDateTime pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDateTime getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(LocalDateTime shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<DeliveryProduct> getProducts() {
        return products;
    }

    public void setProducts(List<DeliveryProduct> products) {
        this.products = products;
    }

    public void addProduct(DeliveryProduct product) {
        this.products.add(product);
        updateTotals();
    }

    public void updateTotals() {
        this.totalValue = BigDecimal.ZERO;
        this.totalWeightKg = BigDecimal.ZERO;
        this.totalVolumeM3 = BigDecimal.ZERO;

        for (DeliveryProduct product : products) {
            this.totalValue = this.totalValue.add(product.getSubtotal());
            this.totalWeightKg = this.totalWeightKg.add(
                product.getUnitWeightKg().multiply(new BigDecimal(product.getQuantity()))
            );
            this.totalVolumeM3 = this.totalVolumeM3.add(
                product.getUnitVolumeM3().multiply(new BigDecimal(product.getQuantity()))
            );
        }
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", trackingCode='" + trackingCode + '\'' +
                ", status=" + status +
                ", totalValue=" + totalValue +
                ", totalWeightKg=" + totalWeightKg +
                ", totalVolumeM3=" + totalVolumeM3 +
                '}';
    }
}
