package Group4.StudyHubBackendG4.integration;

import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.services.AutenticacionService;
import Group4.StudyHubBackendG4.util.DatabaseInitializer;
import Group4.StudyHubBackendG4.util.TestUtils;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @SpyBean
    private JavaMailSender javaMailSender;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private AutenticacionService autenticacionService;

    @Autowired
    private DatabaseInitializer databaseInitializer;


    private Usuario user1;
    private Usuario user2;

    private Usuario userNotValidated;

    private String token1;
    private String token2;

    private DtUsuario dtUserModifiedNombreApellidoEmail;
    private DtUsuario dtUserModifiedCedula;

    @BeforeEach
    public void setUp() {
        databaseInitializer.executeSqlScript("src/test/resources/cleanup.sql");

        user1 = testUtils.createUsuario(1, "John", "Doe", "john.doe@example.com", "12345678", "A", "123", true, true);
        user2 = testUtils.createUsuario(2, "Jane", "Smith", "jane.smith@example.com", "87654321", "A", "123", true, true);
        userNotValidated = testUtils.createUsuario(3, "Samba", "Rodriguez", "samba.rodriguez@example.com", "65465465", "E", "123", false, false);

        Docente docente1 = testUtils.createDocente(1001, "Alan Grant", true);
        Docente docente2 = testUtils.createDocente(1002, "Ellen Sattler", true);

        token1 = testUtils.authenticateUser(user1.getCedula(), "123");
        token2 = testUtils.authenticateUser(user2.getCedula(), "123");

        dtUserModifiedNombreApellidoEmail = testUtils.createDtUsuario("Mike", "Tyson", "mike.tyson@example.com", "19980903", "A", "12345678", true, true);
        dtUserModifiedCedula = testUtils.createDtUsuario("Mike", "Tyson", "mike.tyson@example.com", "19980903", "A", "12345678", true, true);

    }

   /* @Test
    public void testModificarOtherUsuarioCedula_Success() throws Exception {
        // Perform request with authenticated token
        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuario/modificarUsuario/{idUsuario}", 2)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtUserModifiedCedula)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario actualizado con exitosamente"));
    }
    */

    @Test
    public void modificarUsuarioSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuario/modificarUsuario/{idUsuario}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtUserModifiedNombreApellidoEmail)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario actualizado con exitosamente"));
    }

    @Test
    public void getUsuariosSuccess() throws Exception {
        mockMvc.perform(get("/api/usuario/getUsuarios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].nombre").value("John"));
    }

    @Test
    public void getDocentesSuccess() throws Exception {
        mockMvc.perform(get("/api/usuario/getDocentes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Alan Grant"));
    }

    @Test
    public void getUsuarioById() throws Exception {
        mockMvc.perform(get("/api/usuario/getUsuario/{idUsuario}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nombre").value("John"));
    }

    @Test
    public void acceptEstudianteSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuario/acceptEstudiante/{idUsuario}", 3)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(true)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario aceptado con exito"));
    }

}
