package com.tartarugacometasystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects; // Importar para equals/hashCode

public class Product {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal weightKg; // Peso do produto em quilogramas
    private BigDecimal volumeM3; // Volume do produto em metros cúbicos
    private BigDecimal declaredValue; // Valor declarado do produto
    private String category; // Categoria do produto
    private boolean active; // Mudança: de Boolean isActive para boolean active
    private Integer stockQuantity; // Quantidade em estoque
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product() {
        // Opcional: Definir valores padrão para novos produtos
        this.createdAt = LocalDateTime.now();
        this.active = true; // Produtos novos são ativos por padrão
    }

    // Construtor completo (sem IDs e datas, mas com 'active')
    public Product(String name, String description, BigDecimal price, BigDecimal weightKg,
                   BigDecimal volumeM3, BigDecimal declaredValue, String category,
                   boolean active, Integer stockQuantity) { // Mudança: de Boolean isActive para boolean active
        this.name = name;
        this.description = description;
        this.price = price;
        this.weightKg = weightKg;
        this.volumeM3 = volumeM3;
        this.declaredValue = declaredValue;
        this.category = category;
        this.active = active; // Atribuição do novo campo 'active'
        this.stockQuantity = stockQuantity;
        this.createdAt = LocalDateTime.now(); // Definir data de criação aqui também
    }

    // Construtor completo (com IDs e datas, e 'active')
    public Product(Integer id, String name, String description, BigDecimal price,
                   BigDecimal weightKg, BigDecimal volumeM3, BigDecimal declaredValue,
                   String category, boolean active, Integer stockQuantity, // Mudança: de Boolean isActive para boolean active
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weightKg = weightKg;
        this.volumeM3 = volumeM3;
        this.declaredValue = declaredValue;
        this.category = category;
        this.active = active; // Atribuição do novo campo 'active'
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
        this.updatedAt = LocalDateTime.now(); // Exemplo de atualização de timestamp
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

    // Mudança: Getter para 'active' agora é 'isActive()'
    public boolean isActive() { // Mudança: de getIsActive() para isActive()
        return active;
    }

    // Mudança: Setter para 'active'
    public void setActive(boolean active) { // Mudança: de setIsActive() para setActive()
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

    // Removidos os getters e setters para formattedCreatedAt e formattedUpdatedAt

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id); // Usar Objects.equals para Integer
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
                ", declaredValue=" + declaredValue +
                ", category='" + category + '\'' +
                ", active=" + active + // Mudança: de isActive para active
                ", stockQuantity=" + stockQuantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
