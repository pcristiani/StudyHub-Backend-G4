package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.services.EmailService;
import Group4.StudyHubBackendG4.services.UsuarioService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/getAllUsers")
    public List<DtUsuario> getAllUsers() {
        return usuarioService.getAllUsers();
    }

    @GetMapping("/api/users/getUser/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        return usuarioService.getUserById(id);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody DtNuevoUsuario dtNuevoUsuario) throws MessagingException, IOException {
        return usuarioService.register(dtNuevoUsuario);
    }

    @PutMapping("/api/users/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody DtUsuario dtUsuario) {
        return usuarioService.modificarUsuario(id, dtUsuario);
    }

    @DeleteMapping("/api/users/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
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
    public ResponseEntity<?> altaDocente(@Valid @RequestBody DtNuevoDocente dtNuevoDocente) throws MessagingException, IOException {
        return usuarioService.nuevoDocente(dtNuevoDocente);
    }

    @DeleteMapping("/api/docente/bajaDocente/{id}")
    public ResponseEntity<?> bajaDocente(@PathVariable Integer id) {
        return usuarioService.bajaDocente(id);
    }

    @PutMapping("/api/docente/modificarDocente/{id}")
    public ResponseEntity<?> modificarDocente(@PathVariable Integer id, @RequestBody DtDocente dtDocente) {
        return usuarioService.modificarDocente(id, dtDocente);
    }


}
