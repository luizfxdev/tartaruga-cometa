package br.com.tartarugacometa.entrega.historico;

import java.time.LocalDateTime;
import br.com.tartarugacometa.enums.StatusEntrega;

public class HistoricoEntrega {
    private Integer id;
    private Integer deliveryId;
    private StatusEntrega previousStatus;
    private StatusEntrega newStatus;
    private LocalDateTime changeDate;
    private String observations;
    private String user;
    private String location;
    private LocalDateTime createdAt;

    private String formattedChangeDate;
    private String formattedCreatedAt;
    private String formattedPreviousStatus;
    private String formattedNewStatus;

    public HistoricoEntrega() {
    }

    public HistoricoEntrega(Integer deliveryId, StatusEntrega previousStatus, StatusEntrega newStatus,
                           String observations, String user, String location) {
        this.deliveryId = deliveryId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.observations = observations;
        this.user = user;
        this.location = location;
        this.changeDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public HistoricoEntrega(Integer id, Integer deliveryId, StatusEntrega previousStatus, StatusEntrega newStatus,
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

    public StatusEntrega getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(StatusEntrega previousStatus) {
        this.previousStatus = previousStatus;
    }

    public StatusEntrega getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(StatusEntrega newStatus) {
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

    public String getFormattedPreviousStatus() {
        return formattedPreviousStatus;
    }

    public void setFormattedPreviousStatus(String formattedPreviousStatus) {
        this.formattedPreviousStatus = formattedPreviousStatus;
    }

    public String getFormattedNewStatus() {
        return formattedNewStatus;
    }

    public void setFormattedNewStatus(String formattedNewStatus) {
        this.formattedNewStatus = formattedNewStatus;
    }

    @Override
    public String toString() {
        return "HistoricoEntrega{" +
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
