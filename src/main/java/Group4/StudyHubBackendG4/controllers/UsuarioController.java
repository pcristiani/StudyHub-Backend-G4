package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Validated
@Tag(name = "Usuarios", description = "Endpoints para la operativa de Usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/api/usuario/getUsuarios")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F')")
    @Operation(summary = "Obtiene todos los usuarios")
    public List<DtUsuario> getUsuarios() {
        return usuarioService.getUsuarios();
    }

    @GetMapping("/api/usuario/getDocentes")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F')")
    @Operation(summary = "Obtiene todos los docentes")
    public ResponseEntity<?> getDocentes() {
        return ResponseEntity.ok().body(usuarioService.getDocentes());
    }

    @GetMapping("/api/usuario/getUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    @Operation(summary = "Obtiene un usuario por su id")
    public ResponseEntity<?> getUsuariosById(@PathVariable Integer idUsuario) {
        return usuarioService.getUsuarioById(idUsuario);
    }

    @GetMapping("/api/usuario/getEstudiantesPendientes")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    @Operation(summary = "Obtiene los estudiantes pendientes de aceptación")
    public ResponseEntity<?> getEstudiantesPendientes() {
        List<DtUsuario> dtUsuarios = usuarioService.getEstudiantesPendientes();
        return ResponseEntity.ok().body(dtUsuarios);
    }

    @PutMapping("/api/usuario/acceptEstudiante/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    @Operation(summary = "Acepta o rechaza un estudiante")
    public ResponseEntity<?> acceptEstudiante(@PathVariable Integer idUsuario, @RequestBody Boolean aceptado) throws MessagingException, IOException {
        return usuarioService.acceptEstudiante(idUsuario,aceptado);
    }

    @PostMapping("/registerUsuario")
    @Operation(summary = "Registra un nuevo usuario")
    public ResponseEntity<?> createUsuario(@Valid @RequestBody DtNuevoUsuario dtNuevoUsuario) throws MessagingException, IOException {
        return usuarioService.register(dtNuevoUsuario);
    }

    @PutMapping("/api/usuario/modificarUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A')")
    @Operation(summary = "Modifica un usuario")
    public ResponseEntity<?> modificarUsuario(@PathVariable Integer idUsuario, @RequestBody DtUsuario dtUsuario) {
        return usuarioService.modificarUsuario(idUsuario, dtUsuario);
    }

    @DeleteMapping("/api/usuario/bajaUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A')")
    @Operation(summary = "Da de baja un usuario")
    public ResponseEntity<?> bajaUsuario(@PathVariable Integer idUsuario) {
        return usuarioService.bajaUsuario(idUsuario);
    }

    @PostMapping("/forgotPassword")
    @Operation(summary = "Envio de mail para recuperar contraseña")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) throws MessagingException, IOException, MessagingException, IOException {
        return usuarioService.recuperarPasswordEmail(email);
    }

    @PostMapping("/recuperarPassword")
    @Operation(summary = "Recupera la contraseña de un usuario")
    public ResponseEntity<?> recuperarPassword(@RequestBody DtNewPassword dtNewPassword){
        return usuarioService.recuperarPassword(dtNewPassword.getToken(), dtNewPassword.getNewPassword());
    }

    @PutMapping("/api/usuario/modificarPerfil/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    @Operation(summary = "Modifica el perfil de un usuario")
    public ResponseEntity<?> modificarPerfil(@PathVariable Integer idUsuario, @RequestBody DtPerfil dtPerfil) {
        return usuarioService.modificarPerfil(idUsuario, dtPerfil);
    }

    @PutMapping("/api/usuario/modificarPassword/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    @Operation(summary = "Modifica la contraseña de un usuario")
    public ResponseEntity<?> modificarPassword(@PathVariable Integer idUsuario, @RequestBody String newPassword) {
        return usuarioService.modificarPassword(idUsuario, newPassword);
    }

    @GetMapping("/api/usuario/getResumenActividad/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A')")
    @Operation(summary = "Obtiene el resumen de actividad de un usuario")
    public ResponseEntity<?> getResumenActividad(@PathVariable Integer idUsuario) {
        return usuarioService.getActividadUsuario(idUsuario);
    }

    @GetMapping("/api/docente/getDocentesByAsignaturaId/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    @Operation(summary = "Obtiene los docentes de una asignatura")
    public ResponseEntity<?> getDocentesByAsignaturaId(@PathVariable Integer idAsignatura) {
        return ResponseEntity.ok().body(usuarioService.getDocentesByAsignaturaId(idAsignatura));
    }

    @PostMapping("/api/docente/altaDocente")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    @Operation(summary = "Da de alta un docente")
    public ResponseEntity<?> altaDocente(@Valid @RequestBody DtNuevoDocente dtNuevoDocente) throws MessagingException, IOException {
        return usuarioService.nuevoDocente(dtNuevoDocente);
    }

    @PutMapping("/api/docente/modificarDocente/{idDocente}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    @Operation(summary = "Modifica un docente")
    public ResponseEntity<?> modificarDocente(@PathVariable Integer idDocente, @RequestBody DtDocente dtDocente) {
        return usuarioService.modificarDocente(idDocente, dtDocente);
    }

    @DeleteMapping("/api/docente/bajaDocente/{idDocente}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    @Operation(summary = "Da de baja un docente")
    public ResponseEntity<?> bajaDocente(@PathVariable Integer idDocente) {
        return usuarioService.bajaDocente(idDocente);
    }

    @GetMapping("/api/estudiante/getCalificacionesAsignaturas/{idEstudiante}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E') or hasRole('ROLE_F') or hasRole('ROLE_C')")
    @Operation(summary = "Obtiene las calificaciones de las asignaturas de un estudiante")
    public ResponseEntity<?> getCalificacionesAsignaturas(@PathVariable Integer idEstudiante, @RequestParam Integer idCarrera) {
        return usuarioService.getCalificacionesAsignaturas(idEstudiante, idCarrera);
    }

    @GetMapping("/api/estudiante/getCalificacionesExamenes/{idEstudiante}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E') or hasRole('ROLE_F') or hasRole('ROLE_C')")
    @Operation(summary = "Obtiene las calificaciones de los examenes de un estudiante")
    public ResponseEntity<?> getCalificacionesExamenes(@PathVariable Integer idEstudiante, @RequestParam Integer idCarrera) {
        return usuarioService.getCalificacionesExamenes(idEstudiante, idCarrera);
    }

    @PostMapping("/api/usuario/registerMobileToken/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    @Operation(summary = "Registra un token de dispositivo móvil para un usuario")
    public ResponseEntity<?> registerMobileToken(@PathVariable Integer idUsuario, @Valid @RequestBody String mobileToken) {
        return usuarioService.registerMobileToken(idUsuario, mobileToken);
    }
}
