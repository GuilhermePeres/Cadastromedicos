package br.com.agendamentoconsultas.cadastromedicos.exception;

public class MedicoNaoCadastradoException extends RuntimeException{
    private static final String CODE = "MEDICOS.medicoNaoCadastradoException";
    private static final String MESSAGE = "Médico não cadastrado.";
    private static final Integer HTTPSTATUS = 404;

    public int getHttpStatus() {
        return HTTPSTATUS;
    }

    public String getCode(){
        return CODE;
    }

    @Override
    public String getMessage() { return MESSAGE; }
}
