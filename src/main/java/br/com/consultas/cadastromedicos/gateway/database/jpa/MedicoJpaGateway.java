package br.com.consultas.cadastromedicos.gateway.database.jpa;

import br.com.consultas.cadastromedicos.controller.dto.HorarioTrabalhoRespDTO;
import br.com.consultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.consultas.cadastromedicos.domain.HorarioTrabalho;
import br.com.consultas.cadastromedicos.domain.Medico;
import br.com.consultas.cadastromedicos.exception.ErroAoAcessarRepoException;
import br.com.consultas.cadastromedicos.exception.MedicoNaoCadastradoException;
import br.com.consultas.cadastromedicos.gateway.MedicoGateway;
import br.com.consultas.cadastromedicos.gateway.database.jpa.entity.HoraTrabalhoEntity;
import br.com.consultas.cadastromedicos.gateway.database.jpa.entity.MedicoEntity;
import br.com.consultas.cadastromedicos.gateway.database.jpa.repository.MedicoRepository;
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
            throw new ErroAoAcessarRepoException();
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
                    HoraTrabalhoEntity horaTrabalhoEntity = new HoraTrabalhoEntity();
                    horaTrabalhoEntity.setDiaDaSemana(ht.getDiaDaSemana());
                    horaTrabalhoEntity.setHoraInicio(ht.getHoraInicio());
                    horaTrabalhoEntity.setHoraFim(ht.getHoraFim());
                    horaTrabalhoEntity.setMedicoEntity(medicoEntity);
                    medicoEntity.getHorariosTrabalhoEntity().add(horaTrabalhoEntity);
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

        List<HoraTrabalhoEntity> horariosTrabalhoEntity = new ArrayList<>();

        for(HorarioTrabalho ht : medico.getHorariosTrabalho()){
            HoraTrabalhoEntity horaTrabalhoEntity = new HoraTrabalhoEntity();
            horaTrabalhoEntity.setDiaDaSemana(ht.getDiaDaSemana());
            horaTrabalhoEntity.setHoraInicio(ht.getHoraInicio());
            horaTrabalhoEntity.setHoraFim(ht.getHoraFim());
            horaTrabalhoEntity.setMedicoEntity(medicoEntity);
            horariosTrabalhoEntity.add(horaTrabalhoEntity);
        }

        medicoEntity.setHorariosTrabalhoEntity(horariosTrabalhoEntity);

        return medicoEntity;
    }

    private MedicoResponseDTO medicoEntityToDto(MedicoEntity medicoEntity){
        List<HorarioTrabalhoRespDTO> horariosTrabalhoResponseDTOS = new ArrayList<>();

        for(HoraTrabalhoEntity hte : medicoEntity.getHorariosTrabalhoEntity()){
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
