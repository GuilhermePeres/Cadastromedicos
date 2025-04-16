package br.com.agendamentoconsultas.cadastromedicos.controller;

import br.com.agendamentoconsultas.cadastromedicos.controller.dto.HorarioTrabalhoDTO;
import br.com.agendamentoconsultas.cadastromedicos.controller.dto.MedicoDTO;
import br.com.agendamentoconsultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.agendamentoconsultas.cadastromedicos.domain.HorarioTrabalho;
import br.com.agendamentoconsultas.cadastromedicos.domain.Medico;
import br.com.agendamentoconsultas.cadastromedicos.usecase.MedicoUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoUseCase medicoUseCase;

    @Autowired
    public MedicoController(MedicoUseCase medicoUseCase) {
        this.medicoUseCase = medicoUseCase;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<MedicoResponseDTO> cadastrarMedico(@Valid @RequestBody MedicoDTO medicoDTO){
        Medico medico = medicoDTOtoDomain(medicoDTO);

        MedicoResponseDTO medicoResponseDTO = medicoUseCase.cadastrarMedico(medico);

        return ResponseEntity.ok(medicoResponseDTO);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<MedicoResponseDTO> atualizarMedico(@PathVariable Long id, @RequestBody MedicoDTO medicoDTO){
        Medico medicoAtualizado = medicoDTOtoDomain(medicoDTO);

        MedicoResponseDTO medicoResponseDTO = medicoUseCase.atualizarMedico(id, medicoAtualizado);

        return ResponseEntity.ok(medicoResponseDTO);
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<String> removerMedico(@PathVariable Long id){
        medicoUseCase.removerMedico(id);

        return ResponseEntity.ok("Médico removido com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> buscarMedico(
            @RequestParam(value = "especialidade", required = false) String especialidade,
            @RequestParam(value = "cidade", required = false) String cidade){
        List<MedicoResponseDTO> medicoResponseDTOs = medicoUseCase.buscarMedicos(especialidade, cidade);

        if (medicoResponseDTOs.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(medicoResponseDTOs);
    }

    private Medico medicoDTOtoDomain(MedicoDTO medicoDTO){
        List<HorarioTrabalho> horariosTrabalho = new ArrayList<>();

        for(HorarioTrabalhoDTO dto : medicoDTO.getHorariosTrabalho()){
            HorarioTrabalho horarioTrabalho = new HorarioTrabalho();
            horarioTrabalho.setDiaDaSemana(dto.getDiaDaSemana());
            horarioTrabalho.setHoraInicio(dto.getHoraInicio());
            horarioTrabalho.setHoraFim(dto.getHoraFim());
            horariosTrabalho.add(horarioTrabalho);
        }

        return new Medico(
                medicoDTO.getNome(),
                medicoDTO.getEspecialidade(),
                medicoDTO.getCidade(),
                horariosTrabalho
        );
    }
}
