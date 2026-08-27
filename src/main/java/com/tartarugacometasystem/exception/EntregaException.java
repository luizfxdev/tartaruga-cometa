package com.tartarugacometasystem.exception;

public class EntregaException extends NegocioException {
    public EntregaException(String mensagem) {
        super(mensagem);
    }

    public EntregaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
