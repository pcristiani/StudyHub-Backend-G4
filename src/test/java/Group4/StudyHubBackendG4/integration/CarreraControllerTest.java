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
    public void setUp() {
        databaseInitializer.executeSqlScript("src/test/resources/cleanup.sql");
        setUpHelper.setUp();
    }

    @Test
    public void altaCarrera_Ok() throws Exception {
        mockMvc.perform(post("/api/carrera/altaCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaCarrera1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Carrera registrada con éxito."));
    }

    @Test
    public void altaCarrera_BadRequest() throws Exception {
        mockMvc.perform(post("/api/carrera/altaCarrera")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevaCarreraConflict)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya existe una carrera con ese nombre."));
    }

    @Test
    public void asignarCoordinadorCarrera_Ok() throws Exception {
        mockMvc.perform(put("/api/carrera/asignarCoordinadorCarrera/{idCarrera}", setUpHelper.carrera1.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.user6Coordinador.getIdUsuario())))
                .andExpect(status().isOk())
                .andExpect(content().string("Coordinador asignado exitosamente."));
    }


    @Test
    public void asignarCoordinadorCarrera_BadRequest() throws Exception {
        mockMvc.perform(put("/api/carrera/asignarCoordinadorCarrera/{idCarrera}", setUpHelper.carrera1.getIdCarrera())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.user5Coordinador.getIdUsuario())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El coordinador ya está asignado a la carrera."));
    }

}
