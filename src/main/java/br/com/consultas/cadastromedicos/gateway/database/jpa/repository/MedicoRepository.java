package br.com.consultas.cadastromedicos.gateway.database.jpa.repository;

import br.com.consultas.cadastromedicos.gateway.database.jpa.entity.MedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<MedicoEntity, Long> {
    List<MedicoEntity> findByEspecialidadeIgnoreCaseAndCidadeIgnoreCase(String especialidade, String cidade);
    List<MedicoEntity> findByEspecialidadeIgnoreCase(String especialidade);
    List<MedicoEntity> findByCidadeIgnoreCase(String cidade);
}
