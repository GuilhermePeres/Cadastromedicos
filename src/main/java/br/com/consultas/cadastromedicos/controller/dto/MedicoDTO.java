package br.com.consultas.cadastromedicos.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MedicoDTO {
    @NotEmpty
    private String nome;
    @NotEmpty
    private String especialidade;
    @NotEmpty
    private String cidade;
    @NotEmpty
    private List<HorarioTrabalhoDTO> horariosTrabalho;
}
