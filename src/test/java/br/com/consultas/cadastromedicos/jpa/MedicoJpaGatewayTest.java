package br.com.consultas.cadastromedicos.jpa;

import br.com.consultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.consultas.cadastromedicos.domain.Medico;
import br.com.consultas.cadastromedicos.exception.ErroAoAcessarRepoException;
import br.com.consultas.cadastromedicos.exception.MedicoNaoCadastradoException;
import br.com.consultas.cadastromedicos.gateway.database.jpa.MedicoJpaGateway;
import br.com.consultas.cadastromedicos.gateway.database.jpa.entity.MedicoEntity;
import br.com.consultas.cadastromedicos.gateway.database.jpa.repository.MedicoRepository;
import br.com.consultas.cadastromedicos.utils.MedicoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MedicoJpaGatewayTest {
    @InjectMocks
    private MedicoJpaGateway medicoJpaGateway;

    @Mock
    private MedicoRepository medicoRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCadastrarMedico() {
        //Arrange
        Medico medico = MedicoHelper.gerarNovoMedicoComHorarios();
        MedicoEntity medicoEntity = MedicoHelper.gerarNovoMedicoEntityComHorarios();

        when(medicoRepository.save(any(MedicoEntity.class))).thenReturn(medicoEntity);

        //Act
        MedicoResponseDTO medicoResponseDTO = medicoJpaGateway.cadastrarMedico(medico);

        //Assert
        verify(medicoRepository, times(1)).save(any(MedicoEntity.class));
        assertThat(medicoResponseDTO).isNotNull();
        assertThat(medicoResponseDTO.getHorariosTrabalho()).hasSize(1);
        assertThat(medicoResponseDTO.getNome()).isEqualTo(medico.getNome());
        assertThat(medicoResponseDTO.getEspecialidade()).isEqualTo(medico.getEspecialidade());
        assertThat(medicoResponseDTO.getCidade()).isEqualTo(medico.getCidade());
        assertThat(medicoResponseDTO.getHorariosTrabalho().getFirst().getDiaDaSemana())
                .isEqualTo(medico.getHorariosTrabalho().getFirst().getDiaDaSemana());
        assertThat(medicoResponseDTO.getHorariosTrabalho().getFirst().getHoraInicio())
                .isEqualTo(medico.getHorariosTrabalho().getFirst().getHoraInicio());
        assertThat(medicoResponseDTO.getHorariosTrabalho().getFirst().getHoraFim())
                .isEqualTo(medico.getHorariosTrabalho().getFirst().getHoraFim());
    }

    @Test
    void deveLancarErroAoAcessarRepoExceptionAoCadastrarMedico(){
        //Arrange
        Medico medico = MedicoHelper.gerarNovoMedicoComHorarios();
        when(medicoRepository.save(any(MedicoEntity.class))).thenThrow(new RuntimeException());

        //Act Assert
        assertThatThrownBy(() -> medicoJpaGateway.cadastrarMedico(medico))
                .isInstanceOf(ErroAoAcessarRepoException.class);
    }

    @Test
    void devePermitirAtualizarMedico() {
        //Arrange
        Medico medicoAtualizado = MedicoHelper.gerarNovoMedicoComHorarios();
        Long id = 1L;

        MedicoEntity medicoEntity = MedicoHelper.gerarMedicoEntityExistenteComHorarios();
        MedicoEntity medicoEntityRetorno = MedicoHelper.gerarNovoMedicoEntityComHorarios();

        medicoAtualizado.setEspecialidade("Especialidade Atualizada");
        medicoEntityRetorno.setEspecialidade("Especialidade Atualizada");
        medicoAtualizado.setCidade("Cidade Atualizada");
        medicoEntityRetorno.setCidade("Cidade Atualizada");

        when(medicoRepository.findById(anyLong())).thenReturn(Optional.of(medicoEntity));
        when(medicoRepository.save(any(MedicoEntity.class))).thenReturn(medicoEntityRetorno);

        //Act
        MedicoResponseDTO medicoResponseDTO = medicoJpaGateway.atualizarMedico(id, medicoAtualizado);

        //Assert
        verify(medicoRepository, times(1)).findById(anyLong());
        verify(medicoRepository, times(1)).save(any(MedicoEntity.class));
        assertThat(medicoResponseDTO).isNotNull();
        assertThat(medicoResponseDTO.getHorariosTrabalho()).hasSize(1);
        assertThat(medicoResponseDTO.getNome()).isEqualTo(medicoAtualizado.getNome());
        assertThat(medicoResponseDTO.getEspecialidade()).isEqualTo(medicoAtualizado.getEspecialidade());
        assertThat(medicoResponseDTO.getCidade()).isEqualTo(medicoAtualizado.getCidade());
        assertThat(medicoResponseDTO.getHorariosTrabalho().getFirst().getDiaDaSemana())
                .isEqualTo(medicoAtualizado.getHorariosTrabalho().getFirst().getDiaDaSemana());
        assertThat(medicoResponseDTO.getHorariosTrabalho().getFirst().getHoraInicio())
                .isEqualTo(medicoAtualizado.getHorariosTrabalho().getFirst().getHoraInicio());
        assertThat(medicoResponseDTO.getHorariosTrabalho().getFirst().getHoraFim())
                .isEqualTo(medicoAtualizado.getHorariosTrabalho().getFirst().getHoraFim());
    }

    @Test
    void deveLancarMedicoNaoCadastradoExceptionAoAtualizarMedico(){
        //Arrange
        Medico medico = MedicoHelper.gerarNovoMedicoComHorarios();
        Long id = 4L;

        when(medicoRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Act Assert
        assertThatThrownBy(() -> medicoJpaGateway.atualizarMedico(id, medico))
                .isInstanceOf(MedicoNaoCadastradoException.class);
    }

    @Test
    void devePermitirRemoverMedico() {
        //Arrange
        MedicoEntity medicoEntity = MedicoHelper.gerarMedicoEntityExistenteComHorarios();
        Long id = 1L;

        when(medicoRepository.findById(anyLong())).thenReturn(Optional.of(medicoEntity));
        doNothing().when(medicoRepository).delete(any(MedicoEntity.class));

        //Act
        medicoJpaGateway.removerMedico(id);

        //Assert
        verify(medicoRepository, times(1)).findById(anyLong());
        verify(medicoRepository, times(1)).delete(any(MedicoEntity.class));
    }

    @Test
    void deveLancarMedicoNaoCadastradoExceptionAoRemoverMedico(){
        //Arrange
        Long id = 4L;

        when(medicoRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Act Assert
        assertThatThrownBy(() -> medicoJpaGateway.removerMedico(id))
                .isInstanceOf(MedicoNaoCadastradoException.class);
    }

    @Test
    void devePermitirBuscarMedicosPorEspecialidadeECidade() {
        //Arrange
        String especialidade = "Cardiologista";
        String cidade = "São Paulo";
        ArrayList<MedicoEntity> medicoEntityList = MedicoHelper.gerarListaMedicosEntityExistentesComHorarios();

        when(medicoRepository.findByEspecialidadeIgnoreCaseAndCidadeIgnoreCase(anyString(), anyString())).thenReturn(List.of(medicoEntityList.getFirst()));

        //Act
        List<MedicoResponseDTO> medicoResponseDTOList = medicoJpaGateway.buscarMedicos(especialidade, cidade);

        //Assert
        verify(medicoRepository, times(1)).findByEspecialidadeIgnoreCaseAndCidadeIgnoreCase(especialidade, cidade);
        assertThat(medicoResponseDTOList).hasSize(1);
        assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho()).hasSize(2);
        assertThat(medicoResponseDTOList.getFirst().getNome()).isEqualTo(medicoEntityList.getFirst().getNome());
        assertThat(medicoResponseDTOList.getFirst().getEspecialidade()).isEqualTo(medicoEntityList.getFirst().getEspecialidade());
        assertThat(medicoResponseDTOList.getFirst().getCidade()).isEqualTo(medicoEntityList.getFirst().getCidade());

        for (int i = 0; i < medicoResponseDTOList.getFirst().getHorariosTrabalho().size(); i++) {
            assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho().get(i).getDiaDaSemana())
                    .isEqualTo(medicoEntityList.getFirst().getHorariosTrabalhoEntity().get(i).getDiaDaSemana());

            assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho().get(i).getHoraInicio())
                    .isEqualTo(medicoEntityList.getFirst().getHorariosTrabalhoEntity().get(i).getHoraInicio());

            assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho().get(i).getHoraFim())
                    .isEqualTo(medicoEntityList.getFirst().getHorariosTrabalhoEntity().get(i).getHoraFim());
        }
    }

    @Test
    void devePermitirBuscarMedicosPorEspecialidade() {
        //Arrange
        String especialidade = "Cardiologista";
        ArrayList<MedicoEntity> medicoEntityList = MedicoHelper.gerarListaMedicosEntityExistentesComHorarios();

        when(medicoRepository.findByEspecialidadeIgnoreCase(anyString())).thenReturn(List.of(medicoEntityList.getFirst()));

        //Act
        List<MedicoResponseDTO> medicoResponseDTOList = medicoJpaGateway.buscarMedicos(especialidade, null);

        //Assert
        verify(medicoRepository, times(1)).findByEspecialidadeIgnoreCase(especialidade);
        assertThat(medicoResponseDTOList).hasSize(1);
        assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho()).hasSize(2);
        assertThat(medicoResponseDTOList.getFirst().getNome()).isEqualTo(medicoEntityList.getFirst().getNome());
        assertThat(medicoResponseDTOList.getFirst().getEspecialidade()).isEqualTo(medicoEntityList.getFirst().getEspecialidade());
        assertThat(medicoResponseDTOList.getFirst().getCidade()).isEqualTo(medicoEntityList.getFirst().getCidade());

        for (int i = 0; i < medicoResponseDTOList.getFirst().getHorariosTrabalho().size(); i++) {
            assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho().get(i).getDiaDaSemana())
                    .isEqualTo(medicoEntityList.getFirst().getHorariosTrabalhoEntity().get(i).getDiaDaSemana());

            assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho().get(i).getHoraInicio())
                    .isEqualTo(medicoEntityList.getFirst().getHorariosTrabalhoEntity().get(i).getHoraInicio());

            assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho().get(i).getHoraFim())
                    .isEqualTo(medicoEntityList.getFirst().getHorariosTrabalhoEntity().get(i).getHoraFim());
        }
    }

    @Test
    void devePermitirBuscarMedicosPorCidade() {
        //Arrange
        String cidade = "Campinas";
        ArrayList<MedicoEntity> medicoEntityList = MedicoHelper.gerarListaMedicosEntityExistentesComHorarios();

        when(medicoRepository.findByCidadeIgnoreCase(anyString())).thenReturn(List.of(medicoEntityList.getLast()));

        //Act
        List<MedicoResponseDTO> medicoResponseDTOList = medicoJpaGateway.buscarMedicos(null, cidade);

        //Assert
        verify(medicoRepository, times(1)).findByCidadeIgnoreCase(cidade);
        assertThat(medicoResponseDTOList).hasSize(1);
        assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho()).hasSize(2);
        assertThat(medicoResponseDTOList.getFirst().getNome()).isEqualTo(medicoEntityList.getLast().getNome());
        assertThat(medicoResponseDTOList.getFirst().getEspecialidade()).isEqualTo(medicoEntityList.getLast().getEspecialidade());
        assertThat(medicoResponseDTOList.getFirst().getCidade()).isEqualTo(medicoEntityList.getLast().getCidade());

        for (int i = 0; i < medicoResponseDTOList.getFirst().getHorariosTrabalho().size(); i++) {
            assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho().get(i).getDiaDaSemana())
                    .isEqualTo(medicoEntityList.getLast().getHorariosTrabalhoEntity().get(i).getDiaDaSemana());

            assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho().get(i).getHoraInicio())
                    .isEqualTo(medicoEntityList.getLast().getHorariosTrabalhoEntity().get(i).getHoraInicio());

            assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho().get(i).getHoraFim())
                    .isEqualTo(medicoEntityList.getLast().getHorariosTrabalhoEntity().get(i).getHoraFim());
        }
    }

    @Test
    void devePermitirBuscarTodosOsMedicos() {
        //Arrange
        ArrayList<MedicoEntity> medicoEntityList = MedicoHelper.gerarListaMedicosEntityExistentesComHorarios();

        when(medicoRepository.findAll()).thenReturn(medicoEntityList);

        //Act
        List<MedicoResponseDTO> medicoResponseDTOList = medicoJpaGateway.buscarMedicos(null, null);

        //Assert
        verify(medicoRepository, times(1)).findAll();
        assertThat(medicoResponseDTOList).hasSize(3);
        assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho()).hasSize(2);
        assertThat(medicoResponseDTOList.get(1).getHorariosTrabalho()).hasSize(1);
        assertThat(medicoResponseDTOList.get(2).getHorariosTrabalho()).hasSize(2);

        for (int i = 0; i < medicoResponseDTOList.size(); i++) {
            assertThat(medicoResponseDTOList.get(i).getNome()).isEqualTo(medicoEntityList.get(i).getNome());
            assertThat(medicoResponseDTOList.get(i).getEspecialidade()).isEqualTo(medicoEntityList.get(i).getEspecialidade());
            assertThat(medicoResponseDTOList.get(i).getCidade()).isEqualTo(medicoEntityList.get(i).getCidade());

            for (int j = 0; j < medicoResponseDTOList.get(i).getHorariosTrabalho().size(); j++) {
                assertThat(medicoResponseDTOList.get(i).getHorariosTrabalho().get(j).getDiaDaSemana())
                        .isEqualTo(medicoEntityList.get(i).getHorariosTrabalhoEntity().get(j).getDiaDaSemana());

                assertThat(medicoResponseDTOList.get(i).getHorariosTrabalho().get(j).getHoraInicio())
                        .isEqualTo(medicoEntityList.get(i).getHorariosTrabalhoEntity().get(j).getHoraInicio());

                assertThat(medicoResponseDTOList.get(i).getHorariosTrabalho().get(j).getHoraFim())
                        .isEqualTo(medicoEntityList.get(i).getHorariosTrabalhoEntity().get(j).getHoraFim());
            }
        }
    }
}
