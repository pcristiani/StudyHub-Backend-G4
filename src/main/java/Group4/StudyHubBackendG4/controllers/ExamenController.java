package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.Cursada;
import Group4.StudyHubBackendG4.repositories.CursadaExamenRepo;
import Group4.StudyHubBackendG4.services.CarreraService;
import Group4.StudyHubBackendG4.services.ExamenService;
import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
import Group4.StudyHubBackendG4.utils.enums.ResultadoExamen;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Tag(name = "Examenes", description = "Endpoints para la operativa de Examenes")
public class ExamenController {

    @Autowired
    private ExamenService examenService;

    @GetMapping("/api/examen/getExamenes/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    @Operation(summary = "Obtiene los examenes de un estudiante")
    public ResponseEntity<?> getExamenes(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok().body(examenService.getExamenes(idUsuario));
    }

    @GetMapping("/api/examen/getExamenesAsignatura/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    @Operation(summary = "Obtiene los examenes de una asignatura")
    public ResponseEntity<?> getExamenesAsignatura(@PathVariable Integer idAsignatura) {
        return ResponseEntity.ok().body(examenService.getExamenesAsignatura(idAsignatura));
    }
    @GetMapping("/api/examen/getExamenesPeriodo/{idPeriodo}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    @Operation(summary = "Obtiene los examenes de un periodo")
    public ResponseEntity<?> getExamenesPeriodo(@PathVariable Integer idPeriodo) {
        return ResponseEntity.ok().body(examenService.getExamenesPeriodo(idPeriodo));
    }
    @PostMapping("/api/examen/registroAsignaturaAPeriodo")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    @Operation(summary = "Registra una asignatura a un periodo")
    public ResponseEntity<?> registroAsignaturaAPeriodo(@RequestBody DtNuevoExamen nuevoExamen) {
        return examenService.registroAsignaturaAPeriodo(nuevoExamen);
    }

    @PostMapping("/api/examen/inscripcionExamen")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    @Operation(summary = "Inscripcion a un examen de un estudiante")
    public ResponseEntity<?> inscripcionExamen(@RequestBody DtInscripcionExamen dtInscripcionExamen) {
        return examenService.inscripcionExamen(dtInscripcionExamen);
    }

    @PostMapping("/api/examen/cambiarResultadoExamen/{idCursadaExamen}")
    @Operation(summary = "Cambia el resultado de un examen de un estudiante")
    public ResponseEntity<?> cambiarResultadoExamen(@PathVariable Integer idCursadaExamen, @RequestParam Integer calificacion) throws MessagingException, IOException {
        return examenService.modificarResultadoExamen(idCursadaExamen, calificacion);
    }

    @GetMapping("/api/examen/getCursadasExamen/{idExamen}")
    @Operation(summary = "Obtiene las cursadas de un examen")
    public ResponseEntity<?> getCursadasExamen(@PathVariable Integer idExamen) {
        List<DtCursadaExamen> pendientes = examenService.findCursadasExamenByExamen(idExamen);
        return ResponseEntity.ok(pendientes);
    }

    @GetMapping("/api/examen/getExamenesAsignaturaPorAnio/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    @Operation(summary = "Obtiene los examenes de una asignatura por año")
    public ResponseEntity<?> getExamenesAsignaturaPorAnio(@PathVariable Integer idAsignatura, @RequestParam Integer anio) {
        return ResponseEntity.ok().body(examenService.getExamenesAsignaturaPorAnio(idAsignatura, anio));
    }

    @GetMapping("/api/examen/getActa/{idExamen}")
    @Operation(summary = "Obtiene el acta de un examen")
    public ResponseEntity<?> getActa(@PathVariable Integer idExamen) {
        return examenService.getActa(idExamen);
    }
}
