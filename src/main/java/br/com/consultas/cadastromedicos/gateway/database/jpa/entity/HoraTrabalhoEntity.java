package br.com.consultas.cadastromedicos.gateway.database.jpa.entity;

import br.com.consultas.cadastromedicos.domain.DiaDaSemanaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="HORARIO_TRABALHO")
public class HoraTrabalhoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private DiaDaSemanaEnum diaDaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    @ManyToOne
    private MedicoEntity medicoEntity;
}
