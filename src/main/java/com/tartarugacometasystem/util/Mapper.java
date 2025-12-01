package com.tartarugacometasystem.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.AddressType;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.model.PersonType;
import com.tartarugacometasystem.model.Product;

import jakarta.servlet.http.HttpServletRequest;

public class Mapper {

    /**
     * Converte HttpServletRequest → HashMap<String, String> → Address.
     * Mantém compatibilidade com o método já existente (mapToAddress(HashMap...)).
     */
    public static Address mapToAddress(HttpServletRequest request) {
        HashMap<String, String> params = new HashMap<>();

        request.getParameterMap().forEach((key, value) -> {
            if (value != null && value.length > 0) {
                params.put(key, value[0]); // pega o primeiro valor sempre
            }
        });

        return mapToAddress(params); // reaproveita o método original
    }

    /**
     * Mapeia um HashMap de parâmetros para um objeto Client.
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

        Optional.ofNullable(params.get("isMain"))
                .filter(s -> !s.trim().isEmpty())
                .map(Boolean::parseBoolean)
                .ifPresent(address::setIsMain);

        Optional.ofNullable(params.get("addressType"))
                .filter(s -> !s.trim().isEmpty())
                .map(AddressType::fromValue)
                .ifPresent(address::setAddressType);

        address.setReference(params.get("reference"));

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
     * Mapeia um HashMap para Delivery.
     */
    public static Delivery mapToDelivery(HashMap<String, String> params) {
        Delivery delivery = new Delivery();

        Optional.ofNullable(params.get("id"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setId);

        delivery.setTrackingCode(params.get("trackingCode"));

        Optional.ofNullable(params.get("senderId"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setSenderId);

        Optional.ofNullable(params.get("recipientId"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setRecipientId);

        Optional.ofNullable(params.get("originAddressId"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setOriginAddressId);

        Optional.ofNullable(params.get("destinationAddressId"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setDestinationAddressId);

        Optional.ofNullable(params.get("totalValue"))
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(delivery::setTotalValue);

        Optional.ofNullable(params.get("freightValue"))
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(delivery::setFreightValue);

        Optional.ofNullable(params.get("totalWeightKg"))
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(delivery::setTotalWeightKg);

        Optional.ofNullable(params.get("totalVolumeM3"))
                .filter(s -> !s.trim().isEmpty())
                .map(BigDecimal::new)
                .ifPresent(delivery::setTotalVolumeM3);

        Optional.ofNullable(params.get("status"))
                .filter(s -> !s.trim().isEmpty())
                .map(DeliveryStatus::fromValue)
                .ifPresent(delivery::setStatus);

        delivery.setObservations(params.get("observations"));

        Optional.ofNullable(params.get("deliveryDate"))
                .filter(s -> !s.trim().isEmpty())
                .map(DateFormatter::parseLocalDateTime)
                .ifPresent(delivery::setDeliveryDate);

        Optional.ofNullable(params.get("reasonNotDelivered"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(delivery::setReasonNotDelivered);

        Optional.ofNullable(params.get("creationDate"))
                .filter(s -> !s.trim().isEmpty())
                .map(DateFormatter::parseLocalDateTime)
                .ifPresent(delivery::setCreationDate);

        Optional.ofNullable(params.get("updatedAt"))
                .filter(s -> !s.trim().isEmpty())
                .map(DateFormatter::parseLocalDateTime)
                .ifPresent(delivery::setUpdatedAt);

        return delivery;
    }
}
