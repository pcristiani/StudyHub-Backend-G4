package Group4.StudyHubBackendG4.integration;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.services.AsignaturaService;
import Group4.StudyHubBackendG4.services.AutenticacionService;
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
    private Carrera carrera2;
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
    private DtDocente dtDocente2;
    private DtNuevoUsuario dtNuevoUsuario;
    private DtNuevoDocente dtNuevoDocente1;
    private DtNuevoDocente dtNuevoDocente2;
    private DtNewPassword dtNewPassword;
    private DtPerfil dtPerfil1;
    private DtPerfil dtPerfil2;

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
        carrera2 = testUtils.createCarrera("Traductorado", "Descripción de la carrera", "Requisitos de ingreso", 2, true);
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
        dtNuevoDocente1 = new DtNuevoDocente(1003, "Jack Johnson");
        dtNuevoDocente2 = new DtNuevoDocente(1001, "Jack Johnson");
        dtDocente1 = new DtDocente(null, 1006, "Marcelo Tinelli", true);
        dtDocente2 = new DtDocente(null, 1001, "Marcelo Tinelli", true);
        dtPerfil1 = new DtPerfil("Andrew", "Dornen", "andrew.dornen@example.com", "19960606");
        dtPerfil2 = new DtPerfil("Andrew", "Dornen", "john.doe@example.com", "19960606");

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
    public void modificarUsuario_Ok() throws Exception {
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
    public void getUsuarios_Ok() throws Exception {
        mockMvc.perform(get("/api/usuario/getUsuarios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].nombre").value("John"));
    }

    @Test
    public void getDocentes_Ok() throws Exception {
        mockMvc.perform(get("/api/usuario/getDocentes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Alan Grant"));
    }

    @Test
    public void getUsuarioById_Ok() throws Exception {
        mockMvc.perform(get("/api/usuario/getUsuario/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nombre").value("John"));
    }

    @Test
    public void acceptEstudiante_Ok() throws Exception {
        mockMvc.perform(put("/api/usuario/acceptEstudiante/{idUsuario}", userNotValidated.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(true)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario aceptado con exito"));
    }

    @Test
    public void acceptEstudiante_BadRequest() throws Exception {
        mockMvc.perform(put("/api/usuario/acceptEstudiante/{idUsuario}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(true)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usuario no existe en el sistema."));
    }

    @Test
    public void getEstudiantesPendientes_Ok() throws Exception {
        mockMvc.perform(get("/api/usuario/getEstudiantesPendientes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Samba"))
                .andExpect(jsonPath("$[0].apellido").value("Rodriguez"));
    }

    @Test
    public void createUsuario_Ok() throws Exception {
        mockMvc.perform(post("/registerUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtNuevoUsuario)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado con éxito."));
    }

    @Test
    public void bajaUsuario_Ok() throws Exception {
        mockMvc.perform(delete("/api/usuario/bajaUsuario/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario desactivado exitosamente."));

    }

    @Test
    public void bajaUsuario_NotFound() throws Exception {
        mockMvc.perform(delete("/api/usuario/bajaUsuario/{idUsuario}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id no existe o usuario ya inactivo."));

    }

    @Test
    public void forgotPassword_Ok() throws Exception {
        mockMvc.perform(post("/forgotPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user2.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("Email enviado."));
    }

    @Test
    public void forgotPassword_InvalidEmail() throws Exception {
        mockMvc.perform(post("/forgotPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("invalidEmail@example.com")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid email."));
    }

     /*
    @Test
    public void recuperarPassword_Ok() throws Exception {       //Todo: Fix reset token
        mockMvc.perform(post("/recuperarPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtNewPassword)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));
    }

     */

    @Test
    public void modificarPerfil_Ok() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarPerfil/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtPerfil1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Perfil modificado exitosamente."));

    }

    @Test
    public void modificarPerfil_NotFound() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarPerfil/{idUsuario}", user2.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtPerfil2)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ese email ya esta en uso."));

    }

    @Test
    public void modificarPassword_Ok() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarPassword/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtNewPassword.getNewPassword())))
                .andExpect(status().isOk())
                .andExpect(content().string("Contraseña modificada exitosamente."));
    }

    @Test
    public void modificarPassword_NotFound() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarPassword/{idUsuario}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtNewPassword.getNewPassword())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró usuario."));
    }

    @Test
    public void getResumenActividad_Ok() throws Exception {
        mockMvc.perform(get("/api/usuario/getResumenActividad/{idUsuario}", user4.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()) // Expecting response to be a JSON array
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(10))) // At least 10 activities
                .andExpect(jsonPath("$[0].accion").value("Inicio de sesion")); // First activity action is "inicio de sesion"
    }

    @Test
    public void getResumenActividad_NoActividad() throws Exception {
        mockMvc.perform(get("/api/usuario/getResumenActividad/{idUsuario}", userNotValidated.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron actividades para el usuario"));
    }

    @Test
    public void getResumenActividadUsuario_NotFound() throws Exception {
        mockMvc.perform(get("/api/usuario/getResumenActividad/{idUsuario}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró el usuario"));
    }

    @Test
    public void getResumenActividad_NotEnoughActividad() throws Exception {
        mockMvc.perform(get("/api/usuario/getResumenActividad/{idUsuario}", user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No hay suficiente información para generar el resumen de actividades"));
    }

    @Test
    public void getDocentesByAsignaturaId_Ok() throws Exception {
        mockMvc.perform(get("/api/docente/getDocentesByAsignaturaId/{idAsignatura}", docente1.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Alan Grant"));
    }

    @Test
    public void altaDocente_Success() throws Exception {
        mockMvc.perform(post("/api/docente/altaDocente")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtNuevoDocente1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Docente registrado con éxito."));
    }

    @Test
    public void altaDocente_BadRequest() throws Exception {
        mockMvc.perform(post("/api/docente/altaDocente")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtNuevoDocente2)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya existe ese codigo de docente."));
    }

    @Test
    public void modificarDocente_Success() throws Exception {
        mockMvc.perform(put("/api/docente/modificarDocente/{idDocente}", docente1.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtDocente1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Docente actualizado exitosamente"));
    }

    @Test
    public void modificarDocente_NotFound() throws Exception {
        mockMvc.perform(put("/api/docente/modificarDocente/{idDocente}", docente2.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtDocente2)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ya existe un docente con ese codigo."));
    }

    @Test
    public void bajaDocente_Success() throws Exception {
        mockMvc.perform(delete("/api/docente/bajaDocente/{idDocente}", docente1.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Docente desactivado exitosamente."));
    }

    @Test
    public void bajaDocente_BadRequest() throws Exception {
        mockMvc.perform(delete("/api/docente/bajaDocente/{idDocente}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Id no existe."));
    }

    @Test
    public void getCalificacionesAsignaturas_Success() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", user2.getIdUsuario())
                        .param("idCarrera", carrera1.getIdCarrera().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].calificaciones[0].resultado").value("PENDIENTE"));
    }

    @Test
    public void getCalificacionesAsignaturas_CarreraNotFound() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", user2.getIdUsuario())
                        .param("idCarrera", "60")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró la carrera."));
    }

    @Test
    public void getCalificacionesAsignaturas_EstudianteNotFound() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", 60)
                        .param("idCarrera", carrera1.getIdCarrera().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró el estudiante."));
    }

    @Test
    public void getCalificacionesAsignaturas_CursadaNotFound() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", user4.getIdUsuario())
                        .param("idCarrera", carrera1.getIdCarrera().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron cursadas del estudiante."));
    }

    @Test
    public void getCalificacionesAsignaturas_AsignaturasNotFound() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", user2.getIdUsuario())
                        .param("idCarrera", carrera2.getIdCarrera().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron asignaturas para la carrera."));
    }

}
