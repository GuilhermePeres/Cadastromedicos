package br.com.consultas.cadastromedicos.utils;

import br.com.consultas.cadastromedicos.controller.dto.HorarioTrabalhoDTO;
import br.com.consultas.cadastromedicos.controller.dto.HorarioTrabalhoRespDTO;
import br.com.consultas.cadastromedicos.controller.dto.MedicoDTO;
import br.com.consultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.consultas.cadastromedicos.domain.DiaDaSemanaEnum;
import br.com.consultas.cadastromedicos.domain.HorarioTrabalho;
import br.com.consultas.cadastromedicos.domain.Medico;
import br.com.consultas.cadastromedicos.gateway.database.jpa.entity.HoraTrabalhoEntity;
import br.com.consultas.cadastromedicos.gateway.database.jpa.entity.MedicoEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MedicoHelper {
    public static MedicoEntity gerarNovoMedicoEntity() {
        MedicoEntity medico = new MedicoEntity();
        medico.setNome("Carol Pompeo");
        medico.setEspecialidade("Cardiologista");
        medico.setCidade("São Paulo");
        return medico;
    }

    public static Medico gerarNovoMedicoComHorarios() {
        Medico medico = new Medico();
        medico.setNome("Carol Pompeo");
        medico.setEspecialidade("Cardiologista");
        medico.setCidade("São Paulo");

        List<HorarioTrabalho> horarioTrabalhoList = new ArrayList<>();

        HorarioTrabalho horario = new HorarioTrabalho();
        horario.setDiaDaSemana(DiaDaSemanaEnum.SEXTA);
        horario.setHoraInicio(LocalTime.of(14, 0));
        horario.setHoraFim(LocalTime.of(19, 0));
        horarioTrabalhoList.add(horario);

        medico.setHorariosTrabalho(horarioTrabalhoList);

        return medico;
    }

    public static MedicoEntity gerarNovoMedicoEntityComHorarios() {
        MedicoEntity medico = new MedicoEntity();
        medico.setNome("Carol Pompeo");
        medico.setEspecialidade("Cardiologista");
        medico.setCidade("São Paulo");

        List<HoraTrabalhoEntity> horariosEntityList = new ArrayList<>();

        HoraTrabalhoEntity horario = new HoraTrabalhoEntity();
        horario.setDiaDaSemana(DiaDaSemanaEnum.SEXTA);
        horario.setHoraInicio(LocalTime.of(14, 0));
        horario.setHoraFim(LocalTime.of(19, 0));
        horario.setMedicoEntity(medico);
        horariosEntityList.add(horario);

        medico.setHorariosTrabalhoEntity(horariosEntityList);

        return medico;
    }

    public static MedicoResponseDTO gerarNovoMedicoResponseDTOComHorarios() {
        List<HorarioTrabalhoRespDTO> horarioTrabalhoList = new ArrayList<>();

        HorarioTrabalhoRespDTO horario = new HorarioTrabalhoRespDTO(
                DiaDaSemanaEnum.SEXTA,
                LocalTime.of(14, 0),
                LocalTime.of(19, 0));

        horarioTrabalhoList.add(horario);

        return new MedicoResponseDTO(
                4L,
                "Carol Pompeo",
                "Cardiologista",
                "São Paulo",
                horarioTrabalhoList);
    }

    public static MedicoDTO gerarNovoMedicoDTOComHorarios() {
        List<HorarioTrabalhoDTO> horarioTrabalhoList = new ArrayList<>();

        HorarioTrabalhoDTO horario = new HorarioTrabalhoDTO(
                DiaDaSemanaEnum.SEXTA,
                LocalTime.of(14, 0),
                LocalTime.of(19, 0));

        horarioTrabalhoList.add(horario);

        return new MedicoDTO(
                "Carol Pompeo",
                "Cardiologista",
                "São Paulo",
                horarioTrabalhoList);
    }

    public static MedicoDTO gerarNovoMedicoDTOComHorariosAtualizado(){
        List<HorarioTrabalhoDTO> horariosDtoList = new ArrayList<>();

        HorarioTrabalhoDTO horario1 = new HorarioTrabalhoDTO(
                DiaDaSemanaEnum.QUINTA,
                LocalTime.of(7, 0),
                LocalTime.of(11, 0));
        horariosDtoList.add(horario1);

        HorarioTrabalhoDTO horario2 = new HorarioTrabalhoDTO(
                DiaDaSemanaEnum.SABADO,
                LocalTime.of(8, 0),
                LocalTime.of(13, 0));
        horariosDtoList.add(horario2);

        return new MedicoDTO(
                "Carol Pompeo",
                "Cardiologista",
                "São Paulo",
                horariosDtoList);
    }

    public static MedicoDTO gerarNovoMedicoDTOComHorariosEmBranco() {
        List<HorarioTrabalhoDTO> horarioTrabalhoList = new ArrayList<>();

        return new MedicoDTO(
                "Carol Pompeo",
                "Cardiologista",
                "São Paulo",
                horarioTrabalhoList);
    }

    public static MedicoEntity gerarMedicoEntityExistente(){
        MedicoEntity medico = new MedicoEntity();
        medico.setNome("Ricardo Silva");
        medico.setEspecialidade("Cardiologista");
        medico.setCidade("São Paulo");
        return medico;
    }

    public static MedicoEntity gerarMedicoEntityExistenteComHorarios(){
        MedicoEntity medico = new MedicoEntity();
        medico.setNome("Ricardo Silva");
        medico.setEspecialidade("Cardiologista");
        medico.setCidade("São Paulo");

        List<HoraTrabalhoEntity> horariosEntityList = new ArrayList<>();

        HoraTrabalhoEntity horario1 = new HoraTrabalhoEntity();
        horario1.setDiaDaSemana(DiaDaSemanaEnum.SEGUNDA);
        horario1.setHoraInicio(LocalTime.of(8, 0));
        horario1.setHoraFim(LocalTime.of(13, 0));
        horario1.setMedicoEntity(medico);
        horariosEntityList.add(horario1);

        HoraTrabalhoEntity horario2 = new HoraTrabalhoEntity();
        horario2.setDiaDaSemana(DiaDaSemanaEnum.QUARTA);
        horario2.setHoraInicio(LocalTime.of(13, 0));
        horario2.setHoraFim(LocalTime.of(18, 0));
        horario2.setMedicoEntity(medico);
        horariosEntityList.add(horario2);

        medico.setHorariosTrabalhoEntity(horariosEntityList);

        return medico;
    }

    public static MedicoResponseDTO gerarMedicoResponseDTOExistenteComHorarios(){
        List<HorarioTrabalhoRespDTO> horariosDtoList = new ArrayList<>();

        HorarioTrabalhoRespDTO horario1 = new HorarioTrabalhoRespDTO(
                DiaDaSemanaEnum.SEGUNDA,
                LocalTime.of(8, 0),
                LocalTime.of(13, 0));
        horariosDtoList.add(horario1);

        HorarioTrabalhoRespDTO horario2 = new HorarioTrabalhoRespDTO(
                DiaDaSemanaEnum.QUARTA,
                LocalTime.of(13, 0),
                LocalTime.of(18, 0));
        horariosDtoList.add(horario2);

        return new MedicoResponseDTO(
                1L,
                "Ricardo Silva",
                "Cardiologista",
                "São Paulo",
                horariosDtoList);
    }

    public static MedicoResponseDTO gerarMedicoResponseDTOExistenteComHorariosAtualizado(){
        List<HorarioTrabalhoRespDTO> horariosDtoList = new ArrayList<>();

        HorarioTrabalhoRespDTO horario1 = new HorarioTrabalhoRespDTO(
                DiaDaSemanaEnum.SEGUNDA,
                LocalTime.of(8, 0),
                LocalTime.of(13, 0));
        horariosDtoList.add(horario1);

        HorarioTrabalhoRespDTO horario2 = new HorarioTrabalhoRespDTO(
                DiaDaSemanaEnum.QUARTA,
                LocalTime.of(13, 0),
                LocalTime.of(18, 0));
        horariosDtoList.add(horario2);

        return new MedicoResponseDTO(
                1L,
                "Carol Pompeo",
                "Cardiologista",
                "São Paulo",
                horariosDtoList);
    }

    public static Medico gerarMedicoExistenteComHorarios(){
        Medico medico = new Medico();
        medico.setNome("Ricardo Silva");
        medico.setEspecialidade("Cardiologista");
        medico.setCidade("São Paulo");

        List<HorarioTrabalho> horariosList = new ArrayList<>();

        HorarioTrabalho horario1 = new HorarioTrabalho();
        horario1.setDiaDaSemana(DiaDaSemanaEnum.SEGUNDA);
        horario1.setHoraInicio(LocalTime.of(8, 0));
        horario1.setHoraFim(LocalTime.of(13, 0));
        horariosList.add(horario1);

        HorarioTrabalho horario2 = new HorarioTrabalho();
        horario2.setDiaDaSemana(DiaDaSemanaEnum.QUARTA);
        horario2.setHoraInicio(LocalTime.of(13, 0));
        horario2.setHoraFim(LocalTime.of(18, 0));
        horariosList.add(horario2);

        medico.setHorariosTrabalho(horariosList);

        return medico;
    }

    public static Medico gerarMedicoEmBrancoComHorariosEmBranco(){
        Medico medico = new Medico();
        medico.setNome("");
        medico.setEspecialidade("");
        medico.setCidade("");

        List<HorarioTrabalho> horariosList = new ArrayList<>();

        medico.setHorariosTrabalho(horariosList);

        return medico;
    }

    public static ArrayList<MedicoEntity> gerarListaMedicosEntityExistentes(){
        ArrayList<MedicoEntity> medicoEntities = new ArrayList<>();

        MedicoEntity medico1 = new MedicoEntity();
        medico1.setNome("Ricardo Silva");
        medico1.setEspecialidade("Cardiologista");
        medico1.setCidade("São Paulo");
        medicoEntities.add(medico1);

        MedicoEntity medico2 = new MedicoEntity();
        medico2.setNome("Isadora Meneses");
        medico2.setEspecialidade("Dermatologista");
        medico2.setCidade("São Paulo");
        medicoEntities.add(medico2);

        MedicoEntity medico3 = new MedicoEntity();
        medico3.setNome("Carlos Domingues");
        medico3.setEspecialidade("Pediatra");
        medico3.setCidade("Campinas");
        medicoEntities.add(medico3);

        return medicoEntities;
    }

    public static ArrayList<MedicoEntity> gerarListaMedicosEntityExistentesComHorarios(){
        ArrayList<MedicoEntity> medicoEntities = new ArrayList<>();

        MedicoEntity medico1 = new MedicoEntity();
        medico1.setNome("Ricardo Silva");
        medico1.setEspecialidade("Cardiologista");
        medico1.setCidade("São Paulo");

        List<HoraTrabalhoEntity> horariosEntityList1 = new ArrayList<>();
        HoraTrabalhoEntity horario1 = new HoraTrabalhoEntity();
        horario1.setDiaDaSemana(DiaDaSemanaEnum.SEGUNDA);
        horario1.setHoraInicio(LocalTime.of(8, 0));
        horario1.setHoraFim(LocalTime.of(13, 0));
        horario1.setMedicoEntity(medico1);
        horariosEntityList1.add(horario1);

        HoraTrabalhoEntity horario2 = new HoraTrabalhoEntity();
        horario2.setDiaDaSemana(DiaDaSemanaEnum.QUARTA);
        horario2.setHoraInicio(LocalTime.of(13, 0));
        horario2.setHoraFim(LocalTime.of(18, 0));
        horario2.setMedicoEntity(medico1);
        horariosEntityList1.add(horario2);

        medico1.setHorariosTrabalhoEntity(horariosEntityList1);
        medicoEntities.add(medico1);

        MedicoEntity medico2 = new MedicoEntity();
        medico2.setNome("Isadora Meneses");
        medico2.setEspecialidade("Dermatologista");
        medico2.setCidade("São Paulo");

        List<HoraTrabalhoEntity> horariosEntityList2 = new ArrayList<>();
        HoraTrabalhoEntity horario3 = new HoraTrabalhoEntity();
        horario3.setDiaDaSemana(DiaDaSemanaEnum.TERCA);
        horario3.setHoraInicio(LocalTime.of(9, 0));
        horario3.setHoraFim(LocalTime.of(14, 0));
        horario3.setMedicoEntity(medico2);
        horariosEntityList2.add(horario3);

        medico2.setHorariosTrabalhoEntity(horariosEntityList2);
        medicoEntities.add(medico2);

        MedicoEntity medico3 = new MedicoEntity();
        medico3.setNome("Carlos Domingues");
        medico3.setEspecialidade("Pediatra");
        medico3.setCidade("Campinas");

        List<HoraTrabalhoEntity> horariosEntityList3 = new ArrayList<>();
        HoraTrabalhoEntity horario4 = new HoraTrabalhoEntity();
        horario4.setDiaDaSemana(DiaDaSemanaEnum.QUINTA);
        horario4.setHoraInicio(LocalTime.of(7, 0));
        horario4.setHoraFim(LocalTime.of(11, 0));
        horario4.setMedicoEntity(medico3);
        horariosEntityList3.add(horario4);

        HoraTrabalhoEntity horario5 = new HoraTrabalhoEntity();
        horario5.setDiaDaSemana(DiaDaSemanaEnum.SABADO);
        horario5.setHoraInicio(LocalTime.of(8, 0));
        horario5.setHoraFim(LocalTime.of(13, 0));
        horario5.setMedicoEntity(medico3);
        horariosEntityList3.add(horario5);

        medico3.setHorariosTrabalhoEntity(horariosEntityList3);
        medicoEntities.add(medico3);

        return medicoEntities;
    }

    public static ArrayList<MedicoResponseDTO> gerarListaMedicoResponseDTOExistentesComHorarios(){
        ArrayList<MedicoResponseDTO> medicoResponseDTOS = new ArrayList<>();

        List<HorarioTrabalhoRespDTO> horariosDtoList1 = new ArrayList<>();
        HorarioTrabalhoRespDTO horario1 = new HorarioTrabalhoRespDTO(
                DiaDaSemanaEnum.SEGUNDA,
                LocalTime.of(8, 0),
                LocalTime.of(13, 0));
        horariosDtoList1.add(horario1);

        HorarioTrabalhoRespDTO horario2 = new HorarioTrabalhoRespDTO(
                DiaDaSemanaEnum.QUARTA,
                LocalTime.of(13, 0),
                LocalTime.of(18, 0));
        horariosDtoList1.add(horario2);

        MedicoResponseDTO medico1 = new MedicoResponseDTO(
                1L,
                "Ricardo Silva",
                "Cardiologista",
                "São Paulo",
                horariosDtoList1
        );
        medicoResponseDTOS.add(medico1);

        List<HorarioTrabalhoRespDTO> horariosDtoList2 = new ArrayList<>();
        HorarioTrabalhoRespDTO horario3 = new HorarioTrabalhoRespDTO(
                DiaDaSemanaEnum.TERCA,
                LocalTime.of(9, 0),
                LocalTime.of(14, 0));
        horariosDtoList2.add(horario3);

        MedicoResponseDTO medico2 = new MedicoResponseDTO(
                2L,
                "Isadora Meneses",
                "Dermatologista",
                "São Paulo",
                horariosDtoList2
        );
        medicoResponseDTOS.add(medico2);

        List<HorarioTrabalhoRespDTO> horariosDtoList3 = new ArrayList<>();
        HorarioTrabalhoRespDTO horario4 = new HorarioTrabalhoRespDTO(
                DiaDaSemanaEnum.QUINTA,
                LocalTime.of(7, 0),
                LocalTime.of(11, 0));
        horariosDtoList3.add(horario4);

        HorarioTrabalhoRespDTO horario5 = new HorarioTrabalhoRespDTO(
                DiaDaSemanaEnum.SABADO,
                LocalTime.of(8, 0),
                LocalTime.of(13, 0));
        horariosDtoList3.add(horario5);

        MedicoResponseDTO medico3 = new MedicoResponseDTO(
                3L,
                "Carlos Domingues",
                "Pediatra",
                "Campinas",
                horariosDtoList3
        );

        medicoResponseDTOS.add(medico3);

        return medicoResponseDTOS;
    }
}
