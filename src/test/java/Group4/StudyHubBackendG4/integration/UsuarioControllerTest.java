package Group4.StudyHubBackendG4.integration;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.services.AsignaturaService;
import Group4.StudyHubBackendG4.services.AutenticacionService;
import Group4.StudyHubBackendG4.services.ExamenService;
import Group4.StudyHubBackendG4.services.UsuarioService;
import Group4.StudyHubBackendG4.util.DatabaseInitializer;
import Group4.StudyHubBackendG4.util.TestUtils;
import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
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

import java.util.List;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private UsuarioService usuarioService;

    @Autowired
    private AsignaturaService asignaturaService;

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;

    private Usuario user1;
    private Usuario user2;
    private Usuario user4;
    private Docente docente1;
    private Docente docente2;
    private Usuario userNotValidated;
    private String token1;
    private String token2;
    private String token4;
    private Carrera carrera1;
    private Asignatura asignatura1;
    private Asignatura asignatura2;
    private DocenteAsignatura docenteAsignatura1;
    private DocenteAsignatura docenteAsignatura2;
    private DtHorarioDias dtHorarioDias;
    private DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura;
    private DtNuevaInscripcionAsignatura dtNuevaInscripcionAsignatura;
    private DtUsuario dtUserModifiedNombreApellidoEmail;
    private DtUsuario dtUserModifiedCedula;
    private DtDocente dtDocente1;
    private DtNuevoUsuario dtNuevoUsuario;
    private DtNuevoDocente dtNuevoDocente;
    private DtNewPassword dtNewPassword;
    private DtPerfil dtPerfil;

    @BeforeEach
    public void setUp() {
        databaseInitializer.executeSqlScript("src/test/resources/cleanup.sql");

        user1 = testUtils.createUsuario("John", "Doe", "john.doe@example.com", "12345678", "A", "123", true, true);
        user2 = testUtils.createUsuario("Jane", "Smith", "jane.smith@example.com", "87654321", "E", "123", true, true);
        userNotValidated = testUtils.createUsuario("Samba", "Rodriguez", "samba.rodriguez@example.com", "65465465", "E", "123", false, false);
        user4 = testUtils.createUsuario("Betty", "Brown", "betty.brown@example.com", "789654123", "E", "123", true, true);

        docente1 = testUtils.createDocente(1001, "Alan Grant", true);
        docente2 = testUtils.createDocente(1002, "Ellen Sattler", true);

        token1 = testUtils.authenticateUser(user1.getCedula(), "123");
        token2 = testUtils.authenticateUser(user2.getCedula(), "123");
        token4 = testUtils.authenticateUser(user4.getCedula(), "123");
        autenticacionService.logoutUser(token2);

        carrera1 = testUtils.createCarrera("Ingeniería Informática", "Descripción de la carrera", "Requisitos de ingreso", 5, true);
        asignatura1 = testUtils.createAsignatura(carrera1, "Programacion 1", 12, "Descripcion", "Informatica", false, true);
        asignatura2 = testUtils.createAsignatura(carrera1, "Sistemas Operativos", 12, "Descripcion", "Informatica", false, true);
        docenteAsignatura1 = testUtils.createDocenteAsignatura(docente1, asignatura1);
        docenteAsignatura2 = testUtils.createDocenteAsignatura(docente2, asignatura2);
        docenteAsignatura2 = testUtils.createDocenteAsignatura(docente2, asignatura2);

        for (int i = 0; i < 10; i++) {
            testUtils.createActividad(user4);
        }

        dtUserModifiedNombreApellidoEmail = testUtils.createDtUsuario("Mike", "Tyson", "mike.tyson@example.com", "19980903", "A", "12345678", true, true);
        dtUserModifiedCedula = testUtils.createDtUsuario("Mike", "Tyson", "mike.tyson@example.com", "19980903", "A", "654654321", true, true);

        dtNuevoUsuario = testUtils.createDtNuevoUsuario("Michael", "Jordan", "michael.jordan@example.com", "1985-02-17", "23456789", "123", "ROLE_A");
        dtNuevoDocente = new DtNuevoDocente(1003, "Jack Johnson");
        dtDocente1 = new DtDocente(null, 1006, "Marcelo Tinelli", true);
        dtPerfil = new DtPerfil("Andrew", "Dornen", "andrew.dornen@example.com", "19960606");

        dtHorarioDias = new DtHorarioDias(DiaSemana.LUNES, "10:30","12:30");
        dtNuevoHorarioAsignatura = new DtNuevoHorarioAsignatura(docente1.getIdDocente(), 2022, List.of(dtHorarioDias));
        asignaturaService.registroHorarios(asignatura1.getIdAsignatura(), dtNuevoHorarioAsignatura);
        dtNuevaInscripcionAsignatura = new DtNuevaInscripcionAsignatura(user2.getIdUsuario(), asignatura1.getIdAsignatura(), 1);
        asignaturaService.inscripcionAsignatura(dtNuevaInscripcionAsignatura);


        //TODO: Fix
        usuarioService.generatePasswordResetToken(user1);
        dtNewPassword = new DtNewPassword();
        dtNewPassword.setToken(String.valueOf(passwordResetTokenRepo.findAll().get(0)));
        dtNewPassword.setNewPassword("newPass123");
    }

    @Test
    public void modificarUsuarioOk() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarUsuario/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtUserModifiedNombreApellidoEmail)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario actualizado con exitosamente"));
    }

    @Test
    public void testModificarOtherUsuarioCedula_Ok() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarUsuario/{idUsuario}", user2.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtUserModifiedCedula)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario actualizado con exitosamente"));
    }

   @Test
    public void testModificarOtherUsuarioCedula_Conflict() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarUsuario/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtUserModifiedCedula)))
                .andExpect(status().isConflict())
                .andExpect(content().string("No se puede modificar la cedula porque el usuario tiene una sesion activa."));
    }

    @Test
    public void getUsuariosOk() throws Exception {
        mockMvc.perform(get("/api/usuario/getUsuarios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].nombre").value("John"));
    }

    @Test
    public void getDocentesOk() throws Exception {
        mockMvc.perform(get("/api/usuario/getDocentes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Alan Grant"));
    }

    @Test
    public void getUsuarioByIdOk() throws Exception {
        mockMvc.perform(get("/api/usuario/getUsuario/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nombre").value("John"));
    }

    @Test
    public void acceptEstudianteOk() throws Exception {
        mockMvc.perform(put("/api/usuario/acceptEstudiante/{idUsuario}", userNotValidated.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(true)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario aceptado con exito"));
    }

    @Test
    public void getEstudiantesPendientesOk() throws Exception {
        mockMvc.perform(get("/api/usuario/getEstudiantesPendientes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Samba"))
                .andExpect(jsonPath("$[0].apellido").value("Rodriguez"));
    }

    @Test
    public void createUsuarioOk() throws Exception {
        mockMvc.perform(post("/registerUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtNuevoUsuario)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado con éxito."));
    }

    @Test
    public void bajaUsuarioOk() throws Exception {
        mockMvc.perform(delete("/api/usuario/bajaUsuario/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario desactivado exitosamente."));

    }

    @Test
    public void forgotPasswordOk() throws Exception {
        mockMvc.perform(post("/forgotPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user2.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("Email enviado."));
    }

     /*
    @Test
    public void recuperarPasswordOk() throws Exception {       //Todo: Fix reset token
        mockMvc.perform(post("/recuperarPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtNewPassword)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));
    }

     */

    @Test
    public void modificarPerfilOk() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarPerfil/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtPerfil)))
                .andExpect(status().isOk())
                .andExpect(content().string("Perfil modificado exitosamente."));

    }

    @Test
    public void modificarPasswordOk() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarPassword/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtNewPassword.getNewPassword())))
                .andExpect(status().isOk())
                .andExpect(content().string("Contraseña modificada exitosamente."));
    }

    @Test
    public void getResumenActividadOk() throws Exception {
        mockMvc.perform(get("/api/usuario/getResumenActividad/{idUsuario}", user4.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()) // Expecting response to be a JSON array
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(10))) // At least 10 activities
                .andExpect(jsonPath("$[0].accion").value("Inicio de sesion")); // First activity action is "inicio de sesion"
    }

    @Test
    public void getResumenActividadNotEnoughActividad() throws Exception {
        mockMvc.perform(get("/api/usuario/getResumenActividad/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No hay suficiente información para generar el resumen de actividades"));

    }

    @Test
    public void getDocentesByAsignaturaIdOk() throws Exception {
        mockMvc.perform(get("/api/docente/getDocentesByAsignaturaId/{idAsignatura}", docente1.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Alan Grant"));
    }

    @Test
    public void altaDocenteSuccess() throws Exception {
        mockMvc.perform(post("/api/docente/altaDocente")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtNuevoDocente)))
                .andExpect(status().isOk())
                .andExpect(content().string("Docente registrado con éxito."));
    }

    @Test
    public void modificarDocenteSuccess() throws Exception {
        mockMvc.perform(put("/api/docente/modificarDocente/{idDocente}", docente1.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtDocente1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Docente actualizado exitosamente"));
    }

    @Test
    public void bajaDocenteSuccess() throws Exception {
        mockMvc.perform(delete("/api/docente/bajaDocente/{idDocente}", docente1.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Docente desactivado exitosamente."));
    }

    @Test
    public void getCalificacionesAsignaturasSuccess() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", user2.getIdUsuario())
                        .param("idCarrera", carrera1.getIdCarrera().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].calificaciones[0].resultado").value("PENDIENTE"));  // Replace EXPECTED_RESULT_VALUE with the actual expected value for `resultado`

    }



}
