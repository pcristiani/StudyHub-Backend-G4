package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.*;
import Group4.StudyHubBackendG4.utils.converters.DocenteConverter;
import Group4.StudyHubBackendG4.utils.converters.UsuarioConverter;
import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
import Group4.StudyHubBackendG4.utils.enums.ResultadoExamen;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamenService {

    @Autowired
    private AsignaturaRepo asignaturaRepo;

    @Autowired
    private PeriodoExamenRepo periodoExamenRepo;

    @Autowired
    private ExamenRepo examenRepo;

    @Autowired
    private DocenteRepo docenteRepo;

    @Autowired
    private DocenteExamenRepo docenteExamenRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private EstudianteCursadaRepo estudianteCursadaRepo;

    @Autowired
    private CursadaExamenRepo cursadaExamenRepo;

    @Autowired
    private DocenteConverter docenteConverter;

    @Autowired
    private UsuarioConverter usuarioConverter;

    @Autowired
    private PushService pushService;

    @Autowired
    private EmailService emailService;


    private DtExamen examenToDtExamen(Examen examen){
        DtExamen dtExamen = new DtExamen();
        dtExamen.setIdExamen(examen.getIdExamen());
        dtExamen.setAsignatura(examen.getAsignatura().getNombre());
        dtExamen.setPeriodoExamen(examen.getPeriodoExamen().getNombre());
        dtExamen.setFechaHora(examen.getFechaHora());
        return dtExamen;
    }
    public List<DtExamen> getExamenes(Integer idUsuario) {
        Usuario user = usuarioRepo.findById(idUsuario).orElse(null);
        List<Examen> examenes = cursadaExamenRepo.findAllExamenesByCedulaEstudiante(user.getCedula());
        List<DtExamen> dtExamenes = new ArrayList<>();
        for (Examen examen : examenes) {
            dtExamenes.add(examenToDtExamen(examen));
        }
        return dtExamenes;
    }
    public List<DtExamen> getExamenesPeriodo(Integer idPeriodo) {
        PeriodoExamen periodoExamen = periodoExamenRepo.findById(idPeriodo).orElse(null);
        List<Examen> examenes = examenRepo.findByPeriodoExamen(periodoExamen);
        List<DtExamen> dtExamenes = new ArrayList<>();
        for (Examen examen : examenes) {
            dtExamenes.add(examenToDtExamen(examen));
        }
        return dtExamenes;
    }

    public List<DtExamen> getExamenesAsignatura(Integer idAsignatura) {
        Asignatura asignatura = asignaturaRepo.findById(idAsignatura).orElse(null);
        List<Examen> examenes = examenRepo.findByAsignatura(asignatura);
        List<DtExamen> dtExamenes = new ArrayList<>();
        for (Examen examen : examenes) {
            dtExamenes.add(examenToDtExamen(examen));
        }
        return dtExamenes;
    }

    public List<DtExamen> getExamenesAsignaturaPorAnio(Integer idAsignatura, Integer anio) {
        Asignatura asignatura = asignaturaRepo.findById(idAsignatura).orElse(null);
        List<Examen> examenes = examenRepo.findByAsignatura(asignatura);
        List<Examen> filteredExamenes = examenes.stream()
                .filter(examen -> examen.getFechaHora().getYear() == anio)
                .toList();
        List<DtExamen> dtExamenes = new ArrayList<>();
        for (Examen examen : filteredExamenes) {
            dtExamenes.add(examenToDtExamen(examen));
        }
        return dtExamenes;
    }
    public ResponseEntity<?> registroAsignaturaAPeriodo(DtNuevoExamen nuevoExamen) {
        //Validar que existe asignatura
        Asignatura asignatura = asignaturaRepo.findById(nuevoExamen.getIdAsignatura()).orElse(null);
        if (asignatura == null) {
            return ResponseEntity.badRequest().body("No existe la asignatura");
        }
        //Validar que existe el periodo
        PeriodoExamen periodoExamen = periodoExamenRepo.findById(nuevoExamen.getIdPeriodo()).orElse(null);
        if (periodoExamen == null) {
            return ResponseEntity.badRequest().body("No existe el periodo");
        }
        //Validar que existan los docentes
        List<Integer> idsDocentes = nuevoExamen.getIdsDocentes();
        List<Docente> docentes = new ArrayList<>();
        for (Integer id : idsDocentes) {
            Docente docente = docenteRepo.findById(id).orElse(null);
            if (docente == null) {
                return ResponseEntity.badRequest().body("No existe el docente con id: " + id);
            }
            docentes.add(docente);
        }
        LocalDateTime fechaHoraExamen = nuevoExamen.getFechaHora();
        LocalDate inicioPeriodo = periodoExamen.getFechaInicio();
        LocalDate finPeriodo = periodoExamen.getFechaFin();
        if (fechaHoraExamen.isBefore(inicioPeriodo.atStartOfDay()) || fechaHoraExamen.isAfter(finPeriodo.atStartOfDay())) {
            return ResponseEntity.badRequest().body("La fecha indicada no esta dentro del periodo de examen.");
        }
        if(examenRepo.existsByAsignaturaAndFechaHora(asignatura,fechaHoraExamen)){
            return ResponseEntity.badRequest().body("Ya existe un examen de la asignatura para la fecha indicada.");
        }
        Examen examen = new Examen();
        examen.setAsignatura(asignatura);
        examen.setPeriodoExamen(periodoExamen);
        examen.setFechaHora(fechaHoraExamen);
        examenRepo.save(examen);

        for(Docente d: docentes){
            DocenteExamen docenteExamen = new DocenteExamen();
            docenteExamen.setIdExamen(examen);
            docenteExamen.setDocente(d);
            docenteExamenRepo.save(docenteExamen);
        }

        return ResponseEntity.ok().body("Se registró la asignatura al periodo de examen.");
    }



    public ResponseEntity<?> inscripcionExamen(DtInscripcionExamen dtInscripcionExamen) {
        // Valido que existe el estudiante
        Usuario estudiante = usuarioRepo.findById(dtInscripcionExamen.getIdEstudiante()).orElse(null);
        if (estudiante == null) {
            return ResponseEntity.badRequest().body("No existe el estudiante.");
        }
        // Validar que existe el examen
        Examen examen = examenRepo.findById(dtInscripcionExamen.getIdExamen()).orElse(null);
        if (examen == null) {
            return ResponseEntity.badRequest().body("No existe el examen.");
        }

        // Obtengo la asignatura del examen
        Asignatura asignatura = examen.getAsignatura();
        // Valido que el estudiante no la haya aprobado y que tenga el resultado examen
        List<Asignatura> asignaturasAprobadas = estudianteCursadaRepo.findAprobadasByEstudiante(estudiante, ResultadoAsignatura.EXONERADO);
        boolean isAsignaturaAprobada = asignaturasAprobadas.contains(asignatura);
        if(isAsignaturaAprobada){
            return ResponseEntity.badRequest().body("Ya aprobaste la asignatura.");
        }

        List<EstudianteCursada> estudianteCursadas = estudianteCursadaRepo.findByEstudianteAndAsignatura(estudiante, asignatura);
        Optional<Cursada> optionalCursada = estudianteCursadas.stream()
                .map(EstudianteCursada::getCursada)
                .filter(cursada -> cursada.getResultado().equals(ResultadoAsignatura.EXAMEN))
                .findFirst();

        if (optionalCursada.isEmpty()) {
            return ResponseEntity.badRequest().body("El estudiante no tiene una cursada con resultado 'EXAMEN'.");
        }

        Cursada cursada = optionalCursada.get();

        // Valido que el estudiante no haya cursado el examen
        CursadaExamen cursadaExamen = cursadaExamenRepo.findByCedulaEstudianteAndExamen(estudiante.getCedula(), examen);
        if(cursadaExamen != null) {
            switch (cursadaExamen.getResultado()) {
                case APROBADO:
                    return ResponseEntity.badRequest().body("Ya aprobaste el examen.");
                case REPROBADO:
                    return ResponseEntity.badRequest().body("Reprobaste este examen.");
                case PENDIENTE:
                    return ResponseEntity.badRequest().body("Ya tienes un examen pendiente.");
            }
        }
        CursadaExamen inscripcionExamen = new CursadaExamen();
        inscripcionExamen.setCedulaEstudiante(estudiante.getCedula());
        inscripcionExamen.setExamen(examen);
        inscripcionExamen.setCursada(cursada);
        inscripcionExamen.setResultado(ResultadoExamen.PENDIENTE);
        cursadaExamenRepo.save(inscripcionExamen);

        return ResponseEntity.ok().body("Se inscribió al examen.");
    }

    public List<DtCursadaExamen> findCursadasExamenByExamen(Integer idExamen) {
        Examen examen = examenRepo.findById(idExamen).orElse(null);
        List<CursadaExamen> cursadaExamen = cursadaExamenRepo.findByExamen(examen);
        List<DtCursadaExamen> dtCursadaExamen = new ArrayList<>();
        for (CursadaExamen ce: cursadaExamen) {
            Usuario user = usuarioRepo.findByCedula(ce.getCedulaEstudiante());
            DtCursadaExamen dtCe = new DtCursadaExamen();
            dtCe.setIdCursadaExamen(ce.getIdCursadaExamen());
            dtCe.setIdCursada(ce.getCursada().getIdCursada());
            dtCe.setIdExamen(ce.getExamen().getIdExamen());
            dtCe.setNombreEstudiante(user.getNombre());
            dtCe.setApellidoEstudiante(user.getApellido());
            dtCe.setMailEstudiante(user.getEmail());
            dtCe.setCedulaEstudiante(ce.getCedulaEstudiante());
            dtCe.setCalificacion(ce.getCalificacion());
            dtCursadaExamen.add(dtCe);
        }
        return dtCursadaExamen;
    }

    public ResponseEntity<?> modificarResultadoExamen(Integer idCursadaExamen, Integer calificacion) throws MessagingException, IOException {
        CursadaExamen cursadaExamen = cursadaExamenRepo.findById(idCursadaExamen)
                .orElse(null) ;

        if (cursadaExamen == null) {
            return ResponseEntity.badRequest().body("No se encontró la cursada.");
        }

        ResultadoExamen nuevoResultado = ResultadoExamen.doyResultadoPorCalificacion(calificacion);

        cursadaExamen.setCalificacion(calificacion);
        cursadaExamen.setResultado(nuevoResultado);
        cursadaExamenRepo.save(cursadaExamen);

        Usuario usuario = cursadaExamenRepo.findEstudianteByCursadaExamenCedula(cursadaExamen);

        notificarResultadoExamenPorMail(usuario, nuevoResultado, cursadaExamen.getCursada().getAsignatura().getNombre(), calificacion);
        pushService.sendPushNotification(usuario.getIdUsuario(), "Se ha registrado un resultado de tus examenes! ", "StudyHub");

        return ResponseEntity.ok().body("Resultado de la cursada con ID " + idCursadaExamen + " cambiado exitosamente a " + nuevoResultado);
    }

    public ResponseEntity<?> getActa(Integer idExamen) {
        Examen examen = examenRepo.findById(idExamen).orElse(null);
        if(examen == null) {
            return ResponseEntity.badRequest().body("No se encontró el examen.");
        }
        DtExamen dtExamen = examenToDtExamen(examen);
        String asignatura = dtExamen.getAsignatura();


        //obtener docentes
        List<Docente> docentes = docenteExamenRepo.findDocentesByExamen(examen);
        List<DtDocente> dtDocentes = new ArrayList<>();
        for(Docente d: docentes){
            DtDocente dtDocente = docenteConverter.convertToDto(d);
            dtDocentes.add(dtDocente);
        }
        List<Usuario> estudiantes = cursadaExamenRepo.findUsuariosByExamen(examen);
        List<DtUsuario> dtEstudiantes = new ArrayList<>();
        for(Usuario u: estudiantes){
            DtUsuario dtUsuario = usuarioConverter.convertToDto(u);
            dtEstudiantes.add(dtUsuario);
        }

        DtActa acta = new DtActa();
        acta.setAsignatura(asignatura);
        acta.setEstudiantes(dtEstudiantes);
        acta.setDocentes(dtDocentes);
        acta.setExamen(dtExamen);

        return ResponseEntity.ok().body(acta);
    }

    private void notificarResultadoExamenPorMail(Usuario user, ResultadoExamen resultadoExamen, String nombreExamen, Integer calificacion) throws IOException, MessagingException {
        String htmlContent = emailService.getHtmlContent("htmlContent/notifyResultadoExamen.html");
        htmlContent = htmlContent.replace("$user", user.getNombre());
        htmlContent = htmlContent.replace("$nombreExamen", nombreExamen);
        htmlContent = htmlContent.replace("$resultadoExamen", resultadoExamen.getNombre());
        htmlContent = htmlContent.replace("$calificacion", calificacion.toString());
        emailService.sendEmail(user.getEmail(), "StudyHub - Notificacion de resultado de cursada de asignatura", htmlContent);
    }
}
