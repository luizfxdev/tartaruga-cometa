package com.tartarugacometasystem.util;

import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.AddressType; // Importar AddressType
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.model.PersonType;
import com.tartarugacometasystem.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

public class Mapper {

    /**
     * Mapeia um HashMap de parâmetros para um objeto Client.
     *
     * @param params HashMap contendo os parâmetros do cliente.
     * @return Um objeto Client preenchido.
     */
    public static Client mapToClient(HashMap<String, String> params) {
        Client client = new Client();

        Optional.ofNullable(params.get("id"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(client::setId);

        client.setName(params.get("name"));
        client.setDocument(params.get("document"));
        client.setEmail(params.get("email"));
        client.setPhone(params.get("phone"));

        Optional.ofNullable(params.get("personType"))
                .filter(s -> !s.trim().isEmpty())
                .map(PersonType::fromValue)
                .ifPresent(client::setPersonType);

        Optional.ofNullable(params.get("createdAt"))
                .filter(s -> !s.trim().isEmpty())
                .map(DateFormatter::parseLocalDateTime)
                .ifPresent(client::setCreatedAt);

        Optional.ofNullable(params.get("updatedAt"))
                .filter(s -> !s.trim().isEmpty())
                .map(DateFormatter::parseLocalDateTime)
                .ifPresent(client::setUpdatedAt);

        return client;
    }

    /**
     * Mapeia um HashMap de parâmetros para um objeto Address.
     *
     * @param params HashMap contendo os parâmetros do endereço.
     * @return Um objeto Address preenchido.
     */
    public static Address mapToAddress(HashMap<String, String> params) {
        Address address = new Address();

        Optional.ofNullable(params.get("id"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(address::setId);

        Optional.ofNullable(params.get("clientId"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(address::setClientId);

        address.setStreet(params.get("street"));
        address.setNumber(params.get("number"));
        address.setComplement(params.get("complement"));
        address.setNeighborhood(params.get("neighborhood"));
        address.setCity(params.get("city"));
        address.setState(params.get("state"));
        address.setZipCode(params.get("zipCode"));
        address.setCountry(params.get("country"));

        // Campo renomeado: isPrincipal -> isMain
        Optional.ofNullable(params.get("isMain")) // Usar "isMain"
                .map(s -> s.equalsIgnoreCase("on") || s.equalsIgnoreCase("true"))
                .ifPresent(address::setIsMain); // Chamar setIsMain()

        // Novos campos: addressType e reference
        Optional.ofNullable(params.get("addressType"))
                .filter(s -> !s.trim().isEmpty())
                .map(AddressType::fromValue) // Mapear para o ENUM AddressType
                .ifPresent(address::setAddressType);

        address.setReference(params.get("reference")); // Novo campo

        Optional.ofNullable(params.get("createdAt"))
                .filter(s -> !s.trim().isEmpty())
                .map(DateFormatter::parseLocalDateTime)
                .ifPresent(address::setCreatedAt);

        Optional.ofNullable(params.get("updatedAt"))
                .filter(s -> !s.trim().isEmpty())
                .map(DateFormatter::parseLocalDateTime)
                .ifPresent(address::setUpdatedAt);

        return address;
    }

    /**
     * Mapeia um HashMap de parâmetros para um objeto Product.
     *
     * @param params HashMap contendo os parâmetros do produto.
     * @return Um objeto Product preenchido.
     */
    public static Product mapToProduct(HashMap<String, String> params) {
        Product product = new Product();

        Optional.ofNullable(params.get("id"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(product::setId);

        product.setName(params.get("name"));
        product.setDescription(params.get("description"));

        Optional.ofNullable(params.get("price"))
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(product::setPrice);

        // Novos campos: weightKg e volumeM3
        Optional.ofNullable(params.get("weightKg"))
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(product::setWeightKg);

        Optional.ofNullable(params.get("volumeM3"))
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(product::setVolumeM3);

        Optional.ofNullable(params.get("stockQuantity"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(product::setStockQuantity);

        Optional.ofNullable(params.get("createdAt"))
                .filter(s -> !s.trim().isEmpty())
                .map(DateFormatter::parseLocalDateTime)
                .ifPresent(product::setCreatedAt);

        Optional.ofNullable(params.get("updatedAt"))
                .filter(s -> !s.trim().isEmpty())
                .map(DateFormatter::parseLocalDateTime)
                .ifPresent(product::setUpdatedAt);

        return product;
    }

    /**
     * Mapeia um HashMap de parâmetros para um objeto Delivery.
     *
     * @param params HashMap contendo os parâmetros da entrega.
     * @return Um objeto Delivery preenchido.
     */
    public static Delivery mapToDelivery(HashMap<String, String> params) {
        Delivery delivery = new Delivery();

        Optional.ofNullable(params.get("id"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setId);

        // Campos renomeados
        delivery.setTrackingCode(params.get("trackingCode")); // Renomeado
        Optional.ofNullable(params.get("senderId")) // Renomeado
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setSenderId); // Renomeado

        Optional.ofNullable(params.get("recipientId")) // Renomeado
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setRecipientId); // Renomeado

        Optional.ofNullable(params.get("originAddressId")) // Renomeado
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setOriginAddressId); // Renomeado

        Optional.ofNullable(params.get("destinationAddressId")) // Renomeado
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setDestinationAddressId); // Renomeado

        Optional.ofNullable(params.get("totalValue")) // Renomeado
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(delivery::setTotalValue); // Renomeado

        Optional.ofNullable(params.get("freightValue")) // Renomeado
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(delivery::setFreightValue); // Renomeado

        Optional.ofNullable(params.get("totalWeightKg")) // Renomeado
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(delivery::setTotalWeightKg); // Renomeado

        Optional.ofNullable(params.get("totalVolumeM3")) // Renomeado
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(delivery::setTotalVolumeM3); // Renomeado

        Optional.ofNullable(params.get("status"))
                .filter(s -> !s.trim().isEmpty())
                .map(DeliveryStatus::fromValue)
                .ifPresent(delivery::setStatus);

        delivery.setObservations(params.get("observations"));

        Optional.ofNullable(params.get("creationDate")) // Renomeado
                .filter(s -> !s

