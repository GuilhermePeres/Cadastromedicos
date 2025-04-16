package br.com.consultas.cadastromedicos.gateway;

import br.com.consultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.consultas.cadastromedicos.domain.Medico;

import java.util.List;

public interface MedicoGateway {
    MedicoResponseDTO cadastrarMedico(Medico medico);
    MedicoResponseDTO atualizarMedico(Long id, Medico medicoAtualizado);
    void removerMedico(Long id);
    List<MedicoResponseDTO> buscarMedicos(String especialidade, String cidade);
}
