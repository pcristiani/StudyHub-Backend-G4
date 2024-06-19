package Group4.StudyHubBackendG4.integration;

import Group4.StudyHubBackendG4.util.DatabaseInitializer;
import Group4.StudyHubBackendG4.util.SetUpHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void setUp() {
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


}
