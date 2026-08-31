package br.com.tartarugacometa.exception;

public class CadastroException extends NegocioException {
    public CadastroException(String mensagem) {
        super(mensagem);
    }

    public CadastroException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
