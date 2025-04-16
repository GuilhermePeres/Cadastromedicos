package br.com.consultas.cadastromedicos.controller;

import br.com.consultas.cadastromedicos.controller.dto.MedicoDTO;
import br.com.consultas.cadastromedicos.controller.dto.MedicoResponseDTO;
import br.com.consultas.cadastromedicos.domain.Medico;
import br.com.consultas.cadastromedicos.infrastructure.GlobalExceptionHandler;
import br.com.consultas.cadastromedicos.usecase.MedicoUseCase;
import br.com.consultas.cadastromedicos.utils.MedicoHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MedicoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicoUseCase medicoUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        MedicoController medicoController = new MedicoController(medicoUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(medicoController)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                })
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void devePermitirCadastrarMedico() throws Exception {
        //Arrange
        MedicoDTO medicoDTO = MedicoHelper.gerarNovoMedicoDTOComHorarios();
        MedicoResponseDTO medicoResponseDTO = MedicoHelper.gerarMedicoResponseDTOExistenteComHorarios();

        when(medicoUseCase.cadastrarMedico(any(Medico.class))).thenReturn(medicoResponseDTO);

        //Act & Assert
        mockMvc.perform(post("/medicos/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(medicoDTO)))
                        .andExpect(status().isOk());

        verify(medicoUseCase, times(1)).cadastrarMedico(any(Medico.class));
    }

    @Test
    void devePermitirAtualizarMedico() throws Exception {
        //Arrange
        MedicoDTO medicoDTO = MedicoHelper.gerarNovoMedicoDTOComHorariosEmBranco();
        Long id = 1L;
        MedicoResponseDTO medicoResponseDTO = MedicoHelper.gerarMedicoResponseDTOExistenteComHorariosAtualizado();

        when(medicoUseCase.atualizarMedico(anyLong(), any(Medico.class))).thenReturn(medicoResponseDTO);

        //Act & Assert
        mockMvc.perform(put("/medicos/atualizar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(medicoDTO)))
                .andExpect(status().isOk());

        verify(medicoUseCase, times(1)).atualizarMedico(anyLong(), any(Medico.class));
    }

    @Test
    void devePermitirRemoverMedico() throws Exception {
        Long id = 1L;
        String mensagemRetorno = "Médico removido com sucesso!";

        doNothing().when(medicoUseCase).removerMedico(anyLong());

        mockMvc.perform(delete("/medicos/remover/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(mensagemRetorno));

        verify(medicoUseCase, times(1)).removerMedico(anyLong());
    }

    @Test
    void devePermitirBuscarMedico() throws Exception {
        String especialidade = "Cardiologista";
        String cidade = "São Paulo";
        List<MedicoResponseDTO> medicoResponseDTOList = MedicoHelper.gerarListaMedicoResponseDTOExistentesComHorarios();

        when(medicoUseCase.buscarMedicos(anyString(), anyString())).thenReturn(List.of(medicoResponseDTOList.getFirst()));

        mockMvc.perform(get("/medicos")
                        .param("especialidade", especialidade)
                        .param("cidade", cidade))
                .andExpect(status().isOk());

        verify(medicoUseCase, times(1)).buscarMedicos(anyString(), anyString());
    }

    @Test
    void devePermitirBuscarMedicoSemRetorno() throws Exception {
        String especialidade = "Dentista";
        String cidade = "";
        List<MedicoResponseDTO> medicoResponseDTOList = Collections.emptyList();

        when(medicoUseCase.buscarMedicos(anyString(), anyString())).thenReturn(medicoResponseDTOList);

        mockMvc.perform(get("/medicos")
                        .param("especialidade", especialidade)
                        .param("cidade", cidade))
                .andExpect(status().isNoContent());

        verify(medicoUseCase, times(1)).buscarMedicos(anyString(), anyString());
    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(object);
    }
}
