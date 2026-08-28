package br.com.tartarugacometa.entrega;

import br.com.tartarugacometa.enums.StatusEntrega;
import br.com.tartarugacometa.cadastro.cliente.Cliente;
import br.com.tartarugacometa.cadastro.endereco.Endereco;
import br.com.tartarugacometa.entrega.historico.HistoricoEntrega;
import br.com.tartarugacometa.entrega.item.ItemEntrega;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Entrega {
    private Integer id;
    private String trackingCode;
    private Integer senderId;
    private Integer recipientId;
    private Integer originAddressId;
    private Integer destinationAddressId;
    private BigDecimal totalValue;
    private BigDecimal freightValue;
    private BigDecimal totalWeightKg;
    private BigDecimal totalVolumeM3;
    private StatusEntrega status;
    private String observations;
    private LocalDateTime creationDate;
    private LocalDateTime deliveryDate;
    private String reasonNotDelivered;
    private LocalDateTime updatedAt;

    private Cliente sender;
    private Cliente recipient;
    private Endereco originAddress;
    private Endereco destinationAddress;
    private List<HistoricoEntrega> history;
    private List<ItemEntrega> itens;

    private String formattedCreationDate;
    private String formattedDeliveryDate;
    private String formattedUpdatedAt;

    public Entrega() {
    }

    public Entrega(String trackingCode, Integer senderId, Integer recipientId, Integer originAddressId,
                    Integer destinationAddressId, BigDecimal totalValue, BigDecimal freightValue,
                    BigDecimal totalWeightKg, BigDecimal totalVolumeM3, StatusEntrega status,
                    String observations, LocalDateTime deliveryDate, String reasonNotDelivered) {
        this.trackingCode = trackingCode;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.originAddressId = originAddressId;
        this.destinationAddressId = destinationAddressId;
        this.totalValue = totalValue;
        this.freightValue = freightValue;
        this.totalWeightKg = totalWeightKg;
        this.totalVolumeM3 = totalVolumeM3;
        this.status = status;
        this.observations = observations;
        this.deliveryDate = deliveryDate;
        this.reasonNotDelivered = reasonNotDelivered;
    }

    public Entrega(Integer id, String trackingCode, Integer senderId, Integer recipientId, Integer originAddressId,
                    Integer destinationAddressId, BigDecimal totalValue, BigDecimal freightValue,
                    BigDecimal totalWeightKg, BigDecimal totalVolumeM3, StatusEntrega status,
                    String observations, LocalDateTime creationDate, LocalDateTime deliveryDate,
                    String reasonNotDelivered, LocalDateTime updatedAt) {
        this.id = id;
        this.trackingCode = trackingCode;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.originAddressId = originAddressId;
        this.destinationAddressId = destinationAddressId;
        this.totalValue = totalValue;
        this.freightValue = freightValue;
        this.totalWeightKg = totalWeightKg;
        this.totalVolumeM3 = totalVolumeM3;
        this.status = status;
        this.observations = observations;
        this.creationDate = creationDate;
        this.deliveryDate = deliveryDate;
        this.reasonNotDelivered = reasonNotDelivered;
        this.updatedAt = updatedAt;
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

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
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

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getFreightValue() {
        return freightValue;
    }

    public void setFreightValue(BigDecimal freightValue) {
        this.freightValue = freightValue;
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

    public StatusEntrega getStatus() {
        return status;
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getReasonNotDelivered() {
        return reasonNotDelivered;
    }

    public void setReasonNotDelivered(String reasonNotDelivered) {
        this.reasonNotDelivered = reasonNotDelivered;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Cliente getSender() {
        return sender;
    }

    public void setSender(Cliente sender) {
        this.sender = sender;
    }

    public Cliente getRecipient() {
        return recipient;
    }

    public void setRecipient(Cliente recipient) {
        this.recipient = recipient;
    }

    public Endereco getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(Endereco originAddress) {
        this.originAddress = originAddress;
    }

    public Endereco getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(Endereco destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public List<HistoricoEntrega> getHistory() {
        return history;
    }

    public void setHistory(List<HistoricoEntrega> history) {
        this.history = history;
    }

    public List<ItemEntrega> getItens() {
        return itens;
    }

    public void setItens(List<ItemEntrega> itens) {
        this.itens = itens;
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
        return "Entrega{" +
                "id=" + id +
                ", trackingCode='" + trackingCode + '\'' +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", originAddressId=" + originAddressId +
                ", destinationAddressId=" + destinationAddressId +
                ", totalValue=" + totalValue +
                ", freightValue=" + freightValue +
                ", totalWeightKg=" + totalWeightKg +
                ", totalVolumeM3=" + totalVolumeM3 +
                ", status=" + status +
                ", observations='" + observations + '\'' +
                ", creationDate=" + creationDate +
                ", deliveryDate=" + deliveryDate +
                ", reasonNotDelivered='" + reasonNotDelivered + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
