package br.com.tartarugacometa.exception;

public class FreteException extends NegocioException {
    public FreteException(String mensagem) {
        super(mensagem);
    }

    public FreteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
