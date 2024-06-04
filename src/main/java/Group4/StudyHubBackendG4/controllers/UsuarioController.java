package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.services.UsuarioService;
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
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuario/getUsuarios")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F')")
    public List<DtUsuario> getUsuarios() {
        return usuarioService.getUsuarios();
    }

    @GetMapping("/usuario/getDocentes")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getDocentes() {
        return ResponseEntity.ok().body(usuarioService.getDocentes());
    }

    @GetMapping("/usuario/getUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getUsuariosById(@PathVariable Integer idUsuario) {
        return usuarioService.getUsuarioById(idUsuario);
    }

    @GetMapping("/usuario/getEstudiantesPendientes")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getEstudiantesPendientes() {
        List<DtUsuario> dtUsuarios = usuarioService.getEstudiantesPendientes();
        return ResponseEntity.ok().body(dtUsuarios);
    }

    @PutMapping("/usuario/acceptEstudiante/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> acceptEstudiante(@PathVariable Integer idUsuario, @RequestBody Boolean aceptado) throws MessagingException, IOException {
        return usuarioService.acceptEstudiante(idUsuario,aceptado);
    }

    @PostMapping("/usuario/registerUsuario")
    public ResponseEntity<?> createUsuario(@Valid @RequestBody DtNuevoUsuario dtNuevoUsuario) throws MessagingException, IOException {
        return usuarioService.register(dtNuevoUsuario);
    }

    @PutMapping("/usuario/modificarUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseEntity<?> modificarUsuario(@PathVariable Integer idUsuario, @RequestBody DtUsuario dtUsuario) {
        return usuarioService.modificarUsuario(idUsuario, dtUsuario);
    }

    @DeleteMapping("/usuario/bajaUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseEntity<?> bajaUsuario(@PathVariable Integer idUsuario) {
        return usuarioService.bajaUsuario(idUsuario);
    }

    @PostMapping("/usuario/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) throws MessagingException, IOException, MessagingException, IOException {
        return usuarioService.recuperarPasswordEmail(email);
    }

    @PostMapping("/usuario/recuperarPassword")
    public ResponseEntity<?> recuperarPassword(@RequestBody DtNewPassword dtNewPassword){
        return usuarioService.recuperarPassword(dtNewPassword.getToken(), dtNewPassword.getNewPassword());
    }

    @PutMapping("/usuario/modificarPerfil/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> modificarPerfil(@PathVariable Integer idUsuario, @RequestBody DtPerfil dtPerfil) {
        return usuarioService.modificarPerfil(idUsuario, dtPerfil);
    }

    @PutMapping("/usuario/modificarPassword/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> modificarPassword(@PathVariable Integer idUsuario, @RequestBody String newPassword) {
        return usuarioService.modificarPassword(idUsuario, newPassword);
    }

    @GetMapping("/api/usuario/getResumenActividad/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseEntity<?> getResumenActividad(@PathVariable Integer idUsuario) {
        return usuarioService.getActividadUsuario(idUsuario);
    }

    @GetMapping("/docente/getDocentesByAsignaturaId/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getDocentesByAsignaturaId(@PathVariable Integer idAsignatura) {
        return ResponseEntity.ok().body(usuarioService.getDocentesByAsignaturaId(idAsignatura));
    }

    @PostMapping("/docente/altaDocente")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> altaDocente(@Valid @RequestBody DtNuevoDocente dtNuevoDocente) throws MessagingException, IOException {
        return usuarioService.nuevoDocente(dtNuevoDocente);
    }

    @PutMapping("/docente/modificarDocente/{idDocente}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> modificarDocente(@PathVariable Integer idDocente, @RequestBody DtDocente dtDocente) {
        return usuarioService.modificarDocente(idDocente, dtDocente);
    }

    @DeleteMapping("/docente/bajaDocente/{idDocente}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> bajaDocente(@PathVariable Integer idDocente) {
        return usuarioService.bajaDocente(idDocente);
    }

}
