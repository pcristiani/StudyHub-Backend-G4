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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AutenticacionControllerTest {

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
    public void testLoginSuccess() throws Exception {
       mockMvc.perform(post("/iniciarSesion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtLoginRequest1)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginNotJoined() throws Exception {
        mockMvc.perform(post("/iniciarSesion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtLoginRequest2)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("El usuario no esta validado o se encuentra inactivo."));
    }

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        mockMvc.perform(post("/iniciarSesion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtLoginRequest3)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Usuario o contraseña incorrectos"));
    }

    @Test
    public void testLogoutSuccess() throws Exception {
        mockMvc.perform(post("/cerrarSesion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(setUpHelper.token1))
                .andExpect(status().isOk())
                .andExpect(content().string("Cerro sesion correctamente."));
    }

}
