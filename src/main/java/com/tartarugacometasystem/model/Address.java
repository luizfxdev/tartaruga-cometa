package com.tartarugacometasystem.model;

import java.time.LocalDateTime;

public class Address {
    private Integer id;
    private Integer clientId; // ID do cliente ao qual o endereço pertence
    private AddressType addressType; // NOVO CAMPO: Tipo de endereço (ORIGEM, DESTINO, CADASTRAL)
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String reference; // NOVO CAMPO: Ponto de referência
    private Boolean isMain; // Indica se é o endereço principal do cliente (renomeado de isPrincipal)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Campos formatados para exibição no JSP
    private String formattedAddress;
    private String formattedCreatedAt;
    private String formattedUpdatedAt;

    // Construtor padrão
    public Address() {
    }

    // Construtor completo (sem IDs e datas)
    public Address(Integer clientId, AddressType addressType, String street, String number, String complement, String neighborhood,
                   String city, String state, String zipCode, String country, String reference, Boolean isMain) {
        this.clientId = clientId;
        this.addressType = addressType;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.reference = reference;
        this.isMain = isMain;
    }

    // Construtor completo (com IDs e datas)
    public Address(Integer id, Integer clientId, AddressType addressType, String street, String number, String complement, String neighborhood,
                   String city, String state, String zipCode, String country, String reference, Boolean isMain,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.clientId = clientId;
        this.addressType = addressType;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.reference = reference;
        this.isMain = isMain;
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

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getIsMain() { // Renomeado
        return isMain;
    }

    public void setIsMain(Boolean isMain) { // Renomeado
        this.isMain = isMain;
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

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
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
        return "Address{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", addressType=" + addressType +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", complement='" + complement + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                ", reference='" + reference + '\'' +
                ", isMain=" + isMain +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
