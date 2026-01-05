package com.tartarugacometasystem.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.dao.DeliveryDAO;
import com.tartarugacometasystem.dao.DeliveryHistoryDAO;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryHistory;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.util.DateFormatter;

public class DeliveryService {
    private final DeliveryDAO deliveryDAO;
    private final ClientService clientService;
    private final AddressService addressService;
    private final DeliveryHistoryDAO deliveryHistoryDAO;

    public DeliveryService() {
        this.deliveryDAO = new DeliveryDAO();
        this.clientService = new ClientService();
        this.addressService = new AddressService();
        this.deliveryHistoryDAO = new DeliveryHistoryDAO();
    }

    /**
     * Cria uma nova entrega.
     *
     * @param delivery O objeto Delivery a ser criado.
     * @return O objeto Delivery criado com o ID.
     * @throws SQLException             Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se a entrega for inválida.
     */
    public Delivery createDelivery(Delivery delivery) throws SQLException {
        validateDelivery(delivery);
        delivery.setCreationDate(LocalDateTime.now()); // Definir data de criação
        delivery.setUpdatedAt(LocalDateTime.now());    // Definir data de atualização inicial
        delivery.setStatus(DeliveryStatus.PENDING);    // Status inicial

        Delivery createdDelivery = deliveryDAO.save(delivery);

        // Registrar histórico da criação
        DeliveryHistory historyEntry = new DeliveryHistory();
        historyEntry.setDeliveryId(createdDelivery.getId());
        historyEntry.setPreviousStatus(null); // Não havia status anterior
        historyEntry.setNewStatus(DeliveryStatus.PENDING);
        historyEntry.setChangeDate(LocalDateTime.now());
        historyEntry.setLocation("Sistema"); // Ou a localização inicial
        deliveryHistoryDAO.save(historyEntry);

        // Enriquecer a entrega criada antes de retorná-la
        enrichDelivery(createdDelivery);

        return createdDelivery;
    }

    /**
     * Busca uma entrega pelo ID.
     *
     * @param id O ID da entrega.
     * @return Um Optional contendo o Delivery se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Delivery> getDeliveryById(Integer id) throws SQLException {
        Optional<Delivery> delivery = deliveryDAO.findById(id);
        delivery.ifPresent(this::enrichDelivery); // Enriquecer se presente
        return delivery;
    }

    /**
     * Atualiza uma entrega existente.
     *
     * @param delivery O objeto Delivery a ser atualizado.
     * @throws SQLException             Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se a entrega for inválida ou não existir.
     */
    public void updateDelivery(Delivery delivery) throws SQLException {
        if (delivery.getId() == null) {
            throw new IllegalArgumentException("ID da entrega é obrigatório para atualização.");
        }
        validateDelivery(delivery);

        Optional<Delivery> existingDeliveryOpt = deliveryDAO.findById(delivery.getId());
        if (existingDeliveryOpt.isEmpty()) {
            throw new IllegalArgumentException("Entrega com ID " + delivery.getId() + " não encontrada.");
        }
        Delivery existingDelivery = existingDeliveryOpt.get();

        // Atualizar a data de atualização
        delivery.setUpdatedAt(LocalDateTime.now());

        // Se o status mudou, registrar no histórico
        if (!existingDelivery.getStatus().equals(delivery.getStatus())) {
            DeliveryHistory historyEntry = new DeliveryHistory();
            historyEntry.setDeliveryId(delivery.getId());
            historyEntry.setPreviousStatus(existingDelivery.getStatus());
            historyEntry.setNewStatus(delivery.getStatus());
            historyEntry.setChangeDate(LocalDateTime.now());
            historyEntry.setLocation("Sistema"); // Ou a localização da atualização
            deliveryHistoryDAO.save(historyEntry);
        }

        deliveryDAO.update(delivery);
    }

