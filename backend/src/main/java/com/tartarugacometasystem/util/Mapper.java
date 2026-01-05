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
     * Converte HttpServletRequest -> HashMap<String, String> -> Address.
     * Mantém compatibilidade com o método já existente (mapToAddress(HashMap...)).
     */
    public static Address mapToAddress(HttpServletRequest request) {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (value != null && value.length > 0) {
                params.put(key, value[0]);
            } else {
                params.put(key, ""); // Garante que chaves existam, mesmo com valor vazio
            }
        });
        return mapToAddress(params);
    }

    /**
     * Mapeia um HashMap de parâmetros para um objeto Address.
     *
     * @param params HashMap contendo os parâmetros da requisição.
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

        Optional.ofNullable(params.get("addressType"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return AddressType.valueOf(s.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro ao converter tipo de endereço: '" + s + "' para AddressType. " + e.getMessage());
                        return null;
                    }
                })
                .ifPresent(address::setAddressType);

        Optional.ofNullable(params.get("street"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(address::setStreet);

        Optional.ofNullable(params.get("number"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(address::setNumber);

        Optional.ofNullable(params.get("complement"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(address::setComplement);

        Optional.ofNullable(params.get("neighborhood"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(address::setNeighborhood);

        Optional.ofNullable(params.get("city"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(address::setCity);

        Optional.ofNullable(params.get("state"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(address::setState);

        Optional.ofNullable(params.get("zipCode"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(address::setZipCode);

        Optional.ofNullable(params.get("country"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(address::setCountry);

        Optional.ofNullable(params.get("reference"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(address::setReference);

        Optional.ofNullable(params.get("isMain"))
                .map(s -> s.equalsIgnoreCase("on") || s.equalsIgnoreCase("true"))
                .ifPresent(address::setIsMain);

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
     * Converte HttpServletRequest -> HashMap<String, String> -> Client.
     */
    public static Client mapToClient(HttpServletRequest request) {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (value != null && value.length > 0) {
                params.put(key, value[0]);
            } else {
                params.put(key, "");
            }
        });
        return mapToClient(params);
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

        Optional.ofNullable(params.get("name"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(client::setName);

        Optional.ofNullable(params.get("document"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(client::setDocument);

        Optional.ofNullable(params.get("email"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(client::setEmail);

        Optional.ofNullable(params.get("phone"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(client::setPhone);

        Optional.ofNullable(params.get("personType"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return PersonType.valueOf(s.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro ao converter tipo de pessoa: '" + s + "' para PersonType. " + e.getMessage());
                        return null;
                    }
                })
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
     * Converte HttpServletRequest -> HashMap<String, String> -> Product.
     */
    public static Product mapToProduct(HttpServletRequest request) {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (value != null && value.length > 0) {
                params.put(key, value[0]);
            } else {
                params.put(key, "");
            }
        });
        return mapToProduct(params);
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

        Optional.ofNullable(params.get("name"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(product::setName);

        Optional.ofNullable(params.get("description"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(product::setDescription);

        Optional.ofNullable(params.get("price"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return new BigDecimal(s.trim().replace(',', '.'));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter preço do produto: '" + s + "' para BigDecimal. " + e.getMessage());
                        return null;
                    }
                })
                .ifPresent(product::setPrice);

        Optional.ofNullable(params.get("weightKg"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return new BigDecimal(s.trim().replace(',', '.'));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter peso do produto: '" + s + "' para BigDecimal. " + e.getMessage());
                        return null;
                    }
                })
                .ifPresent(product::setWeightKg);

        Optional.ofNullable(params.get("volumeM3"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return new BigDecimal(s.trim().replace(',', '.'));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter volume do produto: '" + s + "' para BigDecimal. " + e.getMessage());
                        return null;
                    }
                })
                .ifPresent(product::setVolumeM3);

        Optional.ofNullable(params.get("declaredValue"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return new BigDecimal(s.trim().replace(',', '.'));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter valor declarado: '" + s + "' para BigDecimal. " + e.getMessage());
                        return null;
                    }
                })
                .ifPresent(product::setDeclaredValue);

        Optional.ofNullable(params.get("category"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(product::setCategory);

        Optional.ofNullable(params.get("active"))
                .map(s -> s.equalsIgnoreCase("on") || s.equalsIgnoreCase("true"))
                .ifPresent(product::setActive);

        Optional.ofNullable(params.get("stockQuantity"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return Integer.parseInt(s.trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter quantidade em estoque: '" + s + "' para Integer. " + e.getMessage());
                        return null;
                    }
                })
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
     * Converte HttpServletRequest -> HashMap<String, String> -> Delivery.
     */
    public static Delivery mapToDelivery(HttpServletRequest request) {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (value != null && value.length > 0) {
                params.put(key, value[0]);
            } else {
                params.put(key, "");
            }
        });
        return mapToDelivery(params);
    }

    /**
     * Mapeia um HashMap de parâmetros para um objeto Delivery.
     */
    public static Delivery mapToDelivery(HashMap<String, String> params) {
        Delivery delivery = new Delivery();

        Optional.ofNullable(params.get("id"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setId);

        // CORREÇÃO 1: Adicionado o mapeamento para trackingCode
        Optional.ofNullable(params.get("trackingCode"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(delivery::setTrackingCode);

        // CORREÇÃO 2: Mapeando 'shipperId' do JSP para 'senderId' no Delivery
        Optional.ofNullable(params.get("shipperId")) // O nome do campo no JSP é 'shipperId'
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
                .map(s -> {
                    try {
                        return new BigDecimal(s.trim().replace(',', '.'));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter valor total da entrega: '" + s + "' para BigDecimal. " + e.getMessage());
                        return null;
                    }
                })
                .ifPresent(delivery::setTotalValue);

        Optional.ofNullable(params.get("freightValue"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return new BigDecimal(s.trim().replace(',', '.'));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter valor do frete da entrega: '" + s + "' para BigDecimal. " + e.getMessage());
                        return null;
                    }
                })
                .ifPresent(delivery::setFreightValue);

        Optional.ofNullable(params.get("totalWeightKg"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return new BigDecimal(s.trim().replace(',', '.'));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter peso total da entrega: '" + s + "' para BigDecimal. " + e.getMessage());
                        return null;
                    }
                })
                .ifPresent(delivery::setTotalWeightKg);

        Optional.ofNullable(params.get("totalVolumeM3"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return new BigDecimal(s.trim().replace(',', '.'));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter volume total da entrega: '" + s + "' para BigDecimal. " + e.getMessage());
                        return null;
                    }
                })
                .ifPresent(delivery::setTotalVolumeM3);

        Optional.ofNullable(params.get("status"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return DeliveryStatus.valueOf(s.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro ao converter status da entrega: '" + s + "' para DeliveryStatus. " + e.getMessage());
                        return null;
                    }
                })
                .ifPresent(delivery::setStatus);

        Optional.ofNullable(params.get("observations"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(delivery::setObservations);

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
