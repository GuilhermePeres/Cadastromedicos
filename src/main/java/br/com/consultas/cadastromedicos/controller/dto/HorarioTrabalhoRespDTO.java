package br.com.consultas.cadastromedicos.controller.dto;

import br.com.consultas.cadastromedicos.domain.DiaDaSemanaEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HorarioTrabalhoRespDTO {
    private DiaDaSemanaEnum diaDaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
}
