package br.com.consultas.cadastromedicos.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErroAoAcessarRepoExceptionTest {
    @Test
    void deveRetornarCodigoCorreto() {
        ErroAoAcessarRepoException exception = new ErroAoAcessarRepoException();
        assertThat(exception.getCode()).isEqualTo("MEDICOS.erroAcessarRepositorio");
    }

    @Test
    void deveRetornarMensagemCorreta() {
        ErroAoAcessarRepoException exception = new ErroAoAcessarRepoException();
        assertThat(exception.getMessage()).isEqualTo("Erro ao acessar repositório.");
    }

    @Test
    void deveRetornarHttpStatusCorreto() {
        ErroAoAcessarRepoException exception = new ErroAoAcessarRepoException();
        assertThat(exception.getHttpStatus()).isEqualTo(500);
    }
}
