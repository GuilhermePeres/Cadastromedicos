package br.com.consultas.cadastromedicos.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MedicoNaoCadastradoExceptionTest {
    @Test
    void devePermitirRetornarCodigo() {
        MedicoNaoCadastradoException exception = new MedicoNaoCadastradoException();
        assertThat(exception.getCode()).isEqualTo("MEDICOS.medicoNaoCadastradoException");
    }

    @Test
    void devePermitirRetornarMensagem() {
        MedicoNaoCadastradoException exception = new MedicoNaoCadastradoException();
        assertThat(exception.getMessage()).isEqualTo("Médico não cadastrado.");
    }

    @Test
    void devePermitirRetornarHttpStatus() {
        MedicoNaoCadastradoException exception = new MedicoNaoCadastradoException();
        assertThat(exception.getHttpStatus()).isEqualTo(404);
    }
}