    /**
     * Deleta uma entrega pelo ID.
     *
     * @param id O ID da entrega a ser deletada.
     * @throws SQLException             Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se a entrega não existir.
     */
    public void deleteDelivery(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("ID da entrega é obrigatório para exclusão.");
        }
        if (deliveryDAO.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Entrega com ID " + id + " não encontrada para exclusão.");
        }
        deliveryDAO.delete(id);
    }

    /**
     * Busca todas as entregas.
     *
     * @return Uma lista de todas as entregas.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> getAllDeliveries() throws SQLException {
        List<Delivery> deliveries = deliveryDAO.getAll();
        deliveries.forEach(this::enrichDelivery); // Enriquecer cada entrega na lista
        return deliveries;
    }

    /**
     * Busca entregas por status.
     *
     * @param status O status da entrega.
     * @return Uma lista de entregas com o status especificado.<br>
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> getDeliveriesByStatus(DeliveryStatus status) throws SQLException {
        List<Delivery> deliveries = deliveryDAO.findByStatus(status);
        deliveries.forEach(this::enrichDelivery); // Enriquecer cada entrega na lista
        return deliveries;
    }

    /**
     * Busca uma entrega pelo código de rastreio.
     *
     * @param trackingCode O código de rastreio.
     * @return Um Optional contendo o Delivery se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Delivery> getDeliveryByTrackingCode(String trackingCode) throws SQLException {
        Optional<Delivery> delivery = deliveryDAO.findByTrackingCode(trackingCode);
        delivery.ifPresent(this::enrichDelivery); // Enriquecer se presente
        return delivery;
    }

    // --- MÉTODOS CORRIGIDOS/ADICIONADOS ---

    /**
     * Atualiza o status de uma entrega e registra o histórico.
     * Este método unifica a lógica de markDeliveryAsDelivered e markDeliveryAsNotPerformed.
     *
     * @param id                 O ID da entrega a ser atualizada.
     * @param newStatus          O novo status da entrega.
     * @param reasonNotDelivered O motivo se a entrega não foi realizada (pode ser null).
     * @param updatedBy          Quem realizou a atualização (ex: "Sistema", "Usuário X").
     * @throws SQLException             Se ocorrer um erro de banco de dados.
     * @throws IllegalArgumentException Se o ID for inválido, o status for nulo, ou a transição de status for inválida.
     */
    public void updateDeliveryStatus(Integer id, DeliveryStatus newStatus, String reasonNotDelivered, String updatedBy) throws SQLException {
        if (id == null || newStatus == null) {
            throw new IllegalArgumentException("ID da entrega e novo status não podem ser nulos.");
        }

        Optional<Delivery> deliveryOpt = deliveryDAO.findById(id);
        if (deliveryOpt.isEmpty()) {
            throw new IllegalArgumentException("Entrega com ID " + id + " não encontrada.");
        }
        Delivery delivery = deliveryOpt.get();
        DeliveryStatus oldStatus = delivery.getStatus();

        // Validações de transição de status
        if (oldStatus == DeliveryStatus.DELIVERED && newStatus != DeliveryStatus.DELIVERED) {
            throw new IllegalArgumentException("Entrega já está no status 'Entregue'. Não pode ser alterada para outro status.");
        }
        if (oldStatus == DeliveryStatus.CANCELED && newStatus != DeliveryStatus.CANCELED) {
            throw new IllegalArgumentException("Entrega cancelada não pode ser alterada para outro status.");
        }

        // Lógica específica para cada novo status
        if (newStatus == DeliveryStatus.DELIVERED) {
            delivery.setDeliveryDate(LocalDateTime.now());
            delivery.setReasonNotDelivered(null); // Limpa o motivo se for entregue
        } else if (newStatus == DeliveryStatus.NOT_PERFORMED) {
            if (reasonNotDelivered == null || reasonNotDelivered.trim().isEmpty()) {
                throw new IllegalArgumentException("O motivo da não entrega é obrigatório para o status 'Não Realizada'.");
            }
            delivery.setDeliveryDate(null); // Limpa a data de entrega
            delivery.setReasonNotDelivered(reasonNotDelivered);
        } else {
            // Para outros status (PENDING, IN_TRANSIT, etc.), limpa campos específicos se necessário
            delivery.setDeliveryDate(null);
            delivery.setReasonNotDelivered(null);
        }

        delivery.setStatus(newStatus);
        delivery.setUpdatedAt(LocalDateTime.now());

        deliveryDAO.update(delivery);

        // Registrar histórico
        DeliveryHistory historyEntry = new DeliveryHistory();
        historyEntry.setDeliveryId(delivery.getId());
        historyEntry.setPreviousStatus(oldStatus);
        historyEntry.setNewStatus(newStatus);
        historyEntry.setChangeDate(LocalDateTime.now());
        historyEntry.setLocation(updatedBy); // Usar 'updatedBy' como localização ou criar um campo específico
        if (newStatus == DeliveryStatus.NOT_PERFORMED && reasonNotDelivered != null) {
            historyEntry.setObservations("Motivo: " + reasonNotDelivered);
        }
        deliveryHistoryDAO.save(historyEntry);
    }

    /**
     * Busca entregas com base em um termo de pesquisa (código de rastreio, status, etc.).
     * Este método foi renomeado de 'searchDeliveries' para 'search'.
     *
     * @param searchTerm O termo de pesquisa.
     * @return Uma lista de entregas que correspondem ao termo.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> search(String searchTerm) throws SQLException {
        List<Delivery> deliveries = deliveryDAO.search(searchTerm);
        deliveries.forEach(this::enrichDelivery); // Enriquecer cada entrega na lista
        return deliveries;
    }

    // --- Métodos auxiliares ---

    /**
     * Valida os campos de uma entrega.
     *
     * @param delivery O objeto Delivery a ser validado.
     * @throws IllegalArgumentException Se algum campo obrigatório estiver faltando ou for inválido.
     */
    private void validateDelivery(Delivery delivery) {
        if (delivery == null) {
            throw new IllegalArgumentException("Objeto Delivery não pode ser nulo.");
        }
        if (delivery.getTrackingCode() == null || delivery.getTrackingCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Código de rastreio é obrigatório.");
        }
        if (delivery.getSenderId() == null) {
            throw new IllegalArgumentException("Remetente é obrigatório.");
        }
        if (delivery.getRecipientId() == null) {
            throw new IllegalArgumentException("Destinatário é obrigatório.");
        }
        if (delivery.getOriginAddressId() == null) {
            throw new IllegalArgumentException("Endereço de origem é obrigatório.");
        }
        if (delivery.getDestinationAddressId() == null) {
            throw new IllegalArgumentException("Endereço de destino é obrigatório.");
        }
        if (delivery.getTotalValue() == null || delivery.getTotalValue().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor total deve ser um número positivo.");
        }
        if (delivery.getFreightValue() == null || delivery.getFreightValue().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor do frete deve ser um número positivo.");
        }
        if (delivery.getTotalWeightKg() == null || delivery.getTotalWeightKg().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Peso total deve ser um número positivo.");
        }
        if (delivery.getTotalVolumeM3() == null || delivery.getTotalVolumeM3().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Volume total deve ser um número positivo.");
        }
        if (delivery.getStatus() == null) {
            throw new IllegalArgumentException("Status da entrega é obrigatório.");
        }
    }

    /**
     * Enriquecer um objeto Delivery com dados formatados e objetos relacionados.
     *
     * @param delivery O objeto Delivery a ser enriquecido.
     */
    private void enrichDelivery(Delivery delivery) {
        if (delivery == null) return;

        // Preencher os campos formatados
        delivery.setFormattedCreationDate(DateFormatter.formatLocalDateTime(delivery.getCreationDate()));
        delivery.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(delivery.getUpdatedAt()));
        delivery.setFormattedDeliveryDate(DateFormatter.formatLocalDateTime(delivery.getDeliveryDate()));

        try {
            // Enriquecer com dados do remetente
            if (delivery.getSenderId() != null) {
                clientService.getClientById(delivery.getSenderId()).ifPresent(delivery::setSender);
            }
            // Enriquecer com dados do destinatário
            if (delivery.getRecipientId() != null) {
                clientService.getClientById(delivery.getRecipientId()).ifPresent(delivery::setRecipient);
            }
            // Enriquecer com dados do endereço de origem
            if (delivery.getOriginAddressId() != null) {
                addressService.getAddressById(delivery.getOriginAddressId()).ifPresent(delivery::setOriginAddress);
            }
            // Enriquecer com dados do endereço de destino
            if (delivery.getDestinationAddressId() != null) {
                addressService.getAddressById(delivery.getDestinationAddressId()).ifPresent(delivery::setDestinationAddress);
            }
            // Enriquecer com histórico da entrega
            if (delivery.getId() != null) {
                List<DeliveryHistory> history = deliveryHistoryDAO.getHistoryByDeliveryId(delivery.getId());
                history.forEach(h -> {
                    h.setFormattedChangeDate(DateFormatter.formatLocalDateTime(h.getChangeDate()));
                    if (h.getPreviousStatus() != null) {
                        h.setFormattedPreviousStatus(h.getPreviousStatus().getLabel());
                    }
                    if (h.getNewStatus() != null) {
                        h.setFormattedNewStatus(h.getNewStatus().getLabel());
                    }
                });
                delivery.setHistory(history);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao enriquecer entrega " + delivery.getId() + ": " + e.getMessage());
        }
    }
}
