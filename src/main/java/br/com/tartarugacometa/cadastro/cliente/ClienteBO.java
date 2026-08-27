package br.com.tartarugacometa.cadastro.cliente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import br.com.tartarugacometa.enums.TipoPessoa;
import br.com.tartarugacometa.exception.CadastroException;
import br.com.tartarugacometa.util.Conexao;
import br.com.tartarugacometa.util.DateFormatter;
import br.com.tartarugacometa.util.ValidadorCpf;
import br.com.tartarugacometa.util.ValidadorCnpj;
import br.com.tartarugacometa.util.ValidadorEmail;
import br.com.tartarugacometa.util.Validator;

public class ClienteBO {
    private final ClienteDAO clienteDAO;

    public ClienteBO() {
        this.clienteDAO = new ClienteDAO();
    }

    public ClienteBO(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void salvar(Cliente cliente) throws CadastroException {
        validar(cliente);
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                if (clienteDAO.existeDocumento(conexao, cliente.getDocument(), cliente.getId())) {
                    throw new CadastroException("Já existe cliente com este documento.");
                }
                if (cliente.getId() == null) {
                    clienteDAO.inserir(conexao, cliente);
                } else {
                    clienteDAO.atualizar(conexao, cliente);
                }
                conexao.commit();
            } catch (SQLException | CadastroException e) {
                conexao.rollback();
                throw e instanceof CadastroException ce ? ce
                        : new CadastroException("Não foi possível salvar o cliente.", e);
            }
        } catch (SQLException e) {
            throw new CadastroException("Falha de conexão com o banco.", e);
        }
    }

    public Optional<Cliente> buscarPorId(Integer id) throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            Optional<Cliente> cliente = clienteDAO.buscarPorId(conexao, id);
            cliente.ifPresent(this::enriquecer);
            return cliente;
        } catch (SQLException e) {
            throw new CadastroException("Falha ao buscar cliente.", e);
        }
    }

    public void excluir(Integer id) throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                clienteDAO.excluir(conexao, id);
                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw new CadastroException("Não foi possível excluir o cliente.", e);
            }
        } catch (SQLException e) {
            throw new CadastroException("Falha de conexão com o banco.", e);
        }
    }

    public List<Cliente> listarTodos() throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            List<Cliente> clientes = clienteDAO.buscarTodos(conexao);
            clientes.forEach(this::enriquecer);
            return clientes;
        } catch (SQLException e) {
            throw new CadastroException("Falha ao listar clientes.", e);
        }
    }

    public List<Cliente> pesquisar(String termo) throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            List<Cliente> clientes = clienteDAO.pesquisar(conexao, termo);
            clientes.forEach(this::enriquecer);
            return clientes;
        } catch (SQLException e) {
            throw new CadastroException("Falha na pesquisa de clientes.", e);
        }
    }

    public Cliente createClient(Cliente client) throws SQLException {
        try {
            salvar(client);
            return client;
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public Optional<Cliente> getClientById(Integer id) throws SQLException {
        try {
            return buscarPorId(id);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public void updateClient(Cliente client) throws SQLException {
        try {
            salvar(client);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public void deleteClient(Integer id) throws SQLException {
        try {
            excluir(id);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public List<Cliente> getAllClients() throws SQLException {
        try {
            return listarTodos();
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public List<Cliente> search(String termo) throws SQLException {
        try {
            return pesquisar(termo);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    private void validar(Cliente cliente) throws CadastroException {
        validarTipo(cliente);
        validarNome(cliente);
        validarDocumento(cliente);
        validarEmail(cliente);
        validarCanalContato(cliente);
    }

    private void validarTipo(Cliente cliente) throws CadastroException {
        if (cliente.getPersonType() == null) {
            throw new CadastroException("Tipo de pessoa é obrigatório.");
        }
    }

    private void validarNome(Cliente cliente) throws CadastroException {
        if (cliente.getName() == null || cliente.getName().trim().isEmpty()) {
            throw new CadastroException("Nome do cliente é obrigatório.");
        }
        if (cliente.getName().trim().length() < 3) {
            throw new CadastroException("Nome do cliente deve ter no mínimo 3 caracteres.");
        }
    }

    private void validarDocumento(Cliente cliente) throws CadastroException {
        if (cliente.getDocument() == null || cliente.getDocument().trim().isEmpty()) {
            throw new CadastroException("Documento do cliente é obrigatório.");
        }

        if (cliente.getPersonType() == TipoPessoa.FISICA) {
            if (!ValidadorCpf.valido(cliente.getDocument())) {
                throw new CadastroException("CPF inválido.");
            }
        } else if (cliente.getPersonType() == TipoPessoa.JURIDICA) {
            if (!ValidadorCnpj.valido(cliente.getDocument())) {
                throw new CadastroException("CNPJ inválido.");
            }
        }
    }

    private void validarEmail(Cliente cliente) throws CadastroException {
        if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
            if (!ValidadorEmail.valido(cliente.getEmail())) {
                throw new CadastroException("Email inválido.");
            }
        }
    }

    private void validarCanalContato(Cliente cliente) throws CadastroException {
        String email = cliente.getEmail();
        String phone = cliente.getPhone();

        boolean temEmail = email != null && !email.trim().isEmpty();
        boolean temTelefone = phone != null && !phone.trim().isEmpty();

        if (!temEmail && !temTelefone) {
            throw new CadastroException("Cliente deve ter ao menos um canal de contato: email ou telefone.");
        }

        if (temTelefone && !Validator.isValidPhone(phone)) {
            throw new CadastroException("Telefone inválido.");
        }
    }

    private void enriquecer(Cliente cliente) {
        if (cliente == null) return;

        if (cliente.getCreatedAt() != null) {
            cliente.setFormattedCreatedAt(DateFormatter.formatLocalDateTime(cliente.getCreatedAt()));
        }
        if (cliente.getUpdatedAt() != null) {
            cliente.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(cliente.getUpdatedAt()));
        }
        if (cliente.getPersonType() != null) {
            cliente.setFormattedPersonType(cliente.getPersonType().getRotulo());
        }
    }
}
