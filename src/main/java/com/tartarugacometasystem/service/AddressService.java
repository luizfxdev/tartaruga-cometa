package com.tartarugacometasystem.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional; // Importar Client para enriquecimento

import com.tartarugacometasystem.dao.AddressDAO;
import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.util.DateFormatter;

public class AddressService {
    private AddressDAO addressDAO;
    private ClientService clientService; // Para buscar dados do cliente associado

    public AddressService() {
        this.addressDAO = new AddressDAO();
        this.clientService = new ClientService(); // Inicializa o ClientService
    }

    /**
     * Cria um novo endereço.
     *
     * @param address O objeto Address a ser criado.
     * @return O objeto Address criado com o ID.
     * @throws SQLException         Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se o endereço for inválido.
     */
    public Address createAddress(Address address) throws SQLException {
        validateAddress(address);
        return addressDAO.save(address);
    }

    /**
     * Busca um endereço pelo ID.
     *
     * @param id O ID do endereço.
     * @return Um Optional contendo o Address se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Address> getAddressById(Integer id) throws SQLException {
        Optional<Address> address = addressDAO.findById(id);
        address.ifPresent(this::enrichAddress); // Enriquecer se presente
        return address;
    }

    /**
     * Atualiza um endereço existente.
     *
     * @param address O objeto Address a ser atualizado.
     * @throws SQLException         Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se o endereço for inválido ou não existir.
     */
    public void updateAddress(Address address) throws SQLException {
        if (address.getId() == null) {
            throw new IllegalArgumentException("ID do endereço é obrigatório para atualização.");
        }
        validateAddress(address);
        Optional<Address> existingAddress = addressDAO.findById(address.getId());
        if (existingAddress.isEmpty()) {
            throw new IllegalArgumentException("Endereço com ID " + address.getId() + " não encontrado.");
        }
        addressDAO.update(address);
    }

    /**
     * Deleta um endereço pelo ID.
     *
     * @param id O ID do endereço a ser deletado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void deleteAddress(Integer id) throws SQLException {
        addressDAO.delete(id);
    }

    /**
     * Busca todos os endereços, enriquecendo-os com dados do cliente.
     *
     * @return Uma lista de todos os endereços.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Address> getAllAddresses() throws SQLException {
        List<Address> addresses = addressDAO.getAll();
        addresses.forEach(this::enrichAddress);
        return addresses;
    }

    /**
     * Busca endereços por ID de cliente, enriquecendo-os com dados do cliente.
     *
     * @param clientId O ID do cliente.
     * @return Uma lista de endereços do cliente.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Address> getAddressesByClientId(Integer clientId) throws SQLException {
        // CORREÇÃO: O método na AddressDAO é findByClientId, não getAddressesByClientId
        List<Address> addresses = addressDAO.findByClientId(clientId); 
        addresses.forEach(this::enrichAddress);
        return addresses;
    }

    /**
     * Define um endereço como principal para um cliente, desmarcando outros.
     *
     * @param clientId  O ID do cliente.
     * @param addressId O ID do endereço a ser definido como principal.
     * @throws SQLException         Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se o cliente ou endereço não forem encontrados.
     */
    public void setPrincipalAddress(Integer clientId, Integer addressId) throws SQLException {
        Optional<Client> client = clientService.getClientById(clientId);
        if (client.isEmpty()) {
            throw new IllegalArgumentException("Cliente com ID " + clientId + " não encontrado.");
        }
        Optional<Address> address = addressDAO.findById(addressId);
        if (address.isEmpty() || !address.get().getClientId().equals(clientId)) {
            throw new IllegalArgumentException("Endereço com ID " + addressId + " não encontrado ou não pertence ao cliente " + clientId + ".");
        }
        addressDAO.setPrincipalAddress(clientId, addressId);
    }

    /**
     * Valida os campos de um endereço.
     *
     * @param address O objeto Address a ser validado.
     * @throws IllegalArgumentException Se algum campo for inválido.
     */
    private void validateAddress(Address address) {
        if (address.getClientId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório.");
        }
        if (address.getStreet() == null || address.getStreet().trim().isEmpty()) {
            throw new IllegalArgumentException("Rua é obrigatória.");
        }
        if (address.getNumber() == null || address.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Número é obrigatório.");
        }
        if (address.getNeighborhood() == null || address.getNeighborhood().trim().isEmpty()) {
            throw new IllegalArgumentException("Bairro é obrigatório.");
        }
        if (address.getCity() == null || address.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade é obrigatória.");
        }
        if (address.getState() == null || address.getState().trim().isEmpty()) {
            throw new IllegalArgumentException("Estado é obrigatório.");
        }
        if (address.getZipCode() == null || address.getZipCode().trim().isEmpty()) {
            throw new IllegalArgumentException("CEP é obrigatório.");
        }
        if (address.getCountry() == null || address.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("País é obrigatório.");
        }
    }

    /**
     * Enriquecer um objeto Address com dados formatados e do cliente.
     *
     * @param address O objeto Address a ser enriquecido.
     */
    private void enrichAddress(Address address) {
        if (address == null) return;

        // Formata a string completa do endereço
        String fullAddress = String.format("%s, %s%s - %s, %s - %s, %s - CEP: %s",
                address.getStreet(),
                address.getNumber(),
                (address.getComplement() != null && !address.getComplement().isEmpty() ? " (" + address.getComplement() + ")" : ""),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getZipCode());
        address.setFormattedAddress(fullAddress);

        // Formata datas
        if (address.getCreatedAt() != null) {
            address.setFormattedCreatedAt(DateFormatter.formatLocalDateTime(address.getCreatedAt()));
        }
        if (address.getUpdatedAt() != null) {
            address.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(address.getUpdatedAt()));
        }

        // Enriquecer com dados do cliente
        if (address.getClientId() != null) {
            try {
                Optional<Client> client = clientService.getClientById(address.getClientId());
                client.ifPresent(c -> {
                    // Você pode adicionar mais dados do cliente ao endereço se necessário
                    // Por exemplo, address.setClientName(c.getName());
                });
            } catch (SQLException e) {
                System.err.println("Erro ao buscar cliente para endereço " + address.getId() + ": " + e.getMessage());
                // Lidar com o erro, talvez logar ou definir um valor padrão
            }
        }
    }
}
