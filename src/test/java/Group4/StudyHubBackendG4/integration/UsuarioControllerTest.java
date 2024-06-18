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
public class UsuarioControllerTest {

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
    public void modificarUsuario_Ok() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarUsuario/{idUsuario}", setUpHelper.user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtUserModifiedNombreApellidoEmail)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario actualizado con exitosamente"));
    }

    @Test
    public void testModificarOtherUsuarioCedula_Ok() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarUsuario/{idUsuario}", setUpHelper.user2.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtUserModifiedCedula)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario actualizado con exitosamente"));
    }

   @Test
    public void testModificarOtherUsuarioCedula_Conflict() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarUsuario/{idUsuario}", setUpHelper.user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtUserModifiedCedula)))
                .andExpect(status().isConflict())
                .andExpect(content().string("No se puede modificar la cedula porque el usuario tiene una sesion activa."));
    }

    @Test
    public void getUsuarios_Ok() throws Exception {
        mockMvc.perform(get("/api/usuario/getUsuarios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].nombre").value("John"));
    }

    @Test
    public void getDocentes_Ok() throws Exception {
        mockMvc.perform(get("/api/usuario/getDocentes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Alan Grant"));
    }

    @Test
    public void getUsuarioById_Ok() throws Exception {
        mockMvc.perform(get("/api/usuario/getUsuario/{idUsuario}", setUpHelper.user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nombre").value("John"));
    }

    @Test
    public void acceptEstudiante_Ok() throws Exception {
        mockMvc.perform(put("/api/usuario/acceptEstudiante/{idUsuario}", setUpHelper.userNotValidated.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(true)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario aceptado con exito"));
    }

    @Test
    public void acceptEstudiante_BadRequest() throws Exception {
        mockMvc.perform(put("/api/usuario/acceptEstudiante/{idUsuario}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(true)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usuario no existe en el sistema."));
    }

    @Test
    public void getEstudiantesPendientes_Ok() throws Exception {
        mockMvc.perform(get("/api/usuario/getEstudiantesPendientes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
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
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoUsuario)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado con éxito."));
    }

    @Test
    public void bajaUsuario_Ok() throws Exception {
        mockMvc.perform(delete("/api/usuario/bajaUsuario/{idUsuario}", setUpHelper.user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario desactivado exitosamente."));

    }

    @Test
    public void bajaUsuario_NotFound() throws Exception {
        mockMvc.perform(delete("/api/usuario/bajaUsuario/{idUsuario}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id no existe o usuario ya inactivo."));

    }

    @Test
    public void forgotPassword_Ok() throws Exception {
        mockMvc.perform(post("/forgotPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(setUpHelper.user2.getEmail()))
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

    @Test
    public void modificarPerfil_Ok() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarPerfil/{idUsuario}", setUpHelper.user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtPerfil1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Perfil modificado exitosamente."));

    }

    @Test
    public void modificarPerfil_NotFound() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarPerfil/{idUsuario}", setUpHelper.user2.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtPerfil2)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ese email ya esta en uso."));

    }

    @Test
    public void modificarPassword_Ok() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarPassword/{idUsuario}", setUpHelper.user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNewPassword.getNewPassword())))
                .andExpect(status().isOk())
                .andExpect(content().string("Contraseña modificada exitosamente."));
    }

    @Test
    public void modificarPassword_NotFound() throws Exception {
        mockMvc.perform(put("/api/usuario/modificarPassword/{idUsuario}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNewPassword.getNewPassword())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró usuario."));
    }

    @Test
    public void getResumenActividad_Ok() throws Exception {
        mockMvc.perform(get("/api/usuario/getResumenActividad/{idUsuario}", setUpHelper.user4.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()) // Expecting response to be a JSON array
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(10))) // At least 10 activities
                .andExpect(jsonPath("$[0].accion").value("Inicio de sesion")); // First activity action is "inicio de sesion"
    }

    @Test
    public void getResumenActividad_NoActividad() throws Exception {
        mockMvc.perform(get("/api/usuario/getResumenActividad/{idUsuario}", setUpHelper.userNotValidated.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron actividades para el usuario"));
    }

    @Test
    public void getResumenActividadUsuario_NotFound() throws Exception {
        mockMvc.perform(get("/api/usuario/getResumenActividad/{idUsuario}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró el usuario"));
    }

    @Test
    public void getResumenActividad_NotEnoughActividad() throws Exception {
        mockMvc.perform(get("/api/usuario/getResumenActividad/{idUsuario}", setUpHelper.user1.getIdUsuario())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No hay suficiente información para generar el resumen de actividades"));
    }

    @Test
    public void getDocentesByAsignaturaId_Ok() throws Exception {
        mockMvc.perform(get("/api/docente/getDocentesByAsignaturaId/{idAsignatura}", setUpHelper.docente1.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Alan Grant"));
    }

    @Test
    public void altaDocente_Success() throws Exception {
        mockMvc.perform(post("/api/docente/altaDocente")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoDocente1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Docente registrado con éxito."));
    }

    @Test
    public void altaDocente_BadRequest() throws Exception {
        mockMvc.perform(post("/api/docente/altaDocente")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtNuevoDocente2)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya existe ese codigo de docente."));
    }

    @Test
    public void modificarDocente_Success() throws Exception {
        mockMvc.perform(put("/api/docente/modificarDocente/{idDocente}", setUpHelper.docente1.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtDocente1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Docente actualizado exitosamente"));
    }

    @Test
    public void modificarDocente_NotFound() throws Exception {
        mockMvc.perform(put("/api/docente/modificarDocente/{idDocente}", setUpHelper.docente2.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setUpHelper.dtDocente2)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ya existe un docente con ese codigo."));
    }

    @Test
    public void bajaDocente_Success() throws Exception {
        mockMvc.perform(delete("/api/docente/bajaDocente/{idDocente}", setUpHelper.docente1.getIdDocente())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Docente desactivado exitosamente."));
    }

    @Test
    public void bajaDocente_BadRequest() throws Exception {
        mockMvc.perform(delete("/api/docente/bajaDocente/{idDocente}", 60)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Id no existe."));
    }

    @Test
    public void getCalificacionesAsignaturas_Success() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", setUpHelper.user2.getIdUsuario())
                        .param("idCarrera", setUpHelper.carrera1.getIdCarrera().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].calificaciones[0].resultado").value("PENDIENTE"));
    }

    @Test
    public void getCalificacionesAsignaturas_CarreraNotFound() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", setUpHelper.user2.getIdUsuario())
                        .param("idCarrera", "60")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró la carrera."));
    }

    @Test
    public void getCalificacionesAsignaturas_EstudianteNotFound() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", 60)
                        .param("idCarrera", setUpHelper.carrera1.getIdCarrera().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró el estudiante."));
    }

    @Test
    public void getCalificacionesAsignaturas_CursadaNotFound() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", setUpHelper.user4.getIdUsuario())
                        .param("idCarrera", setUpHelper.carrera1.getIdCarrera().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron cursadas del estudiante."));
    }

    @Test
    public void getCalificacionesAsignaturas_AsignaturasNotFound() throws Exception {
        mockMvc.perform(get("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}", setUpHelper.user2.getIdUsuario())
                        .param("idCarrera", setUpHelper.carrera2.getIdCarrera().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpHelper.token1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron asignaturas para la carrera."));
    }

}
