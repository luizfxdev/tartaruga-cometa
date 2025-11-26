package com.tartarugacometasystem.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.dao.AddressDAO;
import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.AddressType;
import com.tartarugacometasystem.util.Validator;

public class AddressService {
    private final AddressDAO addressDAO;

    public AddressService() {
        this.addressDAO = new AddressDAO();
    }

    public void createAddress(Address address) throws SQLException, IllegalArgumentException {
        validateAddress(address);
        addressDAO.save(address);
    }

    public void updateAddress(Address address) throws SQLException, IllegalArgumentException {
        validateAddress(address);

        if (address.getId() == null) {
            throw new IllegalArgumentException("ID do endereço é obrigatório para atualização");
        }

        addressDAO.update(address);
    }

    public void deleteAddress(Integer addressId) throws SQLException {
        if (addressId == null || addressId <= 0) {
            throw new IllegalArgumentException("ID do endereço inválido");
        }

        addressDAO.delete(addressId);
    }

    public Optional<Address> getAddressById(Integer addressId) throws SQLException {
        if (addressId == null || addressId <= 0) {
            throw new IllegalArgumentException("ID do endereço inválido");
        }

        return addressDAO.findById(addressId);
    }

    public List<Address> getAddressesByClientId(Integer clientId) throws SQLException {
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        return addressDAO.findByClientId(clientId);
    }

    public List<Address> getAddressesByClientIdAndType(Integer clientId, AddressType addressType) 
            throws SQLException {
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        if (addressType == null) {
            throw new IllegalArgumentException("Tipo de endereço é obrigatório");
        }

        return addressDAO.findByClientIdAndType(clientId, addressType);
    }

    public Optional<Address> getPrincipalAddressByClientId(Integer clientId) throws SQLException {
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        return addressDAO.findPrincipalByClientId(clientId);
    }

    public List<Address> getOriginAddressesByClientId(Integer clientId) throws SQLException {
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        return addressDAO.findByClientIdAndType(clientId, AddressType.ORIGEM);
    }

    public List<Address> getDestinationAddressesByClientId(Integer clientId) throws SQLException {
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        return addressDAO.findByClientIdAndType(clientId, AddressType.DESTINO);
    }

    public List<Address> getRegistrationAddressesByClientId(Integer clientId) throws SQLException {
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        return addressDAO.findByClientIdAndType(clientId, AddressType.CADASTRAL);
    }

    public void setPrincipalAddress(Integer clientId, Integer addressId) throws SQLException, IllegalArgumentException {
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        if (addressId == null || addressId <= 0) {
            throw new IllegalArgumentException("ID do endereço inválido");
        }

        Optional<Address> addressOptional = addressDAO.findById(addressId);
        Address address = addressOptional.orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));

        if (!address.getClientId().equals(clientId)) {
            throw new IllegalArgumentException("Endereço não pertence ao cliente especificado");
        }

        List<Address> allAddresses = addressDAO.findByClientId(clientId);
        for (Address addr : allAddresses) {
            addr.setIsPrincipal(addr.getId().equals(addressId));
            addressDAO.update(addr);
        }
    }

    private void validateAddress(Address address) {
        if (address.getClientId() == null || address.getClientId() <= 0) {
            throw new IllegalArgumentException("ID do cliente é obrigatório");
        }

        if (address.getAddressType() == null) {
            throw new IllegalArgumentException("Tipo de endereço é obrigatório");
        }

        if (address.getStreet() == null || address.getStreet().trim().isEmpty()) {
            throw new IllegalArgumentException("Logradouro é obrigatório");
        }

        if (address.getNumber() == null || address.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Número é obrigatório");
        }

        if (address.getNeighborhood() == null || address.getNeighborhood().trim().isEmpty()) {
            throw new IllegalArgumentException("Bairro é obrigatório");
        }

        if (address.getCity() == null || address.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade é obrigatória");
        }

        if (address.getState() == null || address.getState().trim().isEmpty()) {
            throw new IllegalArgumentException("Estado é obrigatório");
        }

        if (!Validator.isValidState(address.getState())) {
            throw new IllegalArgumentException("Estado deve ter 2 caracteres (UF)");
        }

        if (address.getZipCode() == null || address.getZipCode().trim().isEmpty()) {
            throw new IllegalArgumentException("CEP é obrigatório");
        }

        if (!Validator.isValidZipCode(address.getZipCode())) {
            throw new IllegalArgumentException("CEP inválido. Formato: 12345-678 ou 12345678");
        }
    }
}
