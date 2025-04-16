package br.com.agendamentoconsultas.cadastromedicos.jpa;

import br.com.agendamentoconsultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.agendamentoconsultas.cadastromedicos.domain.Medico;
import br.com.agendamentoconsultas.cadastromedicos.exception.MedicoNaoCadastradoException;
import br.com.agendamentoconsultas.cadastromedicos.gateway.database.jpa.MedicoJpaGateway;
import br.com.agendamentoconsultas.cadastromedicos.gateway.database.jpa.entity.MedicoEntity;
import br.com.agendamentoconsultas.cadastromedicos.gateway.database.jpa.repository.MedicoRepository;
import br.com.agendamentoconsultas.cadastromedicos.utils.MedicoHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class MedicoJpaGatewayIT {

    @Autowired
    private MedicoJpaGateway medicoJpaGateway;

    @Autowired
    private MedicoRepository medicoRepository;

    @Test
    void devePermitirCadastrarMedico() {
        //Arrange
        Medico medico = MedicoHelper.gerarNovoMedicoComHorarios();

        //Act
        MedicoResponseDTO medicoResponseDTO = medicoJpaGateway.cadastrarMedico(medico);

        //Assert
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
    void devePermitirAtualizarMedico() {
        //Arrange
        Medico medicoAtualizado = MedicoHelper.gerarNovoMedicoComHorarios();
        Long id = 1L;

        medicoAtualizado.setEspecialidade("Especialidade Atualizada");
        medicoAtualizado.setCidade("Cidade Atualizada");

        //Act
        MedicoResponseDTO medicoResponseDTO = medicoJpaGateway.atualizarMedico(id, medicoAtualizado);

        //Assert
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

        //Act Assert
        assertThatThrownBy(() -> medicoJpaGateway.atualizarMedico(id, medico))
                .isInstanceOf(MedicoNaoCadastradoException.class);
    }

    @Test
    void devePermitirRemoverMedico() {
        //Arrange
        ArrayList<MedicoEntity> medicoEntityList = MedicoHelper.gerarListaMedicosEntityExistentesComHorarios();
        Long id = 1L;

        //Act
        medicoJpaGateway.removerMedico(id);
        List<MedicoResponseDTO> medicoResponseDTOList = medicoJpaGateway.buscarMedicos(null, null);

        //Assert
        medicoEntityList.removeFirst();

        assertThat(medicoResponseDTOList).hasSize(2);
        assertThat(medicoResponseDTOList.getFirst().getHorariosTrabalho()).hasSize(1);
        assertThat(medicoResponseDTOList.get(1).getHorariosTrabalho()).hasSize(2);

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

    @Test
    void deveLancarMedicoNaoCadastradoExceptionAoRemoverMedico(){
        //Arrange
        Long id = 4L;

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

        //Act
        List<MedicoResponseDTO> medicoResponseDTOList = medicoJpaGateway.buscarMedicos(especialidade, cidade);

        //Assert
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

        //Act
        List<MedicoResponseDTO> medicoResponseDTOList = medicoJpaGateway.buscarMedicos(especialidade, null);

        //Assert
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

        //Act
        List<MedicoResponseDTO> medicoResponseDTOList = medicoJpaGateway.buscarMedicos(null, cidade);

        //Assert
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

        //Act
        List<MedicoResponseDTO> medicoResponseDTOList = medicoJpaGateway.buscarMedicos(null, null);

        //Assert
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
