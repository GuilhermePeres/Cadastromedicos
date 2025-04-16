package br.com.agendamentoconsultas.cadastromedicos.gateway;

import br.com.agendamentoconsultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.agendamentoconsultas.cadastromedicos.domain.Medico;

import java.util.List;

public interface MedicoGateway {
    MedicoResponseDTO cadastrarMedico(Medico medico);
    MedicoResponseDTO atualizarMedico(Long id, Medico medicoAtualizado);
    void removerMedico(Long id);
    List<MedicoResponseDTO> buscarMedicos(String especialidade, String cidade);
}
