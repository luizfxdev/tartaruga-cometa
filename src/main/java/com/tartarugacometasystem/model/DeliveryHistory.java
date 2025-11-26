package com.tartarugacometasystem.model;

import java.time.LocalDateTime;

public class DeliveryHistory {
    private Integer id;
    private Integer deliveryId;
    private DeliveryStatus previousStatus;
    private DeliveryStatus newStatus;
    private LocalDateTime changeDate;
    private String user;
    private String observations;
    private String location;

    public DeliveryHistory() {}

    public DeliveryHistory(Integer deliveryId, DeliveryStatus previousStatus,
                          DeliveryStatus newStatus, String user) {
        this.deliveryId = deliveryId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.user = user;
        this.changeDate = LocalDateTime.now();
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "DeliveryHistory{" +
                "id=" + id +
                ", deliveryId=" + deliveryId +
                ", previousStatus=" + previousStatus +
                ", newStatus=" + newStatus +
                ", changeDate=" + changeDate +
                '}';
    }
}
