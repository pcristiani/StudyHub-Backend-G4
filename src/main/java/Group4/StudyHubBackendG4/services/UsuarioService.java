package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtDocente;
import Group4.StudyHubBackendG4.datatypes.DtNuevoDocente;
import Group4.StudyHubBackendG4.datatypes.DtDocente;
import Group4.StudyHubBackendG4.datatypes.DtNuevoUsuario;
import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.PasswordResetToken;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.persistence.UsuarioTR;
import Group4.StudyHubBackendG4.repositories.DocenteRepo;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioTrRepo;
import Group4.StudyHubBackendG4.utils.JwtUtil;
import Group4.StudyHubBackendG4.utils.RoleUtil;
import Group4.StudyHubBackendG4.utils.converters.DocenteConverter;
import Group4.StudyHubBackendG4.utils.converters.UsuarioConverter;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.print.Doc;
import java.io.IOException;
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
    private JwtUtil jwtUtil;

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

    public List<DtUsuario> getAllUsers() {
        return userRepo.findAll().stream()
                .map(usuarioConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/getAllDocentes")
    public List<DtDocente> getAllDocentes() {
        return docenteRepo.findAll().stream()
                .map(docenteConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<DtUsuario> getUserById(Integer id) {
        return userRepo.findById(id)
                .map(usuarioConverter::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    public Usuario getUserByUsername(String username) {
        return userRepo.findByCedula(username);
    }

    public Usuario getUserByJwt(String jwt) {
        UsuarioTR usuarioTr = usuarioTrRepo.findByJwt(jwt);
        return usuarioTr != null ? usuarioTr.getUsuario() : null;
    }

    public Usuario getUserByCedula(String cedula) {
        return userRepo.findByCedula(cedula);
    }

    public ResponseEntity<String> register(DtNuevoUsuario dtNuevoUsuario) throws IOException, MessagingException {      //TODO: CONTROL EN FRONT: SI ES ESTUDIANTE PRECISA INGRESAR PASSWORD

        Optional<Usuario> existingUser = Optional.ofNullable(userRepo.findByCedula(dtNuevoUsuario.getCedula()));
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("La cedula ingresada ya existe en el sistema.");
        }

        if(!this.isEstudiante(dtNuevoUsuario)){
            dtNuevoUsuario.setPassword(passwordService.generateRandomPassword());
        }

        Usuario usuario = existingUser.orElseGet(Usuario::new)
                .UserFromDtNewUser(dtNuevoUsuario);

        userRepo.save(usuario);

        if(!this.isEstudiante(dtNuevoUsuario)){
            this.notificarAltaDeUsuarioPorMail(dtNuevoUsuario);
        }

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
        Optional<Usuario> userOptional = userRepo.findById(id);

        if (userOptional.isPresent()) {
            Usuario aux = userOptional.get();
            aux.setActivo(false);
            userRepo.save(aux);

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

    public ResponseEntity<?> recuperarPasswordEmail(String email) {
        if (userRepo.existsByEmail(email)) {
            Usuario usuario = userRepo.findByEmail(email);
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
            String endpointUrl = "https://frontstudyhub.vercel.app/resetPassword";
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
        List<Usuario> users = userRepo.findAllByValidado(false);
        return users.stream()
                .map(usuarioConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> acceptEstudiante(Integer id, Boolean aceptado) throws IOException, MessagingException {
        Usuario user = userRepo.findById(id).orElse(null);
        if (user != null) {
            user.setValidado(aceptado);
            userRepo.save(user);
            //ENVIAR MAIL AL ESTUDIANTE
            String htmlContent = emailService.getHtmlContent("notifyAcceptedUser.html");
            htmlContent = htmlContent.replace("$nombreCompleto", user.getNombre() + ' ' + user.getApellido());
            emailService.sendEmail(user.getEmail(), "StudyHub - Notificacion de alta de nuevo usuario", htmlContent);
            return ResponseEntity.ok().body("Usuario aceptado con exito");
        } else {
            return ResponseEntity.badRequest().body("Usuario no existe en el sistema.");
        }
    }
}
