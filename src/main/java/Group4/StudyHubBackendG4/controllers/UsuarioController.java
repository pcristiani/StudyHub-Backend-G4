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
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/getUsuarios")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F')")
    public List<DtUsuario> getUsuarios() {
        return usuarioService.getUsuarios();
    }

    @GetMapping("/getDocentes")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getDocentes() {
        return ResponseEntity.ok().body(usuarioService.getDocentes());
    }

    @GetMapping("/getUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getUsuariosById(@PathVariable Integer idUsuario) {
        return usuarioService.getUsuarioById(idUsuario);
    }

    @GetMapping("/getDocentesByAsignaturaId/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getDocentesByAsignaturaId(@PathVariable Integer idAsignatura) {
        return ResponseEntity.ok().body(usuarioService.getDocentesByAsignaturaId(idAsignatura));
    }

    @GetMapping("/getEstudiantesPendientes")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getEstudiantesPendientes() {
        List<DtUsuario> dtUsuarios = usuarioService.getEstudiantesPendientes();
        return ResponseEntity.ok().body(dtUsuarios);
    }

    @PutMapping("/acceptEstudiante/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> acceptEstudiante(@PathVariable Integer idUsuario, @RequestBody Boolean aceptado) throws MessagingException, IOException {
        return usuarioService.acceptEstudiante(idUsuario,aceptado);
    }

    @PostMapping("/registerUsuario")
    public ResponseEntity<?> createUsuario(@Valid @RequestBody DtNuevoUsuario dtNuevoUsuario) throws MessagingException, IOException {
        return usuarioService.register(dtNuevoUsuario);
    }

    @PutMapping("/modificarUsuario/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseEntity<?> modificarUsuario(@PathVariable Integer idUsuario, @RequestBody DtUsuario dtUsuario) {
        return usuarioService.modificarUsuario(idUsuario, dtUsuario);
    }

    @DeleteMapping("/bajaUsuario/{idUsuario}")
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

    @PutMapping("/modificarPerfil/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> modificarPerfil(@PathVariable Integer idUsuario, @RequestBody DtPerfil dtPerfil) {
        return usuarioService.modificarPerfil(idUsuario, dtPerfil);
    }

    @PutMapping("/modificarPassword/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> modificarPassword(@PathVariable Integer idUsuario, @RequestBody String newPassword) {
        return usuarioService.modificarPassword(idUsuario, newPassword);
    }
}
