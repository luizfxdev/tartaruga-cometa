package com.tartarugacometasystem.model;

import java.time.LocalDateTime;

public class DeliveryHistory {
    private Integer id;
    private Integer deliveryId;
    private DeliveryStatus previousStatus; // NOVO CAMPO: Status anterior da entrega
    private DeliveryStatus newStatus; // NOVO CAMPO: Novo status da entrega (substitui 'status')
    private LocalDateTime changeDate; // NOVO CAMPO: Data e hora da mudança de status
    private String observations;
    private String user; // Usuário que realizou a mudança
    private String location; // NOVO CAMPO: Localização no momento da mudança (opcional)
    private LocalDateTime createdAt; // Mantido para registro de criação do histórico em si

    // Campos formatados para exibição no JSP
    private String formattedChangeDate;
    private String formattedCreatedAt;

    // Construtor padrão
    public DeliveryHistory() {
    }

    // Construtor completo (sem IDs e datas)
    public DeliveryHistory(Integer deliveryId, DeliveryStatus previousStatus, DeliveryStatus newStatus,
                           String observations, String user, String location) {
        this.deliveryId = deliveryId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.observations = observations;
        this.user = user;
        this.location = location;
        this.changeDate = LocalDateTime.now(); // Define a data da mudança automaticamente
        this.createdAt = LocalDateTime.now(); // Define a data de criação do registro de histórico
    }

    // Construtor completo (com IDs e datas)
    public DeliveryHistory(Integer id, Integer deliveryId, DeliveryStatus previousStatus, DeliveryStatus newStatus,
                           LocalDateTime changeDate, String observations, String user, String location,
                           LocalDateTime createdAt) {
        this.id = id;
        this.deliveryId = deliveryId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changeDate = changeDate;
        this.observations = observations;
        this.user = user;
        this.location = location;
        this.createdAt = createdAt;
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

    public DeliveryStatus getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(DeliveryStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    public DeliveryStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(DeliveryStatus newStatus) {
        this.newStatus = newStatus;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedChangeDate() {
        return formattedChangeDate;
    }

    public void setFormattedChangeDate(String formattedChangeDate) {
        this.formattedChangeDate = formattedChangeDate;
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
                ", previousStatus=" + previousStatus +
                ", newStatus=" + newStatus +
                ", changeDate=" + changeDate +
                ", observations='" + observations + '\'' +
                ", user='" + user + '\'' +
                ", location='" + location + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
