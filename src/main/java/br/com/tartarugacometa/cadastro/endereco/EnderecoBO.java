package br.com.tartarugacometa.cadastro.endereco;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import br.com.tartarugacometa.exception.CadastroException;
import br.com.tartarugacometa.util.Conexao;
import br.com.tartarugacometa.util.DateFormatter;

public class EnderecoBO {
    private final EnderecoDAO enderecoDAO;

    public EnderecoBO() {
        this.enderecoDAO = new EnderecoDAO();
    }

    public EnderecoBO(EnderecoDAO enderecoDAO) {
        this.enderecoDAO = enderecoDAO;
    }

    public void salvar(Endereco endereco) throws CadastroException {
        validar(endereco);
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                if (endereco.getId() == null) {
                    enderecoDAO.inserir(conexao, endereco);
                } else {
                    enderecoDAO.atualizar(conexao, endereco);
                }
                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw new CadastroException("Não foi possível salvar o endereço.", e);
            }
        } catch (SQLException e) {
            throw new CadastroException("Falha de conexão com o banco.", e);
        }
    }

    public Optional<Endereco> buscarPorId(Integer id) throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            Optional<Endereco> endereco = enderecoDAO.buscarPorId(conexao, id);
            endereco.ifPresent(this::enriquecer);
            return endereco;
        } catch (SQLException e) {
            throw new CadastroException("Falha ao buscar endereço.", e);
        }
    }

    public void excluir(Integer id) throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                enderecoDAO.excluir(conexao, id);
                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw new CadastroException("Não foi possível excluir o endereço.", e);
            }
        } catch (SQLException e) {
            throw new CadastroException("Falha de conexão com o banco.", e);
        }
    }

    public List<Endereco> listarTodos() throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            List<Endereco> enderecos = enderecoDAO.buscarTodos(conexao);
            enderecos.forEach(this::enriquecer);
            return enderecos;
        } catch (SQLException e) {
            throw new CadastroException("Falha ao listar endereços.", e);
        }
    }

    public List<Endereco> listarPorClienteId(Integer clienteId) throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            List<Endereco> enderecos = enderecoDAO.buscarPorClienteId(conexao, clienteId);
            enderecos.forEach(this::enriquecer);
            return enderecos;
        } catch (SQLException e) {
            throw new CadastroException("Falha ao listar endereços do cliente.", e);
        }
    }

    public void definirEnderecoPrincipal(Integer clienteId, Integer enderecoId) throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                enderecoDAO.definirEnderecoPrincipal(conexao, clienteId, enderecoId);
                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw new CadastroException("Não foi possível definir o endereço principal.", e);
            }
        } catch (SQLException e) {
            throw new CadastroException("Falha de conexão com o banco.", e);
        }
    }

    public Endereco addAddress(Endereco endereco) throws SQLException {
        try {
            salvar(endereco);
            return endereco;
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public void setPrincipalAddress(Integer clientId, Integer addressId) throws SQLException {
        try {
            definirEnderecoPrincipal(clientId, addressId);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public Endereco createAddress(Endereco endereco) throws SQLException {
        try {
            salvar(endereco);
            return endereco;
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public Optional<Endereco> getAddressById(Integer id) throws SQLException {
        try {
            return buscarPorId(id);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public void updateAddress(Endereco endereco) throws SQLException {
        try {
            salvar(endereco);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public void deleteAddress(Integer id) throws SQLException {
        try {
            excluir(id);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public List<Endereco> getAllAddresses() throws SQLException {
        try {
            return listarTodos();
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public List<Endereco> getAddressesByClientId(Integer clientId) throws SQLException {
        try {
            return listarPorClienteId(clientId);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public void setMainAddress(Integer clientId, Integer addressId) throws SQLException {
        try {
            definirEnderecoPrincipal(clientId, addressId);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    private void validar(Endereco endereco) throws CadastroException {
        if (endereco.getClientId() == null) {
            throw new CadastroException("ID do cliente é obrigatório.");
        }
        if (endereco.getAddressType() == null) {
            throw new CadastroException("Tipo de endereço é obrigatório.");
        }
        if (endereco.getStreet() == null || endereco.getStreet().trim().isEmpty()) {
            throw new CadastroException("Rua é obrigatória.");
        }
        if (endereco.getNumber() == null || endereco.getNumber().trim().isEmpty()) {
            throw new CadastroException("Número é obrigatório.");
        }
        if (endereco.getNeighborhood() == null || endereco.getNeighborhood().trim().isEmpty()) {
            throw new CadastroException("Bairro é obrigatório.");
        }
        if (endereco.getCity() == null || endereco.getCity().trim().isEmpty()) {
            throw new CadastroException("Cidade é obrigatória.");
        }
        if (endereco.getState() == null || endereco.getState().trim().isEmpty()) {
            throw new CadastroException("Estado é obrigatório.");
        }
        if (endereco.getZipCode() == null || endereco.getZipCode().trim().isEmpty()) {
            throw new CadastroException("CEP é obrigatório.");
        }
        if (endereco.getCountry() == null || endereco.getCountry().trim().isEmpty()) {
            throw new CadastroException("País é obrigatório.");
        }
    }

    private void enriquecer(Endereco endereco) {
        if (endereco == null) return;

        String complemento = (endereco.getComplement() != null && !endereco.getComplement().isBlank())
                ? " (" + endereco.getComplement() + ")"
                : "";

        String enderecoCompleto = String.format(
            "%s, %s%s - %s, %s - %s, %s - CEP: %s",
            seguro(endereco.getStreet()),
            seguro(endereco.getNumber()),
            complemento,
            seguro(endereco.getNeighborhood()),
            seguro(endereco.getCity()),
            seguro(endereco.getState()),
            seguro(endereco.getCountry()),
            seguro(endereco.getZipCode())
        );

        endereco.setFormattedAddress(enderecoCompleto);

        if (endereco.getCreatedAt() != null) {
            endereco.setFormattedCreatedAt(DateFormatter.formatLocalDateTime(endereco.getCreatedAt()));
        }

        if (endereco.getUpdatedAt() != null) {
            endereco.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(endereco.getUpdatedAt()));
        }
    }

    private String seguro(String valor) {
        return valor == null ? "" : valor;
    }
}
