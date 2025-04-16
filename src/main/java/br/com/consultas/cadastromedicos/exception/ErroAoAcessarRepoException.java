package br.com.consultas.cadastromedicos.exception;

import lombok.Getter;

@Getter
public class ErroAoAcessarRepoException extends RuntimeException{
    private static final String CODE = "MEDICOS.erroAcessarRepositorio";
    private static final String MESSAGE = "Erro ao acessar repositório.";
    private static final Integer HTTPSTATUS = 500;

    public int getHttpStatus() {
        return HTTPSTATUS;
    }

    public String getCode(){
        return CODE;
    }

    @Override
    public String getMessage() { return MESSAGE; }
}
