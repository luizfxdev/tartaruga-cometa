package com.tartarugacometasystem.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.AddressType;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryProduct;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.model.PersonType;
import com.tartarugacometasystem.model.Product;

public class Mapper {

    public static Client mapToClient(Map<String, String> params) {
        Client client = new Client();

        if (params.containsKey("personType")) {
            client.setPersonType(PersonType.fromValue(params.get("personType")));
        }

        if (params.containsKey("document")) {
            client.setDocument(params.get("document"));
        }

        if (params.containsKey("name")) {
            client.setName(params.get("name"));
        }

        if (params.containsKey("email")) {
            client.setEmail(params.get("email"));
        }

        if (params.containsKey("phone")) {
            client.setPhone(params.get("phone"));
        }

        if (params.containsKey("id")) {
            try {
                client.setId(Integer.parseInt(params.get("id")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        return client;
    }

    public static Address mapToAddress(Map<String, String> params) {
        Address address = new Address();

        if (params.containsKey("clientId")) {
            try {
                address.setClientId(Integer.parseInt(params.get("clientId")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("addressType")) {
            address.setAddressType(AddressType.fromValue(params.get("addressType")));
        }

        if (params.containsKey("street")) {
            address.setStreet(params.get("street"));
        }

        if (params.containsKey("number")) {
            address.setNumber(params.get("number"));
        }

        if (params.containsKey("complement")) {
            address.setComplement(params.get("complement"));
        }

        if (params.containsKey("neighborhood")) {
            address.setNeighborhood(params.get("neighborhood"));
        }

        if (params.containsKey("city")) {
            address.setCity(params.get("city"));
        }

        if (params.containsKey("state")) {
            address.setState(params.get("state"));
        }

        if (params.containsKey("zipCode")) {
            address.setZipCode(params.get("zipCode"));
        }

        if (params.containsKey("reference")) {
            address.setReference(params.get("reference"));
        }

        if (params.containsKey("isPrincipal")) {
            address.setIsPrincipal(Boolean.parseBoolean(params.get("isPrincipal")));
        }

        if (params.containsKey("id")) {
            try {
                address.setId(Integer.parseInt(params.get("id")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        return address;
    }

    public static Product mapToProduct(Map<String, String> params) {
        Product product = new Product();

        if (params.containsKey("name")) {
            product.setName(params.get("name"));
        }

        if (params.containsKey("description")) {
            product.setDescription(params.get("description"));
        }

        if (params.containsKey("weightKg")) {
            try {
                product.setWeightKg(new BigDecimal(params.get("weightKg")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("volumeM3")) {
            try {
                product.setVolumeM3(new BigDecimal(params.get("volumeM3")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("declaredValue")) {
            try {
                product.setDeclaredValue(new BigDecimal(params.get("declaredValue")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("category")) {
            product.setCategory(params.get("category"));
        }

        if (params.containsKey("active")) {
            product.setActive(Boolean.parseBoolean(params.get("active")));
        }

        if (params.containsKey("id")) {
            try {
                product.setId(Integer.parseInt(params.get("id")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        return product;
    }

    public static Delivery mapToDelivery(Map<String, String> params) {
        Delivery delivery = new Delivery();

        if (params.containsKey("trackingCode")) {
            delivery.setTrackingCode(params.get("trackingCode"));
        }

        if (params.containsKey("shipperId")) {
            try {
                delivery.setShipperId(Integer.parseInt(params.get("shipperId")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("recipientId")) {
            try {
                delivery.setRecipientId(Integer.parseInt(params.get("recipientId")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("originAddressId")) {
            try {
                delivery.setOriginAddressId(Integer.parseInt(params.get("originAddressId")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("destinationAddressId")) {
            try {
                delivery.setDestinationAddressId(Integer.parseInt(params.get("destinationAddressId")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("status")) {
            delivery.setStatus(DeliveryStatus.fromValue(params.get("status")));
        }

        if (params.containsKey("freightValue")) {
            try {
                delivery.setFreightValue(new BigDecimal(params.get("freightValue")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("observations")) {
            delivery.setObservations(params.get("observations"));
        }

        if (params.containsKey("id")) {
            try {
                delivery.setId(Integer.parseInt(params.get("id")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        return delivery;
    }

    public static DeliveryProduct mapToDeliveryProduct(Map<String, String> params) {
        DeliveryProduct deliveryProduct = new DeliveryProduct();

        if (params.containsKey("deliveryId")) {
            try {
                deliveryProduct.setDeliveryId(Integer.parseInt(params.get("deliveryId")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("productId")) {
            try {
                deliveryProduct.setProductId(Integer.parseInt(params.get("productId")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("quantity")) {
            try {
                deliveryProduct.setQuantity(Integer.parseInt(params.get("quantity")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("unitWeightKg")) {
            try {
                deliveryProduct.setUnitWeightKg(new BigDecimal(params.get("unitWeightKg")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("unitVolumeM3")) {
            try {
                deliveryProduct.setUnitVolumeM3(new BigDecimal(params.get("unitVolumeM3")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("unitValue")) {
            try {
                deliveryProduct.setUnitValue(new BigDecimal(params.get("unitValue")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        if (params.containsKey("observations")) {
            deliveryProduct.setObservations(params.get("observations"));
        }

        if (params.containsKey("id")) {
            try {
                deliveryProduct.setId(Integer.parseInt(params.get("id")));
            } catch (NumberFormatException e) {
                // Ignora erro de conversão
            }
        }

        return deliveryProduct;
    }

    public static Map<String, String> clientToMap(Client client) {
        Map<String, String> map = new HashMap<>();

        if (client.getId() != null) {
            map.put("id", client.getId().toString());
        }

        if (client.getPersonType() != null) {
            map.put("personType", client.getPersonType().getValue());
        }

        if (client.getDocument() != null) {
            map.put("document", client.getDocument());
        }

        if (client.getName() != null) {
            map.put("name", client.getName());
        }

        if (client.getEmail() != null) {
            map.put("email", client.getEmail());
        }

        if (client.getPhone() != null) {
            map.put("phone", client.getPhone());
        }

        return map;
    }
}
