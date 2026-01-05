package com.tartarugacometasystem.model;

import java.math.BigDecimal;

public class DeliveryProduct {
    private Integer id;
    private Integer deliveryId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal unitWeightKg;
    private BigDecimal unitVolumeM3;
    private BigDecimal unitValue;
    private BigDecimal subtotal;
    private String observations;

    public DeliveryProduct() {}

    public DeliveryProduct(Integer deliveryId, Integer productId, Integer quantity,
                           BigDecimal unitWeightKg, BigDecimal unitVolumeM3,
                           BigDecimal unitValue) {
        this.deliveryId = deliveryId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitWeightKg = unitWeightKg;
        this.unitVolumeM3 = unitVolumeM3;
        this.unitValue = unitValue;
        this.subtotal = unitValue.multiply(new BigDecimal(quantity));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitWeightKg() {
        return unitWeightKg;
    }

    public void setUnitWeightKg(BigDecimal unitWeightKg) {
        this.unitWeightKg = unitWeightKg;
    }

    public BigDecimal getUnitVolumeM3() {
        return unitVolumeM3;
    }

    public void setUnitVolumeM3(BigDecimal unitVolumeM3) {
        this.unitVolumeM3 = unitVolumeM3;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public String toString() {
        return "DeliveryProduct{" +
                "id=" + id +
                ", deliveryId=" + deliveryId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", unitWeightKg=" + unitWeightKg +
                ", unitVolumeM3=" + unitVolumeM3 +
                ", unitValue=" + unitValue +
                ", subtotal=" + subtotal +
                ", observations='" + observations + '\'' +
                '}';
    }
}
