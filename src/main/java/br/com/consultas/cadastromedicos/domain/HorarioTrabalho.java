package br.com.consultas.cadastromedicos.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HorarioTrabalho {
    private DiaDaSemanaEnum diaDaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
}
