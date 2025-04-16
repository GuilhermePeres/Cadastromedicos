package br.com.consultas.cadastromedicos.controller;

import br.com.consultas.cadastromedicos.controller.dto.MedicoDTO;
import br.com.consultas.cadastromedicos.utils.MedicoHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class MedicoControllerIT {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup(){
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void devePermitirCadastrarMedico(){
        MedicoDTO medicoDTO = MedicoHelper.gerarNovoMedicoDTOComHorarios();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(medicoDTO)
        .when()
                .post("/medicos/cadastrar")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/medicoResponseDTO.schema.json"));
    }

    @Test
    void devePermitirAtualizarMedico(){
        MedicoDTO medicoDTO = MedicoHelper.gerarNovoMedicoDTOComHorariosAtualizado();
        Long id = 3L;

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(medicoDTO)
        .when()
                .put("/medicos/atualizar/{id}", id)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/medicoResponseDTO.schema.json"));
    }

    @Test
    void devePermitirRemoverMedico() {
        Long id = 1L;

        when()
                .delete("medicos/remover/{id}", id)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Médico removido com sucesso!"));
    }

    @Test
    void devePermitirBuscarMedico() {
        given()
                .param("especialidade", "")
                .param("cidade", "")
        .when()
                .get("/medicos")
        .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("[0].id", org.hamcrest.Matchers.is(1))
                .body("[1].id", org.hamcrest.Matchers.is(2))
                .body("[2].id", org.hamcrest.Matchers.is(3))
                .body(matchesJsonSchemaInClasspath("schemas/medicoResponseDTOList.schema.json"));
    }

    @Test
    void devePermitirBuscarMedicoSemRetorno() {
        given()
                .param("especialidade", "Dentista")
                .param("cidade", "")
        .when()
                .get("/medicos")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
