package br.com.agendamentoconsultas.cadastromedicos.jpa.repository;

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class MedicoRepositoryIT {

    @Autowired
    private MedicoRepository medicoRepository;

    @Test
    void devePermitirCriarTabela(){
        var totalRegistros = medicoRepository.count();

        assertThat(totalRegistros).isEqualTo(3);
    }

    @Test
    void devePermitirEncontrarMedicosPorEspecialidadeECidade() {
        //Arrange
        MedicoEntity medico = MedicoHelper.gerarMedicoEntityExistenteComHorarios();

        //Act
        List<MedicoEntity> medicosEncontrados = medicoRepository.findByEspecialidadeIgnoreCaseAndCidadeIgnoreCase(medico.getEspecialidade(), medico.getCidade());

        //Assert
        assertThat(medicosEncontrados).hasSize(1);
        assertThat(medicosEncontrados.getFirst().getNome()).isEqualTo(medico.getNome());
        assertThat(medicosEncontrados.getFirst().getEspecialidade()).isEqualTo(medico.getEspecialidade());
        assertThat(medicosEncontrados.getFirst().getCidade()).isEqualTo(medico.getCidade());
        assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity()).hasSize(2);

        for (int i = 0; i < medicosEncontrados.getFirst().getHorariosTrabalhoEntity().size(); i++) {
            assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity().get(i).getDiaDaSemana())
                    .isEqualTo(medico.getHorariosTrabalhoEntity().get(i).getDiaDaSemana());

            assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity().get(i).getHoraInicio())
                    .isEqualTo(medico.getHorariosTrabalhoEntity().get(i).getHoraInicio());

            assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity().get(i).getHoraFim())
                    .isEqualTo(medico.getHorariosTrabalhoEntity().get(i).getHoraFim());
        }
    }

    @Test
    void devePermitirEncontrarMedicosPorEspecialidade() {
        //Arrange
        MedicoEntity medico = MedicoHelper.gerarMedicoEntityExistenteComHorarios();

        //Act
        List<MedicoEntity> medicosEncontrados = medicoRepository.findByEspecialidadeIgnoreCase(medico.getEspecialidade());

        //Assert
        assertThat(medicosEncontrados).hasSize(1);
        assertThat(medicosEncontrados.getFirst().getNome()).isEqualTo(medico.getNome());
        assertThat(medicosEncontrados.getFirst().getEspecialidade()).isEqualTo(medico.getEspecialidade());
        assertThat(medicosEncontrados.getFirst().getCidade()).isEqualTo(medico.getCidade());
        assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity()).hasSize(2);

        for (int i = 0; i < medicosEncontrados.getFirst().getHorariosTrabalhoEntity().size(); i++) {
            assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity().get(i).getDiaDaSemana())
                    .isEqualTo(medico.getHorariosTrabalhoEntity().get(i).getDiaDaSemana());

            assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity().get(i).getHoraInicio())
                    .isEqualTo(medico.getHorariosTrabalhoEntity().get(i).getHoraInicio());

            assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity().get(i).getHoraFim())
                    .isEqualTo(medico.getHorariosTrabalhoEntity().get(i).getHoraFim());
        }
    }

    @Test
    void devePermitirEncontrarMedicosPorCidade() {
        //Arrange
        ArrayList<MedicoEntity> medicoEntityList = MedicoHelper.gerarListaMedicosEntityExistentesComHorarios();

        //Act
        List<MedicoEntity> medicosEncontrados = medicoRepository.findByCidadeIgnoreCase(medicoEntityList.getFirst().getCidade());

        //Assert
        assertThat(medicosEncontrados).hasSize(2);
        assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity()).hasSize(2);
        assertThat(medicosEncontrados.get(1).getHorariosTrabalhoEntity()).hasSize(1);

        for (int i = 0; i < medicosEncontrados.size(); i++) {
            assertThat(medicosEncontrados.get(i).getNome()).isEqualTo(medicoEntityList.get(i).getNome());
            assertThat(medicosEncontrados.get(i).getEspecialidade()).isEqualTo(medicoEntityList.get(i).getEspecialidade());
            assertThat(medicosEncontrados.get(i).getCidade()).isEqualTo(medicoEntityList.get(i).getCidade());

            for (int j = 0; j < medicosEncontrados.get(i).getHorariosTrabalhoEntity().size(); j++) {
                assertThat(medicosEncontrados.get(i).getHorariosTrabalhoEntity().get(j).getDiaDaSemana())
                        .isEqualTo(medicoEntityList.get(i).getHorariosTrabalhoEntity().get(j).getDiaDaSemana());

                assertThat(medicosEncontrados.get(i).getHorariosTrabalhoEntity().get(j).getHoraInicio())
                        .isEqualTo(medicoEntityList.get(i).getHorariosTrabalhoEntity().get(j).getHoraInicio());

                assertThat(medicosEncontrados.get(i).getHorariosTrabalhoEntity().get(j).getHoraFim())
                        .isEqualTo(medicoEntityList.get(i).getHorariosTrabalhoEntity().get(j).getHoraFim());
            }
        }
    }

    @Test
    void devePermitirSalvarMedico() {
        //Arrange
        MedicoEntity medicoEntity = MedicoHelper.gerarNovoMedicoEntityComHorarios();

        //Act
        MedicoEntity medicoSalvo = medicoRepository.save(medicoEntity);

        //Assert
        assertThat(medicoSalvo).isNotNull();
        assertThat(medicoSalvo.getHorariosTrabalhoEntity()).hasSize(1);
        assertThat(medicoSalvo.getNome()).isEqualTo(medicoEntity.getNome());
        assertThat(medicoSalvo.getEspecialidade()).isEqualTo(medicoEntity.getEspecialidade());
        assertThat(medicoSalvo.getCidade()).isEqualTo(medicoEntity.getCidade());
        assertThat(medicoSalvo.getHorariosTrabalhoEntity().getFirst().getDiaDaSemana())
                        .isEqualTo(medicoEntity.getHorariosTrabalhoEntity().getFirst().getDiaDaSemana());
        assertThat(medicoSalvo.getHorariosTrabalhoEntity().getFirst().getHoraInicio())
                        .isEqualTo(medicoEntity.getHorariosTrabalhoEntity().getFirst().getHoraInicio());
        assertThat(medicoSalvo.getHorariosTrabalhoEntity().getFirst().getHoraFim())
                        .isEqualTo(medicoEntity.getHorariosTrabalhoEntity().getFirst().getHoraFim());
    }

    @Test
    void devePermitirEncontrarMedicoPorId() {
        //Arrange
        MedicoEntity medicoEntity = MedicoHelper.gerarMedicoEntityExistenteComHorarios();

        //Act
        Optional<MedicoEntity> medicoEncontrado = medicoRepository.findById(1L);

        //Assert
        assertThat(medicoEncontrado).isPresent();
        assertThat(medicoEncontrado.get().getNome()).isEqualTo(medicoEntity.getNome());
        assertThat(medicoEncontrado.get().getEspecialidade()).isEqualTo(medicoEntity.getEspecialidade());
        assertThat(medicoEncontrado.get().getCidade()).isEqualTo(medicoEntity.getCidade());
        assertThat(medicoEncontrado.get().getHorariosTrabalhoEntity()).hasSize(2);

        for (int i = 0; i < medicoEncontrado.get().getHorariosTrabalhoEntity().size(); i++) {
            assertThat(medicoEncontrado.get().getHorariosTrabalhoEntity().get(i).getDiaDaSemana())
                    .isEqualTo(medicoEntity.getHorariosTrabalhoEntity().get(i).getDiaDaSemana());

            assertThat(medicoEncontrado.get().getHorariosTrabalhoEntity().get(i).getHoraInicio())
                    .isEqualTo(medicoEntity.getHorariosTrabalhoEntity().get(i).getHoraInicio());

            assertThat(medicoEncontrado.get().getHorariosTrabalhoEntity().get(i).getHoraFim())
                    .isEqualTo(medicoEntity.getHorariosTrabalhoEntity().get(i).getHoraFim());
        }
    }

    @Test
    void devePermitirEncontrarTodosOsMedicos() {
        //Arrange
        ArrayList<MedicoEntity> medicoEntities = MedicoHelper.gerarListaMedicosEntityExistentesComHorarios();

        //Act
        List<MedicoEntity> medicosEncontrados = medicoRepository.findAll();

        //Assert
        assertThat(medicosEncontrados).hasSize(3);
        assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity()).hasSize(2);
        assertThat(medicosEncontrados.get(1).getHorariosTrabalhoEntity()).hasSize(1);
        assertThat(medicosEncontrados.get(2).getHorariosTrabalhoEntity()).hasSize(2);

        for (int i = 0; i < medicosEncontrados.size(); i++) {
            assertThat(medicosEncontrados.get(i).getNome()).isEqualTo(medicoEntities.get(i).getNome());
            assertThat(medicosEncontrados.get(i).getEspecialidade()).isEqualTo(medicoEntities.get(i).getEspecialidade());
            assertThat(medicosEncontrados.get(i).getCidade()).isEqualTo(medicoEntities.get(i).getCidade());

            for (int j = 0; j < medicosEncontrados.get(i).getHorariosTrabalhoEntity().size(); j++) {
                assertThat(medicosEncontrados.get(i).getHorariosTrabalhoEntity().get(j).getDiaDaSemana())
                        .isEqualTo(medicoEntities.get(i).getHorariosTrabalhoEntity().get(j).getDiaDaSemana());

                assertThat(medicosEncontrados.get(i).getHorariosTrabalhoEntity().get(j).getHoraInicio())
                        .isEqualTo(medicoEntities.get(i).getHorariosTrabalhoEntity().get(j).getHoraInicio());

                assertThat(medicosEncontrados.get(i).getHorariosTrabalhoEntity().get(j).getHoraFim())
                        .isEqualTo(medicoEntities.get(i).getHorariosTrabalhoEntity().get(j).getHoraFim());
            }
        }
    }

    @Test
    void devePermitirRemoverMedico() {
        //Arrange
        ArrayList<MedicoEntity> medicoEntities = MedicoHelper.gerarListaMedicosEntityExistentesComHorarios();
        medicoEntities.getFirst().setId(1L);

        //Act
        medicoRepository.delete(medicoEntities.getFirst());
        List<MedicoEntity> medicosEncontrados = medicoRepository.findAll();

        //Assert
        medicoEntities.removeFirst();

        assertThat(medicosEncontrados).hasSize(2);
        assertThat(medicosEncontrados.getFirst().getHorariosTrabalhoEntity()).hasSize(1);
        assertThat(medicosEncontrados.get(1).getHorariosTrabalhoEntity()).hasSize(2);

        for (int i = 0; i < medicosEncontrados.size(); i++) {
            assertThat(medicosEncontrados.get(i).getNome()).isEqualTo(medicoEntities.get(i).getNome());
            assertThat(medicosEncontrados.get(i).getEspecialidade()).isEqualTo(medicoEntities.get(i).getEspecialidade());
            assertThat(medicosEncontrados.get(i).getCidade()).isEqualTo(medicoEntities.get(i).getCidade());

            for (int j = 0; j < medicosEncontrados.get(i).getHorariosTrabalhoEntity().size(); j++) {
                assertThat(medicosEncontrados.get(i).getHorariosTrabalhoEntity().get(j).getDiaDaSemana())
                        .isEqualTo(medicoEntities.get(i).getHorariosTrabalhoEntity().get(j).getDiaDaSemana());

                assertThat(medicosEncontrados.get(i).getHorariosTrabalhoEntity().get(j).getHoraInicio())
                        .isEqualTo(medicoEntities.get(i).getHorariosTrabalhoEntity().get(j).getHoraInicio());

                assertThat(medicosEncontrados.get(i).getHorariosTrabalhoEntity().get(j).getHoraFim())
                        .isEqualTo(medicoEntities.get(i).getHorariosTrabalhoEntity().get(j).getHoraFim());
            }
        }
    }
}
