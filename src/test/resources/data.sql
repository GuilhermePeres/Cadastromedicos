INSERT INTO MEDICOS
    (nome, especialidade, cidade)
VALUES
    ('Ricardo Silva', 'Cardiologista', 'São Paulo'),
    ('Isadora Meneses', 'Dermatologista', 'São Paulo'),
    ('Carlos Domingues', 'Pediatra', 'Campinas');

INSERT INTO HORARIO_TRABALHO
    (dia_da_semana, hora_inicio, hora_fim, medico_entity_id)
VALUES
    ('SEGUNDA', '08:00', '13:00', 1),
    ('QUARTA', '13:00', '18:00', 1),
    ('TERCA', '09:00', '14:00', 2),
    ('QUINTA', '07:00', '11:00', 3),
    ('SABADO', '08:00', '13:00', 3);