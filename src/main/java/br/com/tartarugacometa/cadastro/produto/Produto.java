package br.com.tartarugacometa.cadastro.produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Produto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal weightKg;
    private BigDecimal volumeM3;
    private BigDecimal declaredValue;
    private String category;
    private boolean active;
    private Integer stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Produto() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    public Produto(String name, String description, BigDecimal price, BigDecimal weightKg,
                   BigDecimal volumeM3, BigDecimal declaredValue, String category,
                   boolean active, Integer stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.weightKg = weightKg;
        this.volumeM3 = volumeM3;
        this.declaredValue = declaredValue;
        this.category = category;
        this.active = active;
        this.stockQuantity = stockQuantity;
        this.createdAt = LocalDateTime.now();
    }

    public Produto(Integer id, String name, String description, BigDecimal price,
                   BigDecimal weightKg, BigDecimal volumeM3, BigDecimal declaredValue,
                   String category, boolean active, Integer stockQuantity,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weightKg = weightKg;
        this.volumeM3 = volumeM3;
        this.declaredValue = declaredValue;
        this.category = category;
        this.active = active;
        this.stockQuantity = stockQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getVolumeM3() {
        return volumeM3;
    }

    public void setVolumeM3(BigDecimal volumeM3) {
        this.volumeM3 = volumeM3;
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(BigDecimal declaredValue) {
        this.declaredValue = declaredValue;
        this.updatedAt = LocalDateTime.now();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
        this.updatedAt = LocalDateTime.now();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto product = (Produto) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", weightKg=" + weightKg +
                ", volumeM3=" + volumeM3 +
                ", declaredValue=" + declaredValue +
                ", category='" + category + '\'' +
                ", active=" + active +
                ", stockQuantity=" + stockQuantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
