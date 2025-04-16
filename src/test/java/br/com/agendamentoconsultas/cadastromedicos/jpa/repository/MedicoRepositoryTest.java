package br.com.agendamentoconsultas.cadastromedicos.jpa.repository;

import br.com.agendamentoconsultas.cadastromedicos.gateway.database.jpa.entity.MedicoEntity;
import br.com.agendamentoconsultas.cadastromedicos.gateway.database.jpa.repository.MedicoRepository;
import br.com.agendamentoconsultas.cadastromedicos.utils.MedicoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class MedicoRepositoryTest {

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
    void devePermitirEncontrarMedicosPorEspecialidadeECidade() {
        //Arrange
        MedicoEntity medico = MedicoHelper.gerarMedicoEntityExistente();

        when(medicoRepository.findByEspecialidadeIgnoreCaseAndCidadeIgnoreCase(anyString(), anyString())).thenReturn(List.of(medico));

        //Act
        List<MedicoEntity> medicosEncontrados = medicoRepository.findByEspecialidadeIgnoreCaseAndCidadeIgnoreCase(medico.getEspecialidade(), medico.getCidade());

        //Assert
        assertThat(medicosEncontrados).hasSize(1);
        assertThat(medicosEncontrados.getFirst().getNome()).isEqualTo(medico.getNome());
        assertThat(medicosEncontrados.getFirst().getEspecialidade()).isEqualTo(medico.getEspecialidade());
        assertThat(medicosEncontrados.getFirst().getCidade()).isEqualTo(medico.getCidade());
    }

    @Test
    void devePermitirEncontrarMedicosPorEspecialidade() {
        //Arrange
        MedicoEntity medico = MedicoHelper.gerarMedicoEntityExistente();

        when(medicoRepository.findByEspecialidadeIgnoreCase(anyString())).thenReturn(List.of(medico));

        //Act
        List<MedicoEntity> medicosEncontrados = medicoRepository.findByEspecialidadeIgnoreCase(medico.getEspecialidade());

        //Assert
        assertThat(medicosEncontrados).hasSize(1);
        assertThat(medicosEncontrados.getFirst().getNome()).isEqualTo(medico.getNome());
        assertThat(medicosEncontrados.getFirst().getEspecialidade()).isEqualTo(medico.getEspecialidade());
        assertThat(medicosEncontrados.getFirst().getCidade()).isEqualTo(medico.getCidade());
    }

    @Test
    void devePermitirEncontrarMedicosPorCidade() {
        //Arrange
        ArrayList<MedicoEntity> medicoEntities = MedicoHelper.gerarListaMedicosEntityExistentes();

        when(medicoRepository.findByCidadeIgnoreCase(anyString())).thenReturn(List.of(medicoEntities.getFirst(), medicoEntities.get(1)));

        //Act
        List<MedicoEntity> medicosEncontrados = medicoRepository.findByCidadeIgnoreCase(medicoEntities.getFirst().getCidade());

        //Assert
        assertThat(medicosEncontrados).hasSize(2);
        assertThat(medicosEncontrados.getFirst().getNome()).isEqualTo(medicoEntities.getFirst().getNome());
        assertThat(medicosEncontrados.getFirst().getEspecialidade()).isEqualTo(medicoEntities.getFirst().getEspecialidade());
        assertThat(medicosEncontrados.getFirst().getCidade()).isEqualTo(medicoEntities.getFirst().getCidade());
        assertThat(medicosEncontrados.get(1).getNome()).isEqualTo(medicoEntities.get(1).getNome());
        assertThat(medicosEncontrados.get(1).getEspecialidade()).isEqualTo(medicoEntities.get(1).getEspecialidade());
        assertThat(medicosEncontrados.get(1).getCidade()).isEqualTo(medicoEntities.get(1).getCidade());
    }

    @Test
    void devePermitirSalvarMedico() {
        //Arrange
        MedicoEntity medicoEntity = MedicoHelper.gerarNovoMedicoEntity();

        when(medicoRepository.save(medicoEntity)).thenReturn(medicoEntity);

        //Act
        MedicoEntity medicosEncontrado = medicoRepository.save(medicoEntity);

        //Assert
        assertThat(medicosEncontrado)
                .isNotNull()
                .isEqualTo(medicoEntity);
    }

    @Test
    void devePermitirEncontrarMedicoPorId() {
        //Arrange
        MedicoEntity medicoEntity = MedicoHelper.gerarMedicoEntityExistente();

        when(medicoRepository.findById(anyLong())).thenReturn(Optional.of(medicoEntity));

        //Act
        Optional<MedicoEntity> medicosEncontrado = medicoRepository.findById(1L);

        //Assert
        assertThat(medicosEncontrado)
                .isPresent()
                .isEqualTo(Optional.of(medicoEntity));
    }

    @Test
    void devePermitirEncontrarTodosOsMedicos() {
        //Arrange
        ArrayList<MedicoEntity> medicoEntities = MedicoHelper.gerarListaMedicosEntityExistentes();

        when(medicoRepository.findAll()).thenReturn(medicoEntities);

        //Act
        List<MedicoEntity> medicosEncontrados = medicoRepository.findAll();

        //Assert
        assertThat(medicosEncontrados)
                .hasSize(3)
                .isEqualTo(medicoEntities);
    }
}
