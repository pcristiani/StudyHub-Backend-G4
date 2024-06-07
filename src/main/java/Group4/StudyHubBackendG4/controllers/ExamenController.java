package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.Cursada;
import Group4.StudyHubBackendG4.repositories.CursadaExamenRepo;
import Group4.StudyHubBackendG4.services.CarreraService;
import Group4.StudyHubBackendG4.services.ExamenService;
import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
import Group4.StudyHubBackendG4.utils.enums.ResultadoExamen;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ExamenController {
    @Autowired
    private ExamenService examenService;

    @GetMapping("/api/examen/getExamenes/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getExamenes(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok().body(examenService.getExamenes(idUsuario));
    }
    @GetMapping("/api/examen/getExamenesAsignatura/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getExamenesAsignatura(@PathVariable Integer idAsignatura) {
        return ResponseEntity.ok().body(examenService.getExamenesAsignatura(idAsignatura));
    }
    @GetMapping("/api/examen/getExamenesPeriodo/{idPeriodo}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getExamenesPeriodo(@PathVariable Integer idPeriodo) {
        return ResponseEntity.ok().body(examenService.getExamenesPeriodo(idPeriodo));
    }
    @PostMapping("/api/examen/registroAsignaturaAPeriodo")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> registroAsignaturaAPeriodo(@RequestBody DtNuevoExamen nuevoExamen) {
        return examenService.registroAsignaturaAPeriodo(nuevoExamen);
    }

    @PostMapping("/api/examen/inscripcionExamen")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> inscripcionExamen(@RequestBody DtInscripcionExamen dtInscripcionExamen) {
        return examenService.inscripcionExamen(dtInscripcionExamen);
    }

    @PostMapping("/api/examen/cambiarResultadoExamen/{idCursadaExamen}")
    public ResponseEntity<?> cambiarResultadoExamen(@PathVariable Integer idCursadaExamen, @RequestParam String nuevoResultadoStr) {
        return ResponseEntity.ok(examenService.modificarResultadoExamen(idCursadaExamen, ResultadoExamen.valueOf(nuevoResultadoStr)));
    }

    @GetMapping("/api/examen/getCursadasExamenPendientes")
    public ResponseEntity<?> getCursadasExamenPendientes(@RequestParam Integer anio, @RequestParam Integer idAsignatura) {
        List<DtCursadaExamen> pendientes = examenService.findCursadasExamenByAnioAndAsignatura(anio, idAsignatura);
        return ResponseEntity.ok(pendientes);
    }

    @GetMapping("/api/examen/getActa/{idExamen}")
    public ResponseEntity<?> getActa(@PathVariable Integer idExamen) {
        return examenService.getActa(idExamen);
    }
}
