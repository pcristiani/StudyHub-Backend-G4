package Group4.StudyHubBackendG4.integration;

import Group4.StudyHubBackendG4.util.DatabaseInitializer;
import Group4.StudyHubBackendG4.util.SetUpHelper;
import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AsignaturaControllerTest {

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
    public void altaAsignatura_SinPreviasOk() throws Exception {
        mockMvc.perform(post("/api/asignatura/altaAsignatura")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaAsignatura1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Asignatura creada exitosamente."));
    }

    @Test
    public void altaAsignatura_CarreraNotFound() throws Exception {
        mockMvc.perform(post("/api/asignatura/altaAsignatura")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaAsignaturaConflict4)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Carrera no encontrada."));
    }

    @Test
    public void altaAsignatura_NoDocentes() throws Exception {
        mockMvc.perform(post("/api/asignatura/altaAsignatura")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaAsignaturaConflict2)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ingrese al menos un docente."));
    }

    @Test
    public void altaAsignatura_DocenteNotFound() throws Exception {
        mockMvc.perform(post("/api/asignatura/altaAsignatura")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaAsignaturaConflict3)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Uno o más docentes no encontrados."));
    }

    @Test
    public void altaAsignatura_AsignaturaAlreadyExists() throws Exception {
        mockMvc.perform(post("/api/asignatura/altaAsignatura")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaAsignaturaConflict1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La asignatura ya existe."));
    }

    @Test
    public void altaAsignatura_PreviaNotFound() throws Exception {
        mockMvc.perform(post("/api/asignatura/altaAsignatura")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaAsignaturaConflict5)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Previa asignatura no encontrada"));
    }

    @Test
    public void altaAsignatura_PreviaNotSameCarrera() throws Exception {
        mockMvc.perform(post("/api/asignatura/altaAsignatura")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaAsignaturaConflict6)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Todas las previaturas deben pertenecer a la misma carrera."));
    }

    @Test
    public void registroHorarios_Ok() throws Exception {
        mockMvc.perform(post("/api/asignatura/registroHorarios/{idAsignatura}", setUpHelper.asignatura1.getIdAsignatura())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoHorarioAsignatura3)))
                .andExpect(status().isOk())
                .andExpect(content().string("Horarios registrados satisfactoriamente."));
    }

    @Test
    public void registroHorarios_InvalidIdAsignatura() throws Exception {
        mockMvc.perform(post("/api/asignatura/registroHorarios/{idAsignatura}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoHorarioAsignatura2)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("idAsignatura invalido"));
    }

    @Test
    public void registroHorarios_InvalidIdDocente() throws Exception {
        mockMvc.perform(post("/api/asignatura/registroHorarios/{idAsignatura}", setUpHelper.asignatura1.getIdAsignatura())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoHorarioAsignaturaInvalidDocente)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("idDocente invalido"));
    }

    @Test
    public void registroHorarios_InvalidTimeFormat() throws Exception {
        mockMvc.perform(post("/api/asignatura/registroHorarios/{idAsignatura}", setUpHelper.asignatura1.getIdAsignatura())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoHorarioAsignaturaInvalidTimeFormat1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Formato de hora inválido. Use HH:mm"));
    }

    @Test
    public void registroHorarios_HoraInicioAfterHoraFin() throws Exception {
        mockMvc.perform(post("/api/asignatura/registroHorarios/{idAsignatura}", setUpHelper.asignatura1.getIdAsignatura())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoHorarioAsignaturaInvalidTimeFormat2)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("horaInicio debe ser menor a horaFin, y los valores deben ser válidos (por ejemplo, 10:30 para 10:30)"));
    }
    @Test
    public void registroHorarios_HorariosSuperpuestos() throws Exception {
        mockMvc.perform(post("/api/asignatura/registroHorarios/{idAsignatura}", setUpHelper.asignatura1.getIdAsignatura())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoHorarioAsignaturaInvalidTimeFormat3)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Horarios superpuestos detectados para el dia " + DiaSemana.LUNES));
    }

    @Test
    public void inscripcionAsignatura_Ok() throws Exception {
        mockMvc.perform(post("/api/asignatura/inscripcionAsignatura")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaInscripcionAsignatura3)))
                .andExpect(status().isOk())
                .andExpect(content().string("Se realizó la inscripcion a la asignatura"));
    }

    @Test
    public void inscripcionAsignatura_UsuarioNotInscriptoACarrera() throws Exception {
        mockMvc.perform(post("/api/asignatura/inscripcionAsignatura")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaInscripcionAsignatura2)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El usuario no está inscripto en la carrera correspondiente a la asignatura"));
    }

    @Test
      public void registrarPreviaturas_Ok() throws Exception {
        mockMvc.perform(post("/api/asignatura/registrarPreviaturas/{idAsignatura}", setUpHelper.asignatura4.getIdAsignatura())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(setUpHelper.asignatura2.getIdAsignatura()))))
                .andExpect(status().isOk())
                .andExpect(content().string("Previaturas registradas exitosamente."));
    }

    @Test
    public void registrarPreviaturas_AsignaturaNotFound() throws Exception {
        mockMvc.perform(post("/api/asignatura/registrarPreviaturas/{idAsignatura}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(setUpHelper.asignatura2.getIdAsignatura()))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Asignatura no encontrada."));
    }

    @Test
    public void registrarPreviaturas_PreviaAsignaturaNotFound() throws Exception {
        mockMvc.perform(post("/api/asignatura/registrarPreviaturas/{idAsignatura}", setUpHelper.asignatura4.getIdAsignatura())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(60))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Una o más previas no encontradas."));
    }

    @Test
    public void registrarPreviaturas_PreviaturasCirculares() throws Exception {
        mockMvc.perform(post("/api/asignatura/registrarPreviaturas/{idAsignatura}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(6))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Existen circularidades en las previaturas seleccionadas."));
    }

    @Test
    public void cambiarResultadoCursada_Ok() throws Exception {
        mockMvc.perform(post("/api/asignatura/cambiarResultadoCursada/{idCursada}", 1)
                        .param("calificacion", String.valueOf(12))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Resultado de la cursada con ID " + 1 + " cambiado exitosamente a " + ResultadoAsignatura.EXONERADO));
    }

    @Test
    public void cambiarResultadoCursada_CursadaNotFound() throws Exception {
        mockMvc.perform(post("/api/asignatura/cambiarResultadoCursada/{idCursada}", 60)
                        .param("calificacion", String.valueOf(12))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se encontró la cursada."));
    }

    @Test
    public void getCursadasPendientes_Ok() throws Exception {
        mockMvc.perform(get("/api/asignatura/cursadasPendientes")
                        .param("anio", String.valueOf(2022))
                        .param("idAsignatura", String.valueOf(1))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombreEstudiante").value("Jane"))
                .andExpect(jsonPath("$[0].apellidoEstudiante").value("Smith"))
                .andExpect(jsonPath("$[0].mailEstudiante").value("jane.smith@example.com"));
    }

    @Test
    public void getHorarios_Ok() throws Exception {
        mockMvc.perform(get("/api/asignatura/getHorarios/{idAsignatura}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idHorarioAsignatura").value(1))
                .andExpect(jsonPath("$[0].idAsignatura").value(1))
                .andExpect(jsonPath("$[0].anio").value(2022))
                .andExpect(jsonPath("$[0].dtHorarioDias").isArray())
                .andExpect(jsonPath("$[0].dtHorarioDias[0].diaSemana").value("LUNES"))
                .andExpect(jsonPath("$[0].dtHorarioDias[0].horaInicio").value("10:30"))
                .andExpect(jsonPath("$[0].dtHorarioDias[0].horaFin").value("12:30"));
    }

    @Test
    public void getAsignaturasNoAprobadas_Ok() throws Exception {
        mockMvc.perform(get("/api/asignatura/getAsignaturasNoAprobadas/{idEstudiante}", 2)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Principios de Programación"));
    }

    @Test
    public void getPreviasAsignatura_Ok() throws Exception {
        mockMvc.perform(get("/api/asignatura/getPreviasAsignatura/{idAsignatura}", 2)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Principios de Programación"));
    }

    @Test
    public void getPreviasAsignatura_AsignaturaNotFound() throws Exception {
        mockMvc.perform(get("/api/asignatura/getPreviasAsignatura/{idAsignatura}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Asignatura no encontrada."));
    }

    @Test
    public void getPreviasAsignatura_AsignaturaHasNoPrevias() throws Exception {
        mockMvc.perform(get("/api/asignatura/getPreviasAsignatura/{idAsignatura}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("La asignatura no tiene previas."));
    }

    @Test
    public void getNoPreviasAsignatura_Ok() throws Exception {
        mockMvc.perform(get("/api/asignatura/getNoPreviasAsignatura/{idAsignatura}", 2)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Ingenieria de Software"))
                .andExpect(jsonPath("$[1].nombre").value("Matematica Discreta"))
                .andExpect(jsonPath("$[2].nombre").value("Programación 3"));
    }

    @Test
    public void getNoPreviasAsignatura_AsignaturaNotFound() throws Exception {
        mockMvc.perform(get("/api/asignatura/getNoPreviasAsignatura/{idAsignatura}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Asignatura no encontrada."));
    }

    @Test
    public void getActa_Ok() throws Exception {
        mockMvc.perform(get("/api/asignatura/getActa/{idHorario}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiantes").isArray())
                .andExpect(jsonPath("$.estudiantes[0].nombre").value("Jane"))
                .andExpect(jsonPath("$.estudiantes[0].email").value("jane.smith@example.com"))
                .andExpect(jsonPath("$.docentes").isArray())
                .andExpect(jsonPath("$.docentes[0].nombre").value("Ellen Sattler"))
                .andExpect(jsonPath("$.asignatura").value("Principios de Programación"))
                .andExpect(jsonPath("$.horarioAsignatura.anio").value(2022));
    }


}
