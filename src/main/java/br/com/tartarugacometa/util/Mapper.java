package br.com.tartarugacometa.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.tartarugacometa.cadastro.endereco.Endereco;
import br.com.tartarugacometa.enums.TipoEndereco;
import br.com.tartarugacometa.cadastro.cliente.Cliente;
import br.com.tartarugacometa.entrega.Entrega;
import br.com.tartarugacometa.enums.StatusEntrega;
import br.com.tartarugacometa.enums.TipoPessoa;
import br.com.tartarugacometa.cadastro.produto.Produto;

import jakarta.servlet.http.HttpServletRequest;

public class Mapper {

    private static final Logger LOG = Logger.getLogger(Mapper.class.getName());

    /**
     * Converte HttpServletRequest -> HashMap<String, String> -> Endereco.
     * Mantém compatibilidade com o método já existente (mapToAddress(HashMap...)).
     */
    public static Endereco mapToAddress(HttpServletRequest request) {
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
     * Mapeia um HashMap de parâmetros para um objeto Endereco.
     *
     * @param params HashMap contendo os parâmetros da requisição.
     * @return Um objeto Endereco preenchido.
     */
    public static Endereco mapToAddress(HashMap<String, String> params) {
        Endereco address = new Endereco();

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
                        return TipoEndereco.valueOf(s.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        LOG.log(Level.WARNING, "Erro ao converter tipo de endereço: '" + s + "' para TipoEndereco.", e);
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
     * Converte HttpServletRequest -> HashMap<String, String> -> Cliente.
     */
    public static Cliente mapToClient(HttpServletRequest request) {
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
     * Mapeia um HashMap de parâmetros para um objeto Cliente.
     */
    public static Cliente mapToClient(HashMap<String, String> params) {
        Cliente client = new Cliente();

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
                        return TipoPessoa.valueOf(s.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        LOG.log(Level.WARNING, "Erro ao converter tipo de pessoa: '" + s + "' para TipoPessoa.", e);
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
     * Converte HttpServletRequest -> HashMap<String, String> -> Produto.
     */
    public static Produto mapToProduct(HttpServletRequest request) {
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
     * Mapeia um HashMap de parâmetros para um objeto Produto.
     */
    public static Produto mapToProduct(HashMap<String, String> params) {
        Produto product = new Produto();

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
                        LOG.log(Level.WARNING, "Erro ao converter preço do produto: '" + s + "' para BigDecimal.", e);
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
                        LOG.log(Level.WARNING, "Erro ao converter peso do produto: '" + s + "' para BigDecimal.", e);
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
                        LOG.log(Level.WARNING, "Erro ao converter volume do produto: '" + s + "' para BigDecimal.", e);
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
                        LOG.log(Level.WARNING, "Erro ao converter valor declarado: '" + s + "' para BigDecimal.", e);
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
                        LOG.log(Level.WARNING, "Erro ao converter quantidade em estoque: '" + s + "' para Integer.", e);
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
     * Converte HttpServletRequest -> HashMap<String, String> -> Entrega.
     */
    public static Entrega mapToDelivery(HttpServletRequest request) {
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
     * Mapeia um HashMap de parâmetros para um objeto Entrega.
     */
    public static Entrega mapToDelivery(HashMap<String, String> params) {
        Entrega delivery = new Entrega();

        Optional.ofNullable(params.get("id"))
                .filter(s -> !s.trim().isEmpty())
                .map(Integer::parseInt)
                .ifPresent(delivery::setId);

        Optional.ofNullable(params.get("trackingCode"))
                .filter(s -> !s.trim().isEmpty())
                .ifPresent(delivery::setTrackingCode);

        // Campo no JSP chama-se 'shipperId', mapeado para senderId em Entrega
        Optional.ofNullable(params.get("shipperId"))
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
                        LOG.log(Level.WARNING, "Erro ao converter valor total da entrega: '" + s + "' para BigDecimal.", e);
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
                        LOG.log(Level.WARNING, "Erro ao converter valor do frete da entrega: '" + s + "' para BigDecimal.", e);
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
                        LOG.log(Level.WARNING, "Erro ao converter peso total da entrega: '" + s + "' para BigDecimal.", e);
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
                        LOG.log(Level.WARNING, "Erro ao converter volume total da entrega: '" + s + "' para BigDecimal.", e);
                        return null;
                    }
                })
                .ifPresent(delivery::setTotalVolumeM3);

        Optional.ofNullable(params.get("status"))
                .filter(s -> !s.trim().isEmpty())
                .map(s -> {
                    try {
                        return StatusEntrega.valueOf(s.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        LOG.log(Level.WARNING, "Erro ao converter status da entrega: '" + s + "' para StatusEntrega.", e);
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
