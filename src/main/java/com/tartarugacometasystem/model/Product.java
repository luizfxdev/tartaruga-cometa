package com.tartarugacometasystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal weightKg; // Peso do produto em quilogramas
    private BigDecimal volumeM3; // Volume do produto em metros cúbicos
    private Integer stockQuantity; // Quantidade em estoque (se aplicável)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Campos formatados
    private String formattedCreatedAt;
    private String formattedUpdatedAt;

    public Product() {
    }

    // Construtor completo (sem IDs e datas)
    public Product(String name, String description, BigDecimal price, BigDecimal weightKg, BigDecimal volumeM3, Integer stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.weightKg = weightKg;
        this.volumeM3 = volumeM3;
        this.stockQuantity = stockQuantity;
    }

    // Construtor completo (com IDs e datas)
    public Product(Integer id, String name, String description, BigDecimal price, BigDecimal weightKg, BigDecimal volumeM3,
                   Integer stockQuantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weightKg = weightKg;
        this.volumeM3 = volumeM3;
        this.stockQuantity = stockQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getVolumeM3() {
        return volumeM3;
    }

    public void setVolumeM3(BigDecimal volumeM3) {
        this.volumeM3 = volumeM3;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
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
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", weightKg=" + weightKg +
                ", volumeM3=" + volumeM3 +
                ", stockQuantity=" + stockQuantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
