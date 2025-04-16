package br.com.consultas.cadastromedicos.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medico {
    private String nome;
    private String especialidade;
    private String cidade;
    private List<HorarioTrabalho> horariosTrabalho;
}
