package br.com.consultas.cadastromedicos.usecase;

import br.com.consultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.consultas.cadastromedicos.domain.Medico;
import br.com.consultas.cadastromedicos.gateway.MedicoGateway;
import br.com.consultas.cadastromedicos.utils.MedicoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MedicoUseCaseTest {
    @InjectMocks
    private MedicoUseCase medicoUseCase;

    @Mock
    private MedicoGateway medicoGateway;

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
        MedicoResponseDTO medicoResponseDTO = MedicoHelper.gerarNovoMedicoResponseDTOComHorarios();

        when(medicoGateway.cadastrarMedico(any(Medico.class))).thenReturn(medicoResponseDTO);

        //Act
        MedicoResponseDTO medicoResponseDTORetornado = medicoUseCase.cadastrarMedico(medico);

        //Assert
        verify(medicoGateway, times(1)).cadastrarMedico(any(Medico.class));
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

        when(medicoGateway.atualizarMedico(anyLong(), any(Medico.class))).thenReturn(medicoResponseDTO);

        //Act
        MedicoResponseDTO medicoResponseDTORetornado = medicoUseCase.atualizarMedico(id, medico);

        //Assert
        verify(medicoGateway, times(1)).atualizarMedico(anyLong(), any(Medico.class));
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
        Long id = 1L;

        doNothing().when(medicoGateway).removerMedico(anyLong());

        //Act
        medicoUseCase.removerMedico(id);

        //Assert
        verify(medicoGateway, times(1)).removerMedico(anyLong());
    }

    @Test
    void devePermitirBuscarTodosOsMedicos() {
        //Arrange
        ArrayList<MedicoResponseDTO> medicoResponseDTOList = MedicoHelper.gerarListaMedicoResponseDTOExistentesComHorarios();

        when(medicoGateway.buscarMedicos(null, null)).thenReturn(medicoResponseDTOList);

        //Act
        List<MedicoResponseDTO> medicoResponseDTOListRetornado = medicoUseCase.buscarMedicos(null, null);

        //Assert
        verify(medicoGateway, times(1)).buscarMedicos(null, null);
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
