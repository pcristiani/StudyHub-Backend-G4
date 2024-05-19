package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.PasswordResetToken;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.persistence.UsuarioTR;
import Group4.StudyHubBackendG4.repositories.DocenteRepo;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioTrRepo;
import Group4.StudyHubBackendG4.utils.RoleUtil;
import Group4.StudyHubBackendG4.utils.converters.DocenteConverter;
import Group4.StudyHubBackendG4.utils.converters.UsuarioConverter;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PasswordResetTokenRepo tokenRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioTrRepo usuarioTrRepo;

    @Autowired
    private UsuarioConverter usuarioConverter;

    @Autowired
    private DocenteConverter docenteConverter;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private DocenteRepo docenteRepo;

    public List<DtUsuario> getUsuarios() {
        return usuarioRepo.findAll().stream()
                .map(usuarioConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/getAllDocentes")
    public List<DtDocente> getDocentes() {
        return docenteRepo.findAll().stream()
                .map(docenteConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<DtUsuario> getUserById(Integer id) {
        return usuarioRepo.findById(id)
                .map(usuarioConverter::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    public Usuario getUserByUsername(String username) {
        return usuarioRepo.findByCedula(username);
    }

    public Usuario getUserByJwt(String jwt) {
        UsuarioTR usuarioTr = usuarioTrRepo.findByJwt(jwt);
        return usuarioTr != null ? usuarioTr.getUsuario() : null;
    }

    public Usuario getUserByCedula(String cedula) {
        return usuarioRepo.findByCedula(cedula);
    }

    public ResponseEntity<String> register(DtNuevoUsuario dtNuevoUsuario) throws IOException, MessagingException {      //TODO: CONTROL EN FRONT: SI ES ESTUDIANTE PRECISA INGRESAR PASSWORD

        Optional<Usuario> existingUsuario = Optional.ofNullable(usuarioRepo.findByCedula(dtNuevoUsuario.getCedula()));
        if (existingUsuario.isPresent()) {
            return ResponseEntity.badRequest().body("La cedula ingresada ya existe en el sistema.");
        }

        if(!this.isEstudiante(dtNuevoUsuario)){
            dtNuevoUsuario.setPassword(passwordService.generateRandomPassword());
        }

        Usuario usuario = existingUsuario.orElseGet(Usuario::new)
                .UserFromDtNewUser(dtNuevoUsuario);

        usuarioRepo.save(usuario);

        if(!this.isEstudiante(dtNuevoUsuario)){
            this.notificarAltaDeUsuarioPorMail(dtNuevoUsuario);
        }

        return ResponseEntity.ok().body("Usuario registrado con éxito.");
    }

    public ResponseEntity<?> modificarUsuario(Integer id, DtUsuario dtUsuario) {
        String message = "No se encontró usuario.";
        Optional<Usuario> userOptional = usuarioRepo.findById(id);

        if (userOptional.isPresent()) {
            Usuario aux = userOptional.get();

            if (Objects.equals(dtUsuario.getCedula(), aux.getCedula()) || (!Objects.equals(dtUsuario.getCedula(), aux.getCedula()) && !usuarioRepo.existsByCedula(dtUsuario.getCedula()))) {
                aux.setNombre(dtUsuario.getNombre() == null || dtUsuario.getNombre().isEmpty() ? aux.getNombre() : dtUsuario.getNombre());
                aux.setApellido(dtUsuario.getApellido() == null || dtUsuario.getApellido().isEmpty() ? aux.getApellido() : dtUsuario.getApellido());
                aux.setEmail(dtUsuario.getEmail() == null || dtUsuario.getEmail().isEmpty() ? aux.getEmail() : dtUsuario.getEmail());
                aux.setFechaNacimiento(dtUsuario.getFechaNacimiento() == null || dtUsuario.getFechaNacimiento().isEmpty() ? aux.getFechaNacimiento() : dtUsuario.getFechaNacimiento());
                aux.setCedula(dtUsuario.getCedula() == null || dtUsuario.getCedula().isEmpty() ? aux.getCedula() : dtUsuario.getCedula());

                usuarioRepo.save(aux);
                return ResponseEntity.ok().body("Usuario actualizado con exitosamente");
            } else {
                message = "Nombre de usuario ya existe.";
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }


    public ResponseEntity<?> bajaUsuario(Integer id) {
        Optional<Usuario> userOptional = usuarioRepo.findById(id);

        if (userOptional.isPresent()) {
            Usuario aux = userOptional.get();
            aux.setActivo(false);
            usuarioRepo.save(aux);

            return ResponseEntity.ok().body("Usuario desactivado exitosamente.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id no existe o usuario ya inactivo.");
    }

    public ResponseEntity<?> recuperarPassword(String token, String newPassword) {
        var passwordToken = tokenRepo.findByToken(token);
        if (passwordToken != null) {
            LocalDateTime expiration = passwordToken.getExpiryDateTime();
            LocalDateTime now = LocalDateTime.now();
            if(expiration.isAfter(now)){
                Usuario usuario = usuarioRepo.getReferenceById(passwordToken.getUsuario().getIdUsuario());
                usuario.setPassword(PasswordService.getInstance().hashPassword(newPassword));
                usuarioRepo.save(usuario);
                return ResponseEntity.ok().body("Contraseña actualizada con exito");
            } else {
                return ResponseEntity.badRequest().body("Token expirado.");
            }
        } else {
            return ResponseEntity.badRequest().body("Token invalido.");
        }
    }

    public ResponseEntity<?> recuperarPasswordEmail(String email) {
        if (usuarioRepo.existsByEmail(email)) {
            Usuario usuario = usuarioRepo.findByEmail(email);
            String nombreCompleto = usuario.getNombre() + ' ' + usuario.getApellido();
            String resetTokenLink = this.generatePasswordResetToken(usuario);

            try {
                String htmlContent = emailService.getHtmlContent("forgotMail.html");
                htmlContent = htmlContent.replace("$username", nombreCompleto);
                htmlContent = htmlContent.replace("$resetTokenLink", resetTokenLink);

                return emailService.sendEmail(email, "StudyHub - Olvido de contraseña", htmlContent);
            } catch (IOException | MessagingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading email template.");
            }
        }
        return ResponseEntity.badRequest().body("Invalid email.");
    }
    public String generatePasswordResetToken(Usuario usuario) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusMinutes(30L);  // 30 minutos de expiracion
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUsuario(usuario);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiration);
        resetToken.setUsuario(usuario);
        PasswordResetToken token = tokenRepo.save(resetToken);
        if (token != null) {
            String endpointUrl = "http://localhost:3000/resetPassword";
            return endpointUrl + "/?token=" + resetToken.getToken();
        }
        return "";
    }

    public void actualizarJwt(Usuario u, String jwt) {
        UsuarioTR usuarioTr = usuarioTrRepo.findByUsuario(u);

        if (usuarioTr == null) {
            usuarioTr = new UsuarioTR();
            usuarioTr.setUsuario(u);
        }

        usuarioTr.setJwt(jwt);
        usuarioTrRepo.save(usuarioTr);
    }

    private Boolean isEstudiante(DtNuevoUsuario dtNuevoUsuario){
        return dtNuevoUsuario.getRol().equals("E");
    }

    private void notificarAltaDeUsuarioPorMail(DtNuevoUsuario dtNuevoUsuario) throws IOException, MessagingException {
        String htmlContent = emailService.getHtmlContent("notifyRegisterByMail.html");
        htmlContent = htmlContent.replace("$rol", RoleUtil.getRoleName(dtNuevoUsuario.getRol()));
        htmlContent = htmlContent.replace("$password", dtNuevoUsuario.getPassword());
        htmlContent = htmlContent.replace("$nombreCompleto", dtNuevoUsuario.getNombre() + ' ' + dtNuevoUsuario.getApellido());
        emailService.sendEmail(dtNuevoUsuario.getEmail(), "StudyHub - Notificacion de alta de nuevo usuario" + dtNuevoUsuario.getRol() , htmlContent);
    }

    public Boolean existeJwt(String jwt) {
        UsuarioTR usuarioTr = usuarioTrRepo.findByJwt(jwt);
        return usuarioTr != null;
    }

    public ResponseEntity<String> nuevoDocente(DtNuevoDocente dtNuevoDocente) {

        Optional<Docente> existingDocente = Optional.ofNullable(docenteRepo.findByCodigoDocente(dtNuevoDocente.getCodigoDocente()));
        if (existingDocente.isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe ese codigo de docente.");
        }

        Docente docente = existingDocente.orElseGet(Docente::new)
                .DocenteFromDtNuevoDocente(dtNuevoDocente);

        docenteRepo.save(docente);

        return ResponseEntity.ok().body("Docente registrado con éxito.");
    }

    public ResponseEntity<?> bajaDocente(Integer id) {
        Optional<Docente> docenteOpt = docenteRepo.findById(id);

        if (docenteOpt.isPresent()) {
            Docente docente = docenteOpt.get();
            docente.setActivo(false);
            docenteRepo.save(docente);

            return ResponseEntity.ok().body("Docente desactivado exitosamente.");
        }
        return ResponseEntity.badRequest().body("Id no existe.");
    }

    public ResponseEntity<?> modificarDocente(Integer id, DtDocente dtDocente) {
        String message = "No se encontró docente.";
        Optional<Docente> docenteOptional = docenteRepo.findById(id);

        if (docenteOptional.isPresent()) {
            Docente aux = docenteOptional.get();

            if (Objects.equals(dtDocente.getCodigoDocente(), aux.getCodigoDocente()) || (!Objects.equals(dtDocente.getCodigoDocente(), aux.getCodigoDocente()) && !docenteRepo.existsByCodigoDocente(dtDocente.getCodigoDocente()))) {
                aux.setNombre(dtDocente.getNombre() == null || dtDocente.getNombre().isEmpty() ? aux.getNombre() : dtDocente.getNombre());
                aux.setCodigoDocente(dtDocente.getCodigoDocente() == null || dtDocente.getCodigoDocente().equals(0) ? aux.getCodigoDocente() : dtDocente.getCodigoDocente());
                aux.setActivo(dtDocente.getActivo() == null ? aux.getActivo() : dtDocente.getActivo());

                docenteRepo.save(aux);
                return ResponseEntity.ok().body("Docente actualizado exitosamente");
            } else {
                message = "Ya existe un docente con ese codigo.";
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    public List<DtUsuario> getEstudiantesPendientes() {
        List<Usuario> users = usuarioRepo.findAllByValidado(false);
        return users.stream()
                .map(usuarioConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> acceptEstudiante(Integer id, Boolean aceptado) throws IOException, MessagingException {
        Usuario user = usuarioRepo.findById(id).orElse(null);
        if (user != null) {
            user.setValidado(aceptado);
            usuarioRepo.save(user);
            //ENVIAR MAIL AL ESTUDIANTE
            String htmlContent = emailService.getHtmlContent("notifyAcceptedUser.html");
            htmlContent = htmlContent.replace("$nombreCompleto", user.getNombre() + ' ' + user.getApellido());
            emailService.sendEmail(user.getEmail(), "StudyHub - Notificacion de alta de nuevo usuario", htmlContent);
            return ResponseEntity.ok().body("Usuario aceptado con exito");
        } else {
            return ResponseEntity.badRequest().body("Usuario no existe en el sistema.");
        }
    }

    public ResponseEntity<?> modificarPerfil(Integer id, DtPerfil dtPerfil) {
        String message = "No se encontró usuario.";
        Optional<Usuario> userOptional = usuarioRepo.findById(id);

        if (userOptional.isPresent()) {
            Usuario aux = userOptional.get();

            if (Objects.equals(dtPerfil.getEmail(), aux.getEmail()) || (!Objects.equals(dtPerfil.getEmail(), aux.getEmail()) && !usuarioRepo.existsByEmail(dtPerfil.getEmail()))) {
                aux.setNombre(dtPerfil.getNombre() == null || dtPerfil.getNombre().isEmpty() ? aux.getNombre() : dtPerfil.getNombre());
                aux.setApellido(dtPerfil.getApellido() == null || dtPerfil.getApellido().isEmpty() ? aux.getApellido() : dtPerfil.getApellido());
                aux.setEmail(dtPerfil.getEmail() == null || dtPerfil.getEmail().isEmpty() ? aux.getEmail() : dtPerfil.getEmail());
                aux.setFechaNacimiento(dtPerfil.getFechaNacimiento() == null || dtPerfil.getFechaNacimiento().isEmpty() ? aux.getFechaNacimiento() : dtPerfil.getFechaNacimiento());

                usuarioRepo.save(aux);
                return ResponseEntity.ok().body("Perfil modificado exitosamente.");
            } else {
                message = "Ese email ya esta en uso.";
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
