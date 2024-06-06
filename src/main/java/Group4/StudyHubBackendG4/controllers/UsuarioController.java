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
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/api/usuario/getUsuarios")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F')")
    public List<DtUsuario> getUsuarios() {
        return usuarioService.getUsuarios();
    }

    @GetMapping("/api/usuario/getDocentes")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getDocentes() {
        return ResponseEntity.ok().body(usuarioService.getDocentes());
    }

    @GetMapping("/api/usuario/getUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getUsuariosById(@PathVariable Integer idUsuario) {
        return usuarioService.getUsuarioById(idUsuario);
    }

    @GetMapping("/api/usuario/getDocentesByAsignaturaId/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getDocentesByAsignaturaId(@PathVariable Integer idAsignatura) {
        return ResponseEntity.ok().body(usuarioService.getDocentesByAsignaturaId(idAsignatura));
    }

    @GetMapping("/api/usuario/getEstudiantesPendientes")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getEstudiantesPendientes() {
        List<DtUsuario> dtUsuarios = usuarioService.getEstudiantesPendientes();
        return ResponseEntity.ok().body(dtUsuarios);
    }

    @PutMapping("/api/usuario/acceptEstudiante/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> acceptEstudiante(@PathVariable Integer idUsuario, @RequestBody Boolean aceptado) throws MessagingException, IOException {
        return usuarioService.acceptEstudiante(idUsuario,aceptado);
    }

    @PostMapping("/registerUsuario")
    public ResponseEntity<?> createUsuario(@Valid @RequestBody DtNuevoUsuario dtNuevoUsuario) throws MessagingException, IOException {
        return usuarioService.register(dtNuevoUsuario);
    }

    @PutMapping("/api/usuario/modificarUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseEntity<?> modificarUsuario(@PathVariable Integer idUsuario, @RequestBody DtUsuario dtUsuario) {
        return usuarioService.modificarUsuario(idUsuario, dtUsuario);
    }

    @DeleteMapping("/api/usuario/bajaUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseEntity<?> bajaUsuario(@PathVariable Integer idUsuario) {
        return usuarioService.bajaUsuario(idUsuario);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) throws MessagingException, IOException, MessagingException, IOException {
        return usuarioService.recuperarPasswordEmail(email);
    }

    @PostMapping("/recuperarPassword")
    public ResponseEntity<?> recuperarPassword(@RequestBody DtNewPassword dtNewPassword){
        return usuarioService.recuperarPassword(dtNewPassword.getToken(), dtNewPassword.getNewPassword());
    }

    @PostMapping("/api/docente/altaDocente")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> altaDocente(@Valid @RequestBody DtNuevoDocente dtNuevoDocente) throws MessagingException, IOException {
        return usuarioService.nuevoDocente(dtNuevoDocente);
    }

    @DeleteMapping("/api/docente/bajaDocente/{idDocente}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> bajaDocente(@PathVariable Integer idDocente) {
        return usuarioService.bajaDocente(idDocente);
    }

    @PutMapping("/api/docente/modificarDocente/{idDocente}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> modificarDocente(@PathVariable Integer idDocente, @RequestBody DtDocente dtDocente) {
        return usuarioService.modificarDocente(idDocente, dtDocente);
    }

    @PutMapping("/api/usuario/modificarPerfil/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> modificarPerfil(@PathVariable Integer idUsuario, @RequestBody DtPerfil dtPerfil) {
        return usuarioService.modificarPerfil(idUsuario, dtPerfil);
    }

    @PutMapping("/api/usuario/modificarPassword/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> modificarPassword(@PathVariable Integer idUsuario, @RequestBody String newPassword) {
        return usuarioService.modificarPassword(idUsuario, newPassword);
    }

    @PostMapping("/registerMobileToken/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> registerMobileToken(@PathVariable Integer idUsuario, @Valid @RequestBody String mobileToken) {
        return usuarioService.registerMobileToken(idUsuario, mobileToken);
    }
}
