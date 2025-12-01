package com.tartarugacometasystem.model;

import java.time.LocalDateTime;

public class Client {
    private Integer id;
    private String name;
    private String document; // CPF ou CNPJ
    private String email;
    private String phone;
    private PersonType personType; // Tipo de pessoa (Física/Jurídica)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Campos formatados para exibição no JSP
    private String formattedCreatedAt;
    private String formattedUpdatedAt;
    private String formattedPersonType; // NOVO: Campo formatado para o tipo de pessoa

    // Construtor padrão
    public Client() {
    }

    // Construtor completo (sem IDs e datas)
    public Client(String name, String document, String email, String phone, PersonType personType) {
        this.name = name;
        this.document = document;
        this.email = email;
        this.phone = phone;
        this.personType = personType;
    }

    // Construtor completo (com IDs e datas)
    public Client(Integer id, String name, String document, String email, String phone, PersonType personType,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.email = email;
        this.phone = phone;
        this.personType = personType;
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

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
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

    // NOVO: Getter e Setter para formattedPersonType
    public String getFormattedPersonType() {
        return formattedPersonType;
    }

    public void setFormattedPersonType(String formattedPersonType) {
        this.formattedPersonType = formattedPersonType;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", document='" + document + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", personType=" + personType +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
