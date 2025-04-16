package br.com.agendamentoconsultas.cadastromedicos.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MedicoResponseDTO {
    private Long id;
    private String nome;
    private String especialidade;
    private String cidade;
    private List<HorarioTrabalhoRespDTO> horariosTrabalho;
}
