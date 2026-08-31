package br.com.tartarugacometa.exception;

public class NegocioException extends Exception {
    public NegocioException(String mensagem) {
        super(mensagem);
    }

    public NegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
