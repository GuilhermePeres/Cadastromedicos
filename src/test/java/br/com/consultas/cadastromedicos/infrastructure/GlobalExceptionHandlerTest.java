package br.com.consultas.cadastromedicos.infrastructure;

import br.com.consultas.cadastromedicos.controller.dto.ExceptionResponseDTO;
import br.com.consultas.cadastromedicos.exception.ErroAoAcessarRepoException;
import br.com.consultas.cadastromedicos.exception.MedicoNaoCadastradoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private ErroAoAcessarRepoException erroAoAcessarRepoException;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MedicoNaoCadastradoException medicoNaoCadastradoException;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveTratarErroAoAcessarRepoException() {
        //Arrange
        String codigoEsperado = "MEDICOS.erroAcessarRepositorio";
        String mensagemEsperada = "Erro ao acessar repositório.";
        HttpStatus statusEsperado = HttpStatus.INTERNAL_SERVER_ERROR;

        when(erroAoAcessarRepoException.getCode()).thenReturn(codigoEsperado);
        when(erroAoAcessarRepoException.getMessage()).thenReturn(mensagemEsperada);
        when(erroAoAcessarRepoException.getHttpStatus()).thenReturn(statusEsperado.value());

        //Act
        ResponseEntity<ExceptionResponseDTO> response = globalExceptionHandler.tratarErroAoAcessarRepoException(erroAoAcessarRepoException);

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(statusEsperado);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().code()).isEqualTo(codigoEsperado);
        assertThat(response.getBody().message()).isEqualTo(mensagemEsperada);
        assertThat(response.getHeaders()).isEqualTo(new HttpHeaders());
    }

    @Test
    void deveTratarErroMethodArgumentNotValidExceptionException() {
        //Arrange
        FieldError fieldError1 = new FieldError("name", "field", "message");
        FieldError fieldError2 = new FieldError("name2", "field2", "message2");

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        when(methodArgumentNotValidException.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

        //Act
        ResponseEntity<Object> responseEntity = globalExceptionHandler.tratarErroMethodArgumentNotValidExceptionException(methodArgumentNotValidException);
        Map<String, String> erros = (Map<String, String>) responseEntity.getBody();

        //Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(erros)
                .hasSize(2)
                .containsEntry("field", "message")
                .containsEntry("field2", "message2");
    }

    @Test
    void deveTratarErroMedicoNaoCadastradoException() {
        //Arrange
        String codigoEsperado = "MEDICOS.medicoNaoCadastradoException";
        String mensagemEsperada = "Médico não cadastrado.";
        HttpStatus statusEsperado = HttpStatus.NOT_FOUND;

        when(medicoNaoCadastradoException.getCode()).thenReturn(codigoEsperado);
        when(medicoNaoCadastradoException.getMessage()).thenReturn(mensagemEsperada);
        when(medicoNaoCadastradoException.getHttpStatus()).thenReturn(statusEsperado.value());

        //Act
        ResponseEntity<ExceptionResponseDTO> response = globalExceptionHandler.tratarErroMedicoNaoCadastradoException(medicoNaoCadastradoException);

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(statusEsperado);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().code()).isEqualTo(codigoEsperado);
        assertThat(response.getBody().message()).isEqualTo(mensagemEsperada);
        assertThat(response.getHeaders()).isEqualTo(new HttpHeaders());
    }
}
