package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.*;
import Group4.StudyHubBackendG4.utils.RoleUtil;
import Group4.StudyHubBackendG4.utils.converters.CarreraConverter;
import Group4.StudyHubBackendG4.utils.converters.PeriodoConverter;
import Group4.StudyHubBackendG4.utils.converters.UsuarioConverter;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
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

    @Autowired
    private CarreraCoordinadorRepo carreraCoordinadorRepo;

    @Autowired
    private PeriodoExamenRepo periodoExamenRepo;

    @Autowired
    private PeriodoConverter periodoConverter;

    public ResponseEntity<List<DtCarrera>> getCarreras() {
        return ResponseEntity.ok(carreraRepo.findAll()
                .stream()
                .map(carreraConverter::convertToDto)
                .collect(Collectors.toList()));
    }

    public List<DtCarrera> getCarrerasInscripcionesPendientes() {
        return inscripcionCarreraRepo.findInscripcionesPendientes().stream()
                .map(InscripcionCarrera::getCarrera)
                .distinct()
                .map(carreraConverter::convertToDto)
                .collect(Collectors.toList());
    }
    public List<DtUsuario> getInscriptosPendientes(Integer id) {
        Carrera carrera = carreraRepo.findById(id).orElse(null);
        return inscripcionCarreraRepo.findInscriptosPendientes(carrera).stream()
                .map(InscripcionCarrera::getUsuario)
                .distinct()
                .map(usuarioConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DtCarrera> getCarrerasInscripto(Integer id) {
        Usuario user = usuarioRepo.findById(id).orElse(null);
        return user != null
            ? inscripcionCarreraRepo.findCarrerasInscripto(user).stream()
                .map(InscripcionCarrera::getCarrera)
                .distinct()
                .map(carreraConverter::convertToDto)
                .collect(Collectors.toList())
            : null;
    }

    public List<DtCarrera> getCarrerasConPeriodo() {
        return periodoExamenRepo.findDistinctCarreras()
                .stream()
                .map(carreraConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DtPeriodoExamen> getPeriodosDeCarrera(Integer idCarrera) {
        Carrera carrera = carreraRepo.findById(idCarrera).orElse(null);
        return periodoExamenRepo.findByCarrera(carrera)
                .stream()
                .map(periodoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<String> nuevaCarrera(DtNuevaCarrera dtNuevaCarrera) {

        Optional<Carrera> existingCarrera = Optional.ofNullable(carreraRepo.findByNombre(dtNuevaCarrera.getNombre()));
        if (existingCarrera.isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe una carrera con ese nombre.");
        }

        Carrera carrera = existingCarrera.orElseGet(Carrera::new)
                .CarreraFromDtNuevaCarrera(dtNuevaCarrera);

        carreraRepo.save(carrera);
        ResponseEntity<?> asignarCoordinadorResult = asignarCoordinador(carrera.getIdCarrera(), dtNuevaCarrera.getIdCoordinador());

        if(asignarCoordinadorResult.getStatusCode().equals(HttpStatus.BAD_REQUEST)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.badRequest().body(Objects.requireNonNull(asignarCoordinadorResult.getBody()).toString());
        }

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

        assert user != null;
        assert carrera != null;
        this.notificarValidacionCarreraPorMail(user, carrera.getNombre());

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

    @Transactional
    public ResponseEntity<?> asignarCoordinador(Integer idCarrera, Integer idCoordinador) {
        Optional<Carrera> carreraOpt = carreraRepo.findById(idCarrera);
        Optional<Usuario> coordinadorOpt = usuarioRepo.findById(idCoordinador);

        if (carreraOpt.isPresent() && coordinadorOpt.isPresent()) {
            Carrera carrera = carreraOpt.get();
            Usuario coordinador = coordinadorOpt.get();

            if (carreraCoordinadorRepo.existsByCarreraAndUsuario(carrera, coordinador)) {
                return ResponseEntity.badRequest().body("El coordinador ya está asignado a la carrera.");
            }

            if (!RoleUtil.getRoleName(coordinador.getRol()).equals("Coordinador")) {
                return ResponseEntity.badRequest().body("El usuario no es un coordinador.");
            }

            CarreraCoordinador carreraCoordinador = new CarreraCoordinador();
            carreraCoordinador.setCarrera(carrera);
            carreraCoordinador.setUsuario(coordinador);
            carreraCoordinadorRepo.save(carreraCoordinador);

            return ResponseEntity.ok().body("Coordinador asignado exitosamente.");
        }
        return ResponseEntity.badRequest().body("Id no existe.");
    }

    public boolean quedanCarrerasDesatentidas(Integer coordinadorId) {
        List<Carrera> carreras = carreraCoordinadorRepo.findCarrerasByCoordinadorId(coordinadorId);

        for (Carrera carrera : carreras) {
            long countCoordinadores = carreraCoordinadorRepo.countCoordinadoresByCarreraId(carrera.getIdCarrera());
            if (countCoordinadores <= 1) {
                return false; // Hay una carrera que quedaría sin coordinador
            }
        }
        return true; // Todas las carreras tienen otros coordinadores
    }

    public ResponseEntity<?> altaPeriodoDeExamen(Integer idCarrera, DtPeriodoExamenRequest fechas) {
        try {
            LocalDate fechaInicio = fechas.getInicio().convertToLocalDate();
            LocalDate fechaFin = fechas.getFin().convertToLocalDate();

            Carrera carrera = carreraRepo.findById(idCarrera).orElse(null);
            if(carrera == null) {
                return ResponseEntity.badRequest().body("La carrera no existe!");
            }

            String message = validatePeriodo(carrera, fechaInicio, fechaFin);
            if (!message.isEmpty()) {
                return ResponseEntity.badRequest().body(message);
            }

            // Crear y guardar el nuevo período
            PeriodoExamen nuevoPeriodo = new PeriodoExamen();
            nuevoPeriodo.setCarrera(carrera);
            nuevoPeriodo.setFechaInicio(fechaInicio);
            nuevoPeriodo.setFechaFin(fechaFin);
            nuevoPeriodo.setNombre(fechas.getNombre());

            periodoExamenRepo.save(nuevoPeriodo);

            return ResponseEntity.ok("Período de examen añadido con éxito.");
        } catch (DateTimeException e) {
            return ResponseEntity.badRequest().body("Se ha ingresado una fecha invalida");
        }
    }

    private void notificarValidacionCarreraPorMail(Usuario user, String carrera) throws IOException, MessagingException {
        String htmlContent = emailService.getHtmlContent("htmlContent/notifyAcceptedCarrera.html");
        htmlContent = htmlContent.replace("$carrera", carrera);
        htmlContent = htmlContent.replace("$nombreCompleto", user.getNombre() + ' ' + user.getApellido());
        emailService.sendEmail(user.getEmail(), "StudyHub - Notificacion de validación a carrera", htmlContent);
    }

    private String validatePeriodo(Carrera carrera, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaFin.isBefore(fechaInicio)) {
            return "La fecha de fin no puede ser antes de la fecha de inicio.";
        }
        return periodoExamenRepo.findByCarreraAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
                carrera, fechaFin, fechaInicio).isEmpty() ? "" : "El período ingresado se solapa con un período existente.";
    }
}
