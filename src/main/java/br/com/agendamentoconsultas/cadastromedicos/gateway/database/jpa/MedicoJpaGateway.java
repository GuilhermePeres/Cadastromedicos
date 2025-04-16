package br.com.agendamentoconsultas.cadastromedicos.gateway.database.jpa;

import br.com.agendamentoconsultas.cadastromedicos.controller.dto.HorarioTrabalhoRespDTO;
import br.com.agendamentoconsultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.agendamentoconsultas.cadastromedicos.domain.HorarioTrabalho;
import br.com.agendamentoconsultas.cadastromedicos.domain.Medico;
import br.com.agendamentoconsultas.cadastromedicos.exception.ErroAoAcessarRepositorioException;
import br.com.agendamentoconsultas.cadastromedicos.exception.MedicoNaoCadastradoException;
import br.com.agendamentoconsultas.cadastromedicos.gateway.MedicoGateway;
import br.com.agendamentoconsultas.cadastromedicos.gateway.database.jpa.entity.HorarioTrabalhoEntity;
import br.com.agendamentoconsultas.cadastromedicos.gateway.database.jpa.entity.MedicoEntity;
import br.com.agendamentoconsultas.cadastromedicos.gateway.database.jpa.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MedicoJpaGateway implements MedicoGateway {

    private final MedicoRepository medicoRepository;

    @Autowired
    public MedicoJpaGateway(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @Override
    public MedicoResponseDTO cadastrarMedico(Medico medico) {
        try{
            MedicoEntity medicoEntity = medicoToEntity(medico);

            MedicoEntity medicoEntityResponse = medicoRepository.save(medicoEntity);

            return medicoEntityToDto(medicoEntityResponse);

        }catch (Exception e){
            throw new ErroAoAcessarRepositorioException();
        }
    }

    @Transactional
    @Override
    public MedicoResponseDTO atualizarMedico(Long id, Medico medicoAtualizado) {
        Optional<MedicoEntity> medicoEntityOptional = medicoRepository.findById(id);

        if(medicoEntityOptional.isPresent()){
            MedicoEntity medicoEntity = atualizaMedicoEntity(medicoAtualizado, medicoEntityOptional.get());

            if(medicoAtualizado.getHorariosTrabalho() != null && !medicoAtualizado.getHorariosTrabalho().isEmpty()){
                medicoEntity.getHorariosTrabalhoEntity().clear();

                for(HorarioTrabalho ht : medicoAtualizado.getHorariosTrabalho()){
                    HorarioTrabalhoEntity horarioTrabalhoEntity = new HorarioTrabalhoEntity();
                    horarioTrabalhoEntity.setDiaDaSemana(ht.getDiaDaSemana());
                    horarioTrabalhoEntity.setHoraInicio(ht.getHoraInicio());
                    horarioTrabalhoEntity.setHoraFim(ht.getHoraFim());
                    horarioTrabalhoEntity.setMedicoEntity(medicoEntity);
                    medicoEntity.getHorariosTrabalhoEntity().add(horarioTrabalhoEntity);
                }
            }

            MedicoEntity medicoEntityRetorno = medicoRepository.save(medicoEntity);

            return medicoEntityToDto(medicoEntityRetorno);
        }else{
            throw new MedicoNaoCadastradoException();
        }
    }

    @Override
    public void removerMedico(Long id) {
        Optional<MedicoEntity> medicoEntityOptional = medicoRepository.findById(id);

        if(medicoEntityOptional.isPresent()){
            medicoRepository.delete(medicoEntityOptional.get());
        }else{
            throw new MedicoNaoCadastradoException();
        }
    }

    @Override
    public List<MedicoResponseDTO> buscarMedicos(String especialidade, String cidade) {
        List<MedicoEntity> medicoEntityList;

        if(StringUtils.hasLength(especialidade) && StringUtils.hasLength(cidade)){
            medicoEntityList = medicoRepository.findByEspecialidadeIgnoreCaseAndCidadeIgnoreCase(especialidade, cidade);

            return medicoEntityList.stream().map(this::medicoEntityToDto).toList();

        } else if (StringUtils.hasLength(especialidade)) {
            medicoEntityList = medicoRepository.findByEspecialidadeIgnoreCase(especialidade);

            return medicoEntityList.stream().map(this::medicoEntityToDto).toList();

        } else if (StringUtils.hasLength(cidade)) {
            medicoEntityList = medicoRepository.findByCidadeIgnoreCase(cidade);

            return medicoEntityList.stream().map(this::medicoEntityToDto).toList();
        }

        medicoEntityList = medicoRepository.findAll();

        return medicoEntityList.stream().map(this::medicoEntityToDto).toList();
    }

    private MedicoEntity atualizaMedicoEntity(Medico medicoAtualizado, MedicoEntity medicoEntity) {
        if(StringUtils.hasLength(medicoAtualizado.getNome())){
            medicoEntity.setNome(medicoAtualizado.getNome());
        }
        if(StringUtils.hasLength(medicoAtualizado.getEspecialidade())){
            medicoEntity.setEspecialidade(medicoAtualizado.getEspecialidade());
        }
        if(StringUtils.hasLength(medicoAtualizado.getCidade())){
            medicoEntity.setCidade(medicoAtualizado.getCidade());
        }
        return medicoEntity;
    }

    private MedicoEntity medicoToEntity(Medico medico){
        MedicoEntity medicoEntity = new MedicoEntity();
        medicoEntity.setNome(medico.getNome());
        medicoEntity.setEspecialidade(medico.getEspecialidade());
        medicoEntity.setCidade(medico.getCidade());

        List<HorarioTrabalhoEntity> horariosTrabalhoEntity = new ArrayList<>();

        for(HorarioTrabalho ht : medico.getHorariosTrabalho()){
            HorarioTrabalhoEntity horarioTrabalhoEntity = new HorarioTrabalhoEntity();
            horarioTrabalhoEntity.setDiaDaSemana(ht.getDiaDaSemana());
            horarioTrabalhoEntity.setHoraInicio(ht.getHoraInicio());
            horarioTrabalhoEntity.setHoraFim(ht.getHoraFim());
            horarioTrabalhoEntity.setMedicoEntity(medicoEntity);
            horariosTrabalhoEntity.add(horarioTrabalhoEntity);
        }

        medicoEntity.setHorariosTrabalhoEntity(horariosTrabalhoEntity);

        return medicoEntity;
    }

    private MedicoResponseDTO medicoEntityToDto(MedicoEntity medicoEntity){
        List<HorarioTrabalhoRespDTO> horariosTrabalhoResponseDTOS = new ArrayList<>();

        for(HorarioTrabalhoEntity hte : medicoEntity.getHorariosTrabalhoEntity()){
            HorarioTrabalhoRespDTO horarioTrabalhoResponseDTO = new HorarioTrabalhoRespDTO(
                    hte.getDiaDaSemana(),
                    hte.getHoraInicio(),
                    hte.getHoraFim()
            );
            horariosTrabalhoResponseDTOS.add(horarioTrabalhoResponseDTO);
        }

        return new MedicoResponseDTO(
                medicoEntity.getId(),
                medicoEntity.getNome(),
                medicoEntity.getEspecialidade(),
                medicoEntity.getCidade(),
                horariosTrabalhoResponseDTOS
        );
    }
}
