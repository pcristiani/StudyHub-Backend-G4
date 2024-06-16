package Group4.StudyHubBackendG4.integration;

import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.ActividadRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
import Group4.StudyHubBackendG4.services.AutenticacionService;
import Group4.StudyHubBackendG4.services.PasswordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutenticacionService autenticacionService;

    private String token1;
    private String token2;

    private DtUsuario dtUser;


    @BeforeEach
    public void setUp() {
        Usuario user1 = new Usuario();
        user1.setIdUsuario(1);
        user1.setNombre("John");
        user1.setApellido("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setFechaNacimiento("19980903");
        user1.setRol("A");
        user1.setPassword(PasswordService.getInstance().hashPassword("123"));
        user1.setCedula("12345678");
        user1.setActivo(true);
        user1.setValidado(true);
        usuarioRepo.save(user1);

        Usuario user2 = new Usuario();
        user2.setIdUsuario(2);
        user2.setNombre("Jane");
        user2.setApellido("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setFechaNacimiento("19980903");
        user2.setRol("A");
        user2.setPassword(PasswordService.getInstance().hashPassword("123"));
        user2.setCedula("87654321");
        user2.setActivo(true);
        user2.setValidado(true);
        usuarioRepo.save(user2);

        token1 = autenticacionService.authenticateUser(user1.getCedula(), "123");
        token2 = autenticacionService.authenticateUser(user2.getCedula(), "123");
        autenticacionService.logoutUser(token2);

        dtUser = new DtUsuario();
        dtUser.setNombre("Mike");
        dtUser.setApellido("Tyson");
        dtUser.setCedula("98765432");
        dtUser.setEmail("janesmith@example.com");

    }

    @Test
    public void testModificarOtherUsuarioCedula_Success() throws Exception {
        // Perform request with authenticated token
        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuario/modificarUsuario/{idUsuario}", 2)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario actualizado con exitosamente"));
    }


}
