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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ExamenControllerTest {

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
    public void registroAsignaturaAPeriodo_Ok() throws Exception {
        mockMvc.perform(post("/api/examen/registroAsignaturaAPeriodo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoExamen1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Se registró la asignatura al periodo de examen."));
    }

    @Test
    public void registroAsignaturaAPeriodo_AsignaturaNotFound() throws Exception {
        mockMvc.perform(post("/api/examen/registroAsignaturaAPeriodo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoExamenInvalidAsignatura)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No existe la asignatura"));
    }

    @Test
    public void registroAsignaturaAPeriodo_PeriodoNotFound() throws Exception {
        mockMvc.perform(post("/api/examen/registroAsignaturaAPeriodo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoExamenInvalidPeriodo)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No existe el periodo"));
    }

    @Test
    public void registroAsignaturaAPeriodo_DocenteNotFound() throws Exception {
        mockMvc.perform(post("/api/examen/registroAsignaturaAPeriodo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoExamenInvalidDocente)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No existe el docente con id: "+ setUpHelper.dtNuevoExamenInvalidDocente.getIdsDocentes().get(0)));
    }

    @Test
    public void registroAsignaturaAPeriodo_HorarioOutOfBounds() throws Exception {
        mockMvc.perform(post("/api/examen/registroAsignaturaAPeriodo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoExamenInvalidInvalidHorario)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La fecha indicada no esta dentro del periodo de examen."));
    }

    @Test
    public void registroAsignaturaAPeriodo_ExamenAlreadyExists() throws Exception {
        mockMvc.perform(post("/api/examen/registroAsignaturaAPeriodo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoExamenaAlreadyExists)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya existe un examen de la asignatura para la fecha indicada."));
    }

    @Test
    public void inscripcionExamen_Ok() throws Exception {
        mockMvc.perform(post("/api/examen/inscripcionExamen")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionExamen1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Se inscribió al examen."));
    }

    @Test
    public void inscripcionExamen_EstudianteNotFound() throws Exception {
        mockMvc.perform(post("/api/examen/inscripcionExamen")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionExamenInvalidEstudiante)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No existe el estudiante."));
    }

    @Test
    public void inscripcionExamen_ExamenNotFound() throws Exception {
        mockMvc.perform(post("/api/examen/inscripcionExamen")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionExamenInvalidExamen)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No existe el examen."));
    }

    @Test
    public void inscripcionExamen_StudentHasNoCursadaWithResultadoExamen() throws Exception {
        mockMvc.perform(post("/api/examen/inscripcionExamen")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionExamenInvalidCursadaWithNoExamen)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El estudiante no tiene una cursada con resultado 'EXAMEN'."));
    }

    @Test
    public void inscripcionExamen_AsignaturaAlreadyApproved() throws Exception {
        mockMvc.perform(post("/api/examen/inscripcionExamen")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtInscripcionExamenInvalidAlreadyApproved)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El estudiante ya aprobó la asignatura."));
    }
}
