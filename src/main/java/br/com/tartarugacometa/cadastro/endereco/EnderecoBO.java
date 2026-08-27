package br.com.tartarugacometa.cadastro.endereco;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import br.com.tartarugacometa.cadastro.endereco.EnderecoDAO;
import br.com.tartarugacometa.cadastro.endereco.Endereco;
import br.com.tartarugacometa.cadastro.cliente.Cliente;
import br.com.tartarugacometa.cadastro.cliente.ClienteBO;
import br.com.tartarugacometa.util.DateFormatter;

public class EnderecoBO {

    private final EnderecoDAO addressDAO;
    private final ClienteBO clientService;

    public EnderecoBO() {
        this.addressDAO = new EnderecoDAO();
        this.clientService = new ClienteBO();
    }

    // ==============================
    // Métodos auxiliares para compatibilidade com o servlet
    // ==============================

    /** Compatibilidade: addAddress → createAddress */
    public Endereco addAddress(Endereco address) throws SQLException {
        return createAddress(address);
    }

    /** Compatibilidade: setPrincipalAddress → setMainAddress */
    public void setPrincipalAddress(Integer clientId, Integer addressId) throws SQLException {
        setMainAddress(clientId, addressId);
    }

    // ==============================
    // CRUD
    // ==============================

    public Endereco createAddress(Endereco address) throws SQLException {
        validateAddress(address);
        return addressDAO.save(address);
    }

    public Optional<Endereco> getAddressById(Integer id) throws SQLException {
        Optional<Endereco> address = addressDAO.findById(id);
        address.ifPresent(this::enrichAddress);
        return address;
    }

    public void updateAddress(Endereco address) throws SQLException {
        if (address.getId() == null) {
            throw new IllegalArgumentException("ID do endereço é obrigatório para atualização.");
        }

        validateAddress(address);

        Optional<Endereco> existing = addressDAO.findById(address.getId());
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Endereço com ID " + address.getId() + " não encontrado.");
        }

        if (address.getClientId() != null) {
            Optional<Cliente> client = clientService.getClientById(address.getClientId());
            if (client.isEmpty()) {
                throw new IllegalArgumentException("Cliente com ID " + address.getClientId() + " não existe.");
            }
        }

        addressDAO.update(address);
    }

    public void deleteAddress(Integer id) throws SQLException {
        addressDAO.delete(id);
    }

    public List<Endereco> getAllAddresses() throws SQLException {
        List<Endereco> addresses = addressDAO.getAll();
        addresses.forEach(this::enrichAddress);
        return addresses;
    }

    public List<Endereco> getAddressesByClientId(Integer clientId) throws SQLException {
        List<Endereco> addresses = addressDAO.findByClientId(clientId);
        addresses.forEach(this::enrichAddress);
        return addresses;
    }

    public void setMainAddress(Integer clientId, Integer addressId) throws SQLException {
        Optional<Cliente> client = clientService.getClientById(clientId);
        if (client.isEmpty()) {
            throw new IllegalArgumentException("Cliente com ID " + clientId + " não encontrado.");
        }

        Optional<Endereco> address = addressDAO.findById(addressId);
        if (address.isEmpty() || !address.get().getClientId().equals(clientId)) {
            throw new IllegalArgumentException(
                "Endereço com ID " + addressId + " não encontrado ou não pertence ao cliente " + clientId + "."
            );
        }

        addressDAO.setMainAddress(clientId, addressId);
    }

    // ==============================
    // Validações
    // ==============================

    private void validateAddress(Endereco address) {
        if (address.getClientId() == null)
            throw new IllegalArgumentException("ID do cliente é obrigatório.");
        if (address.getAddressType() == null)
            throw new IllegalArgumentException("Tipo de endereço é obrigatório.");
        if (address.getStreet() == null || address.getStreet().trim().isEmpty())
            throw new IllegalArgumentException("Rua é obrigatória.");
        if (address.getNumber() == null || address.getNumber().trim().isEmpty())
            throw new IllegalArgumentException("Número é obrigatório.");
        if (address.getNeighborhood() == null || address.getNeighborhood().trim().isEmpty())
            throw new IllegalArgumentException("Bairro é obrigatório.");
        if (address.getCity() == null || address.getCity().trim().isEmpty())
            throw new IllegalArgumentException("Cidade é obrigatória.");
        if (address.getState() == null || address.getState().trim().isEmpty())
            throw new IllegalArgumentException("Estado é obrigatório.");
        if (address.getZipCode() == null || address.getZipCode().trim().isEmpty())
            throw new IllegalArgumentException("CEP é obrigatório.");
        if (address.getCountry() == null || address.getCountry().trim().isEmpty())
            throw new IllegalArgumentException("País é obrigatório.");
    }

    // ==============================
    // Enriquecimento
    // ==============================

    private void enrichAddress(Endereco address) {
        if (address == null) return;

        String complement = (address.getComplement() != null && !address.getComplement().isBlank())
                ? " (" + address.getComplement() + ")"
                : "";

        String fullAddress = String.format(
            "%s, %s%s - %s, %s - %s, %s - CEP: %s",
            safe(address.getStreet()),
            safe(address.getNumber()),
            complement,
            safe(address.getNeighborhood()),
            safe(address.getCity()),
            safe(address.getState()),
            safe(address.getCountry()),
            safe(address.getZipCode())
        );

        address.setFormattedAddress(fullAddress);

        if (address.getCreatedAt() != null)
            address.setFormattedCreatedAt(DateFormatter.formatLocalDateTime(address.getCreatedAt()));

        if (address.getUpdatedAt() != null)
            address.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(address.getUpdatedAt()));

        try {
            if (address.getClientId() != null) {
                clientService.getClientById(address.getClientId()).ifPresent(address::setClient);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao enriquecer endereço " + address.getId() + ": " + e.getMessage());
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
