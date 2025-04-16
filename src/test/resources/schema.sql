CREATE TABLE IF NOT EXISTS MEDICOS (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255),
    especialidade VARCHAR(255),
    cidade VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS HORARIO_TRABALHO (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dia_da_semana VARCHAR(255),
    hora_inicio TIME,
    hora_fim TIME,
    medico_entity_id BIGINT,
    FOREIGN KEY (medico_entity_id) REFERENCES MEDICOS(id)
);