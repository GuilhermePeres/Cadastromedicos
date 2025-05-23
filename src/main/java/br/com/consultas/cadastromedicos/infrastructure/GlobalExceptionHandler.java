package br.com.consultas.cadastromedicos.infrastructure;

import br.com.consultas.cadastromedicos.controller.dto.ExceptionResponseDTO;
import br.com.consultas.cadastromedicos.exception.ErroAoAcessarRepoException;
import br.com.consultas.cadastromedicos.exception.MedicoNaoCadastradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErroAoAcessarRepoException.class)
    public ResponseEntity<ExceptionResponseDTO> tratarErroAoAcessarRepoException(ErroAoAcessarRepoException ex) {
        final ExceptionResponseDTO exceptionJson = new ExceptionResponseDTO(ex.getCode(), ex.getMessage());

        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(exceptionJson, new HttpHeaders(), ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Object> tratarErroMethodArgumentNotValidExceptionException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);

        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erro ->
                erros.put(erro.getField(), erro.getDefaultMessage())
        );

        return ResponseEntity.status(ex.getStatusCode()).body(erros);
    }

    @ExceptionHandler(MedicoNaoCadastradoException.class)
    public ResponseEntity<ExceptionResponseDTO> tratarErroMedicoNaoCadastradoException(MedicoNaoCadastradoException ex) {
        final ExceptionResponseDTO exceptionJson = new ExceptionResponseDTO(ex.getCode(), ex.getMessage());

        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(exceptionJson, new HttpHeaders(), ex.getHttpStatus());
    }
}
