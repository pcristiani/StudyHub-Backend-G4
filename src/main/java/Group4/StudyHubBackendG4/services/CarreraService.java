package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.datatypes.DtInscripcionCarrera;
import Group4.StudyHubBackendG4.datatypes.DtNuevaCarrera;
import Group4.StudyHubBackendG4.datatypes.DtNuevoUsuario;
import Group4.StudyHubBackendG4.persistence.Carrera;
import Group4.StudyHubBackendG4.persistence.InscripcionCarrera;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.CarreraRepo;
import Group4.StudyHubBackendG4.repositories.InscripcionCarreraRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
import Group4.StudyHubBackendG4.utils.RoleUtil;
import Group4.StudyHubBackendG4.utils.converters.CarreraConverter;
import Group4.StudyHubBackendG4.utils.converters.UsuarioConverter;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarreraService {

    @Autowired
    private CarreraRepo carreraRepo;

    @Autowired
    private CarreraConverter carreraConverter;
    @Autowired
    private UsuarioConverter usuarioConverter;
    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private InscripcionCarreraRepo inscripcionCarreraRepo;
    @Autowired
    private EmailService emailService;


    public List<DtCarrera> getCarreras() {
        return carreraRepo.findAll().stream()
                .map(carreraConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DtCarrera> getCarrerasInscripcionesPendientes() {
        return inscripcionCarreraRepo.findInscripcionesPendientes().stream()
                .map(InscripcionCarrera::getCarrera)
                .distinct()
                .map(carreraConverter::convertToDto)
                .collect(Collectors.toList());
    }
    public Object getInscriptosPendientes(Integer id) {
        Carrera carrera = carreraRepo.findById(id).orElse(null);
        return inscripcionCarreraRepo.findInscriptosPendientes(carrera).stream()
                .map(InscripcionCarrera::getUsuario)
                .distinct()
                .map(usuarioConverter::convertToDto)
                .collect(Collectors.toList());
    }


    public ResponseEntity<String> nuevaCarrera(DtNuevaCarrera dtNuevaCarrera) {

        Optional<Carrera> existingCarrera = Optional.ofNullable(carreraRepo.findByNombre(dtNuevaCarrera.getNombre()));
        if (existingCarrera.isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe una carrera con ese nombre.");
        }

        Carrera carrera = existingCarrera.orElseGet(Carrera::new)
                .CarreraFromDtNuevaCarrera(dtNuevaCarrera);

        carreraRepo.save(carrera);

        return ResponseEntity.ok().body("Carrera registrada con éxito.");
    }

    public ResponseEntity<?> bajaCarrera(Integer id) {
        Optional<Carrera> carreraOpt = carreraRepo.findById(id);

        if (carreraOpt.isPresent()) {
            Carrera carrera = carreraOpt.get();
            carrera.setActiva(false);
            carreraRepo.save(carrera);

            return ResponseEntity.ok().body("Carrera desactivada exitosamente.");
        }
        return ResponseEntity.badRequest().body("Id no existe.");
    }

    public ResponseEntity<?> modificarCarrera(Integer id, DtCarrera dtCarrera) {
        String message = "No se encontró carrera.";
        Optional<Carrera> carreraOptional = carreraRepo.findById(id);

        if (carreraOptional.isPresent()) {
            Carrera aux = carreraOptional.get();

            if (Objects.equals(dtCarrera.getNombre(), aux.getNombre()) || (!Objects.equals(dtCarrera.getNombre(), aux.getNombre()) && !carreraRepo.existsByNombre(dtCarrera.getNombre()))) {
                aux.setNombre(dtCarrera.getNombre() == null || dtCarrera.getNombre().isEmpty() ? aux.getNombre() : dtCarrera.getNombre());
                aux.setDescripcion(dtCarrera.getDescripcion() == null || dtCarrera.getDescripcion().isEmpty() ? aux.getDescripcion() : dtCarrera.getDescripcion());
                aux.setActiva(dtCarrera.getActiva() == null ? aux.getActiva() : dtCarrera.getActiva());

                carreraRepo.save(aux);
                return ResponseEntity.ok().body("Carrera actualizada exitosamente");
            } else {
                message = "Ya existe una carrera con ese nombre.";
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    public ResponseEntity<?> inscripcionCarrera(DtInscripcionCarrera dtInscripcionCarrera) {
        String message = this.validateInscripcion(dtInscripcionCarrera);
        if(message != null){
            return ResponseEntity.badRequest().body(message);
        }

        Usuario user = usuarioRepo.findById(dtInscripcionCarrera.getIdEstudiante()).orElse(null);
        Carrera carrera = carreraRepo.findById(dtInscripcionCarrera.getIdCarrera()).orElse(null);

        // Verificar si el estudiante ya tiene una inscripción activa y no validada a esta carrera
        InscripcionCarrera existingInscripcion = inscripcionCarreraRepo
                .findByUsuarioAndCarreraAndActiva(user, carrera, true).orElse(null);

        if (existingInscripcion != null) {
            return ResponseEntity.badRequest().body("El estudiante ya tiene una inscripción activa.");
        }

        InscripcionCarrera inscripcionCarrera = new InscripcionCarrera();
        inscripcionCarrera.setCarrera(carrera);
        inscripcionCarrera.setUsuario(user);
        inscripcionCarrera.setActiva(true);
        inscripcionCarrera.setValidada(false);
        inscripcionCarreraRepo.save(inscripcionCarrera);

        return ResponseEntity.ok().body("Inscripcion solicitada exitosamente.");
    }

    public ResponseEntity<?> acceptEstudianteCarrera(DtInscripcionCarrera dtInscripcionCarrera) throws MessagingException, IOException {
        String message = this.validateInscripcion(dtInscripcionCarrera);
        if(message != null){
            return ResponseEntity.badRequest().body(message);
        }

        Usuario user = usuarioRepo.findById(dtInscripcionCarrera.getIdEstudiante()).orElse(null);
        Carrera carrera = carreraRepo.findById(dtInscripcionCarrera.getIdCarrera()).orElse(null);

        // Verificar si el estudiante ya tiene una inscripción activa
        InscripcionCarrera inscripcionCarrera = inscripcionCarreraRepo.findByUsuarioAndCarreraAndActivaAndValidada(user, carrera, true, false).orElse(null);

        if (inscripcionCarrera == null) {
            return ResponseEntity.badRequest().body("El estudiante no tiene una inscripción pendiente de validación.");
        }

        inscripcionCarrera.setValidada(true);
        inscripcionCarreraRepo.save(inscripcionCarrera);

        this.notificarValidacionCarrera(user, carrera.getNombre());

        return ResponseEntity.ok().body("Se aceptó la inscripcion del estudiante a la carrera.");
    }

    public String validateInscripcion(DtInscripcionCarrera dtInscripcionCarrera) {
        Usuario user = usuarioRepo.findById(dtInscripcionCarrera.getIdEstudiante()).orElse(null);
        Carrera carrera = carreraRepo.findById(dtInscripcionCarrera.getIdCarrera()).orElse(null);

        if (user == null) {
            return "El usuario no fue encontrado.";
        }
        if (!user.getRol().equals("E")) {
            return "El usuario no es un estudiante.";
        }
        if (carrera == null) {
            return "La carrera no fue encontrada.";
        }
        return null;
    }



    private void notificarValidacionCarrera(Usuario user, String carrera) throws IOException, MessagingException {
        String htmlContent = emailService.getHtmlContent("notifyAcceptedCarrera.html");
        htmlContent = htmlContent.replace("$carrera", carrera);
        htmlContent = htmlContent.replace("$nombreCompleto", user.getNombre() + ' ' + user.getApellido());
        emailService.sendEmail(user.getEmail(), "StudyHub - Notificacion de validación a carrera", htmlContent);
    }
}
