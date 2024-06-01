package Group4.StudyHubBackendG4.services;

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
}
