package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtInscripcionExamen;
import Group4.StudyHubBackendG4.datatypes.DtNuevoExamen;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            Docente docente = docenteRepo.findById(id).orElse(null); // Assuming you have this method
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
        List<EstudianteCursada> estudianteCursadas = estudianteCursadaRepo.findByEstudianteAndAsignatura(estudiante, asignatura);
        if (estudianteCursadas.isEmpty()) {
            return ResponseEntity.badRequest().body("El estudiante no cursa la asignatura.");
        }

        boolean tieneExamenEnCursada = false;
        for (EstudianteCursada ec : estudianteCursadas) {
            if (ec.getCursada().getResultado().equals("APROBADO")) {
                return ResponseEntity.badRequest().body("El estudiante ya aprobó la asignatura.");
            }
            if (ec.getCursada().getResultado().equals("EXAMEN")) {
                tieneExamenEnCursada = true;
            }
        }

        if (!tieneExamenEnCursada) {
            return ResponseEntity.badRequest().body("El estudiante no tiene una cursada con resultado 'EXAMEN'.");
        }

        // Valido que el estudiante no haya aprobado el examen
        List<CursadaExamen> cursadaExamenes = cursadaExamenRepo.findByCedulaEstudianteAndExamen(estudiante.getCedula(), examen);
        for (CursadaExamen ce : cursadaExamenes) {
            if (ce.getResultado().equals("APROBADO")) {
                return ResponseEntity.badRequest().body("El estudiante ya aprobó el examen.");
            }
        }

        // Valido que el estudiante no tenga otro examen en curso
        for (CursadaExamen ce : cursadaExamenes) {
            if (ce.getResultado().equals("EXAMEN")) {
                return ResponseEntity.badRequest().body("El estudiante ya tiene un examen en curso.");
            }
        }

        CursadaExamen cursadaExamen = new CursadaExamen();
        cursadaExamen.setCedulaEstudiante(estudiante.getCedula());
        cursadaExamen.setExamen(examen);
        cursadaExamen.setFechaHora(LocalDateTime.now());
        cursadaExamen.setCursada(estudianteCursadas.get(0).getCursada());
        cursadaExamen.setResultado("PENDIENTE");
        cursadaExamenRepo.save(cursadaExamen);

        return ResponseEntity.ok().body("Se inscribió al examen.");
    }
}
