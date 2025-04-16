package br.com.consultas.cadastromedicos.usecase;

import br.com.consultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.consultas.cadastromedicos.domain.Medico;
import br.com.consultas.cadastromedicos.gateway.MedicoGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoUseCase {

    private final MedicoGateway medicoGateway;

    @Autowired
    public MedicoUseCase(MedicoGateway medicoGateway) {
        this.medicoGateway = medicoGateway;
    }

    public MedicoResponseDTO cadastrarMedico(Medico medico){
        return medicoGateway.cadastrarMedico(medico);
    }

    public MedicoResponseDTO atualizarMedico(Long id, Medico medicoAtualizado) {
        return medicoGateway.atualizarMedico(id, medicoAtualizado);
    }

    public void removerMedico(Long id) {
        medicoGateway.removerMedico(id);
    }

    public List<MedicoResponseDTO> buscarMedicos(String especialidade, String cidade) {
        return medicoGateway.buscarMedicos(especialidade, cidade);
    }
}
