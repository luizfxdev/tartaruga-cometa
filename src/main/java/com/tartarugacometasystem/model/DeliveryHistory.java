package com.tartarugacometasystem.model;

import java.time.LocalDateTime;

public class DeliveryHistory {
    private Integer id;
    private Integer deliveryId;
    private DeliveryStatus status; // Agora é um enum
    private String observations;
    private String user; // Usuário que realizou a alteração
    private LocalDateTime createdAt;

    // Campos formatados para exibição no JSP
    private String formattedCreatedAt;

    // Construtor padrão
    public DeliveryHistory() {
    }

    // Construtor completo
    public DeliveryHistory(Integer deliveryId, DeliveryStatus status, String observations, String user) {
        this.deliveryId = deliveryId;
        this.status = status;
        this.observations = observations;
        this.user = user;
        this.createdAt = LocalDateTime.now(); // Define a data de criação automaticamente
    }

    // Getters e Setters
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedCreatedAt() {
        return formattedCreatedAt;
    }

    public void setFormattedCreatedAt(String formattedCreatedAt) {
        this.formattedCreatedAt = formattedCreatedAt;
    }

    @Override
    public String toString() {
        return "DeliveryHistory{" +
               "id=" + id +
               ", deliveryId=" + deliveryId +
               ", status=" + status +
               ", observations='" + observations + '\'' +
               ", user='" + user + '\'' +
               ", createdAt=" + createdAt +
               '}';
    }
}
