package Group4.StudyHubBackendG4.integration;

import Group4.StudyHubBackendG4.util.DatabaseInitializer;
import Group4.StudyHubBackendG4.util.SetUpHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CarreraControllerTest {

    @SpyBean
    private JavaMailSender javaMailSender;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SetUpHelper setUpHelper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @BeforeEach
    public void setUp() throws MessagingException, IOException {
        databaseInitializer.executeSqlScript("src/test/resources/cleanup.sql");
        setUpHelper.setUp();
    }

    @Test
    public void altaCarrera_Ok() throws Exception {
        mockMvc.perform(post("/api/carrera/altaCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaCarrera4)))
                .andExpect(status().isOk())
                .andExpect(content().string("Carrera registrada con éxito."));
    }

    @Test
    public void altaCarrera_BadRequest() throws Exception {
        mockMvc.perform(post("/api/carrera/altaCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaCarrera4Invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya existe una carrera con ese nombre."));
    }

    @Test
    public void asignarCoordinadorCarrera_Ok() throws Exception {
        mockMvc.perform(put("/api/carrera/asignarCoordinadorCarrera/{idCarrera}", setUpHelper.carrera1.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.userCoordinador2.getIdUsuario())))
                .andExpect(status().isOk())
                .andExpect(content().string("Coordinador asignado exitosamente."));
    }


    @Test
    public void asignarCoordinadorCarrera_BadRequest() throws Exception {
        mockMvc.perform(put("/api/carrera/asignarCoordinadorCarrera/{idCarrera}", setUpHelper.carrera1.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.userCoordinador1.getIdUsuario())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El coordinador ya está asignado a la carrera."));
    }

    @Test
    public void inscripcionCarrera_Ok() throws Exception {
        mockMvc.perform(post("/api/carrera/inscripcionCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionCarrera1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Inscripcion solicitada exitosamente."));
    }

    @Test
    public void inscripcionCarrera_BadRequest() throws Exception {
        mockMvc.perform(post("/api/carrera/inscripcionCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionCarrera2)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El estudiante ya tiene una inscripción activa."));
    }

    @Test
    public void inscripcionCarrera_UserNotFound() throws Exception {
        mockMvc.perform(post("/api/carrera/inscripcionCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionCarreraUserNotFound)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El usuario no fue encontrado."));
    }

    @Test
    public void inscripcionCarrera_UserNotStudent() throws Exception {
        mockMvc.perform(post("/api/carrera/inscripcionCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionCarreraUserNotStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El usuario no es un estudiante."));
    }

    @Test
    public void inscripcionCarrera_CarreraNotFound() throws Exception {
        mockMvc.perform(post("/api/carrera/inscripcionCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionCarreraCarreraNotFound)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La carrera no fue encontrada."));
    }

    @Test
    public void altaPeriodoDeExamen_Ok() throws Exception {
        mockMvc.perform(post("/api/carrera/altaPeriodoDeExamen/{idCarrera}", setUpHelper.carrera1.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtPeriodoExamenRequest1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Período de examen añadido con éxito."));
    }

    @Test
    public void altaPeriodoDeExamen_BadRequest() throws Exception {
        mockMvc.perform(post("/api/carrera/altaPeriodoDeExamen/{idCarrera}", setUpHelper.carrera1.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtPeriodoExamenRequestInvalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Se ha ingresado una fecha invalida"));
    }

    @Test
    public void altaPeriodoDeExamen_CarreraNotFound() throws Exception {
        mockMvc.perform(post("/api/carrera/altaPeriodoDeExamen/{idCarrera}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtPeriodoExamenRequest1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La carrera no existe!"));
    }

    @Test
    public void acceptEstudianteCarrera_Ok() throws Exception {
        mockMvc.perform(put("/api/carrera/acceptEstudianteCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionCarrera3)))
                .andExpect(status().isOk())
                .andExpect(content().string("Se aceptó la inscripcion del estudiante a la carrera."));
    }

    @Test
    public void acceptEstudianteCarrera_InscripcionNotPending() throws Exception {
        mockMvc.perform(put("/api/carrera/acceptEstudianteCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionCarrera1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El estudiante no tiene una inscripción pendiente de validación."));
    }

    @Test
    public void modificarCarrera_Ok() throws Exception {
        mockMvc.perform(put("/api/carrera/modificarCarrera/{idCarrera}", setUpHelper.carrera1.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtCarrera1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Carrera actualizada exitosamente"));
    }

    @Test
    public void modificarCarrera_Conflict() throws Exception {
        mockMvc.perform(put("/api/carrera/modificarCarrera/{idCarrera}", setUpHelper.carrera2.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtCarreraConflict)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ya existe una carrera con ese nombre."));
    }

    @Test
    public void modificarCarrera_NotFound() throws Exception {
        mockMvc.perform(put("/api/carrera/modificarCarrera/{idCarrera}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtCarreraConflict)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró carrera."));
    }

    @Test
    public void getPeriodosDeCarrera_Ok() throws Exception {
        mockMvc.perform(get("/api/carrera/getPeriodosDeCarrera/{idCarrera}", setUpHelper.carrera1.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Exámenes de Diciembre 2024"));
    }

    @Test
    public void getCarrerasConPeriodo_Ok() throws Exception {
        mockMvc.perform(get("/api/carrera/getCarrerasConPeriodo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ingeniería Informática"));
    }

    @Test
    public void getInscriptosPendientes_Ok() throws Exception {
        mockMvc.perform(get("/api/carrera/getInscriptosPendientes/{idCarrera}", setUpHelper.carrera2.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Bran"))
                .andExpect(jsonPath("$[0].apellido").value("Done"))
                .andExpect(jsonPath("$[0].cedula").value("123654786"));
    }

    @Test
    public void getCarrerasCoordinador_Ok() throws Exception {
        mockMvc.perform(get("/api/carrera/getCarrerasCoordinador/{idUsuario}", setUpHelper.userCoordinador1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ingeniería Informática"))
                .andExpect(jsonPath("$[1].nombre").value("Medicina"));
    }

    @Test
    public void getCarrerasInscripto_Ok() throws Exception {
        mockMvc.perform(get("/api/carrera/getCarrerasInscripto/{idUsuario}", setUpHelper.userEstudiante3.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Medicina"));
    }

    @Test
    public void getCarrerasInscripcionesPendientes_Ok() throws Exception {
        mockMvc.perform(get("/api/carrera/getCarrerasInscripcionesPendientes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Medicina"));
    }

    @Test
    public void getCarreras_Ok() throws Exception {
        mockMvc.perform(get("/api/carrera/getCarreras")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body[0].nombre").value("Ingeniería Informática"));
    }

    @Test
    public void getCarreraById_Ok() throws Exception {
        mockMvc.perform(get("/api/carrera/getCarreraById/{idCarrera}", setUpHelper.carrera2.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nombre").value("Medicina"))
                .andExpect(jsonPath("duracion").value("6"));
    }

    @Test
    public void getPreviaturasGrafo_Ok() throws Exception {
        mockMvc.perform(get("/api/carrera/getPreviaturasGrafo/{idCarrera}", setUpHelper.carrera1.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"1\" [label=\"Principios de Programación\"]")))
                .andExpect(content().string(
                        containsString("\"2\" [label=\"Programación de Aplicaciones\"]")))
                .andExpect(content().string(
                        containsString("\"1\" -> \"2\"")));
    }
}
