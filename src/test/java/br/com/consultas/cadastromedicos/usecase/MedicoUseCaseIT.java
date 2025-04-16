package br.com.consultas.cadastromedicos.usecase;

import br.com.consultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.consultas.cadastromedicos.domain.Medico;
import br.com.consultas.cadastromedicos.gateway.MedicoGateway;
import br.com.consultas.cadastromedicos.gateway.database.jpa.entity.MedicoEntity;
import br.com.consultas.cadastromedicos.utils.MedicoHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class MedicoUseCaseIT {

    @Autowired
    private MedicoUseCase medicoUseCase;

    @Autowired
    private MedicoGateway medicoGateway;

    @Test
    void devePermitirCadastrarMedico() {
        //Arrange
        Medico medico = MedicoHelper.gerarNovoMedicoComHorarios();

        //Act
        MedicoResponseDTO medicoResponseDTORetornado = medicoUseCase.cadastrarMedico(medico);

        //Assert
        assertThat(medicoResponseDTORetornado).isNotNull();
        assertThat(medicoResponseDTORetornado.getHorariosTrabalho()).hasSize(1);
        assertThat(medicoResponseDTORetornado.getNome()).isEqualTo(medico.getNome());
        assertThat(medicoResponseDTORetornado.getEspecialidade()).isEqualTo(medico.getEspecialidade());
        assertThat(medicoResponseDTORetornado.getCidade()).isEqualTo(medico.getCidade());
        assertThat(medicoResponseDTORetornado.getHorariosTrabalho().getFirst().getDiaDaSemana())
                .isEqualTo(medico.getHorariosTrabalho().getFirst().getDiaDaSemana());
        assertThat(medicoResponseDTORetornado.getHorariosTrabalho().getFirst().getHoraInicio())
                .isEqualTo(medico.getHorariosTrabalho().getFirst().getHoraInicio());
        assertThat(medicoResponseDTORetornado.getHorariosTrabalho().getFirst().getHoraFim())
                .isEqualTo(medico.getHorariosTrabalho().getFirst().getHoraFim());
    }

    @Test
    void devePermitirAtualizarMedico() {
        //Arrange
        Medico medico = MedicoHelper.gerarMedicoEmBrancoComHorariosEmBranco();
        MedicoResponseDTO medicoResponseDTO = MedicoHelper.gerarMedicoResponseDTOExistenteComHorarios();
        Long id = 1L;

        //Act
        MedicoResponseDTO medicoResponseDTORetornado = medicoUseCase.atualizarMedico(id, medico);

        //Assert
        assertThat(medicoResponseDTORetornado).isNotNull();
        assertThat(medicoResponseDTORetornado.getHorariosTrabalho()).hasSize(2);
        assertThat(medicoResponseDTORetornado.getNome()).isEqualTo(medicoResponseDTO.getNome());
        assertThat(medicoResponseDTORetornado.getEspecialidade()).isEqualTo(medicoResponseDTO.getEspecialidade());
        assertThat(medicoResponseDTORetornado.getCidade()).isEqualTo(medicoResponseDTO.getCidade());

        for (int i = 0; i < medicoResponseDTORetornado.getHorariosTrabalho().size(); i++) {
            assertThat(medicoResponseDTORetornado.getHorariosTrabalho().get(i).getDiaDaSemana())
                    .isEqualTo(medicoResponseDTO.getHorariosTrabalho().get(i).getDiaDaSemana());

            assertThat(medicoResponseDTORetornado.getHorariosTrabalho().get(i).getHoraInicio())
                    .isEqualTo(medicoResponseDTO.getHorariosTrabalho().get(i).getHoraInicio());

            assertThat(medicoResponseDTORetornado.getHorariosTrabalho().get(i).getHoraFim())
                    .isEqualTo(medicoResponseDTO.getHorariosTrabalho().get(i).getHoraFim());
        }
    }

    @Test
    void devePermitirRemoverMedico() {
        //Arrange
        ArrayList<MedicoEntity> medicoEntityList = MedicoHelper.gerarListaMedicosEntityExistentesComHorarios();
        Long id = 1L;

        //Act
        medicoUseCase.removerMedico(id);
        List<MedicoResponseDTO> medicoResponseDTOList = medicoUseCase.buscarMedicos(null, null);

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
    void devePermitirBuscarTodosOsMedicos() {
        //Arrange
        ArrayList<MedicoResponseDTO> medicoResponseDTOList = MedicoHelper.gerarListaMedicoResponseDTOExistentesComHorarios();

        //Act
        List<MedicoResponseDTO> medicoResponseDTOListRetornado = medicoUseCase.buscarMedicos(null, null);

        //Assert
        assertThat(medicoResponseDTOListRetornado).hasSize(3);
        assertThat(medicoResponseDTOListRetornado.getFirst().getHorariosTrabalho()).hasSize(2);
        assertThat(medicoResponseDTOListRetornado.get(1).getHorariosTrabalho()).hasSize(1);
        assertThat(medicoResponseDTOListRetornado.get(2).getHorariosTrabalho()).hasSize(2);

        for (int i = 0; i < medicoResponseDTOListRetornado.size(); i++) {
            assertThat(medicoResponseDTOListRetornado.get(i).getNome()).isEqualTo(medicoResponseDTOList.get(i).getNome());
            assertThat(medicoResponseDTOListRetornado.get(i).getEspecialidade()).isEqualTo(medicoResponseDTOList.get(i).getEspecialidade());
            assertThat(medicoResponseDTOListRetornado.get(i).getCidade()).isEqualTo(medicoResponseDTOList.get(i).getCidade());

            for (int j = 0; j < medicoResponseDTOListRetornado.get(i).getHorariosTrabalho().size(); j++) {
                assertThat(medicoResponseDTOListRetornado.get(i).getHorariosTrabalho().get(j).getDiaDaSemana())
                        .isEqualTo(medicoResponseDTOList.get(i).getHorariosTrabalho().get(j).getDiaDaSemana());

                assertThat(medicoResponseDTOListRetornado.get(i).getHorariosTrabalho().get(j).getHoraInicio())
                        .isEqualTo(medicoResponseDTOList.get(i).getHorariosTrabalho().get(j).getHoraInicio());

                assertThat(medicoResponseDTOListRetornado.get(i).getHorariosTrabalho().get(j).getHoraFim())
                        .isEqualTo(medicoResponseDTOList.get(i).getHorariosTrabalho().get(j).getHoraFim());
            }
        }
    }
}
