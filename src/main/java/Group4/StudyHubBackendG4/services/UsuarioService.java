package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtNuevoUsuario;
import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordResetTokenRepo tokenRepo;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/getAllUsers")
    public List<DtUsuario> getAllUsers() {
        return userRepo.findAll().stream()
                .map(Usuario::userToDtUser)
                .collect(Collectors.toList());
    }

    public ResponseEntity<DtUsuario> getUserById(Integer id) {
        return userRepo.findById(id)
                .map(Usuario::userToDtUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    public Usuario getUserByUsername(String username) {
        return userRepo.findByCedula(username);
    }

    public ResponseEntity<String> createUser(DtNuevoUsuario dtNuevoUsuario) {

        Optional<Usuario> existingUser = Optional.ofNullable(userRepo.findByCedula(dtNuevoUsuario.getCedula()));
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe, intente con otro.");
        }

        Usuario usuario = existingUser.orElseGet(Usuario::new)
                .UserFromDtNewUser(dtNuevoUsuario);

        userRepo.save(usuario);

        return ResponseEntity.ok().body("Usuario registrado con éxito.");
    }

    public ResponseEntity<?> modificarUsuario(Integer id, DtUsuario dtUsuario) {
        String message = "No se encontró usuario.";
        Optional<Usuario> userOptional = userRepo.findById(id);

        if (userOptional.isPresent()) {
            Usuario aux = userOptional.get();

            if (Objects.equals(dtUsuario.getCedula(), aux.getCedula()) || (!Objects.equals(dtUsuario.getCedula(), aux.getCedula()) && !userRepo.existsByCedula(dtUsuario.getCedula()))) {
                aux.setNombre(dtUsuario.getNombre() == null || dtUsuario.getNombre().isEmpty() ? aux.getNombre() : dtUsuario.getNombre());
                aux.setApellido(dtUsuario.getApellido() == null || dtUsuario.getApellido().isEmpty() ? aux.getApellido() : dtUsuario.getApellido());
                aux.setEmail(dtUsuario.getEmail() == null || dtUsuario.getEmail().isEmpty() ? aux.getEmail() : dtUsuario.getEmail());
                aux.setFechaNacimiento(dtUsuario.getFechaNacimiento() == null || dtUsuario.getFechaNacimiento().isEmpty() ? aux.getFechaNacimiento() : dtUsuario.getFechaNacimiento());
                aux.setCedula(dtUsuario.getCedula() == null || dtUsuario.getCedula().isEmpty() ? aux.getCedula() : dtUsuario.getCedula());

                userRepo.save(aux);
                return ResponseEntity.ok().body("Usuario actualizado con exitosamente");
            } else {
                message = "Nombre de usuario ya existe.";
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }


    public ResponseEntity<?> bajaUsuario(Integer id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);

            return ResponseEntity.ok().body("Usuario eliminado exitosamente.");
        }
        return ResponseEntity.badRequest().body("Id no existe.");

    }

    public ResponseEntity<?> recuperarPassword(String token, String newPassword) {
        var passwordToken = tokenRepo.findByToken(token);
        if (passwordToken != null) {
            LocalDateTime expiration = passwordToken.getExpiryDateTime();
            LocalDateTime now = LocalDateTime.now();
            if(expiration.isAfter(now)){
                Usuario usuario = userRepo.getReferenceById(passwordToken.getUsuario().getIdUsuario());
                usuario.setPassword(PasswordService.getInstance().hashPassword(newPassword));
                userRepo.save(usuario);
                return ResponseEntity.ok().body("Contraseña actualizada con exito");
            } else {
                return ResponseEntity.badRequest().body("Token expirado.");
            }
        } else {
            return ResponseEntity.badRequest().body("Token invalido.");
        }
    }

}
