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

    @GetMapping("/api/usuario/getUsuario/{id}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getUsuariosById(@PathVariable Integer id) {
        return usuarioService.getUserById(id);
    }

    @GetMapping("/api/usuario/getEstudiantesPendientes")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getEstudiantesPendientes() {
        List<DtUsuario> dtUsuarios = usuarioService.getEstudiantesPendientes();
        return ResponseEntity.ok().body(dtUsuarios);
    }

    @PutMapping("/api/usuario/acceptEstudiante/{id}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> acceptEstudiante(@PathVariable Integer id, @RequestBody Boolean aceptado) throws MessagingException, IOException {
        return usuarioService.acceptEstudiante(id,aceptado);
    }

    @PostMapping("/registerUsuario")
    public ResponseEntity<?> createUsuario(@Valid @RequestBody DtNuevoUsuario dtNuevoUsuario) throws MessagingException, IOException {
        return usuarioService.register(dtNuevoUsuario);
    }

    @PutMapping("/api/usuario/modificarUsuario/{id}")
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseEntity<?> modificarUsuario(@PathVariable Integer id, @RequestBody DtUsuario dtUsuario) {
        return usuarioService.modificarUsuario(id, dtUsuario);
    }

    @DeleteMapping("/api/usuario/bajaUsuario/{id}")
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseEntity<?> bajaUsuario(@PathVariable Integer id) {
        return usuarioService.bajaUsuario(id);
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

    @DeleteMapping("/api/docente/bajaDocente/{id}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> bajaDocente(@PathVariable Integer id) {
        return usuarioService.bajaDocente(id);
    }

    @PutMapping("/api/docente/modificarDocente/{id}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> modificarDocente(@PathVariable Integer id, @RequestBody DtDocente dtDocente) {
        return usuarioService.modificarDocente(id, dtDocente);
    }

    @PutMapping("/api/usuario/modificarPerfil/{id}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> modificarPerfil(@PathVariable Integer id, @RequestBody DtPerfil dtPerfil) {
        return usuarioService.modificarPerfil(id, dtPerfil);
    }

    @PutMapping("/api/usuario/modificarPassword/{id}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> modificarPassword(@PathVariable Integer id, @RequestBody String newPassword) {
        return usuarioService.modificarPassword(id, newPassword);
    }
}
