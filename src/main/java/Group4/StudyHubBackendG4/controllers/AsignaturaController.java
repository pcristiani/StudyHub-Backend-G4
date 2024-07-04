package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.services.AsignaturaService;
import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Tag(name = "Asignatura", description = "Endpoints para la operativa de Asignatura")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    @Operation(summary = "Lista todas las asignaturas")
    @GetMapping("/api/asignatura/getAsignaturas")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturas() {
        return ResponseEntity.ok(asignaturaService.getAsignaturas());
    }

    @Operation(summary = "Obtiene una asignatura por su id")
    @GetMapping("/api/asignatura/getAsignaturaById/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturaById(@PathVariable Integer idAsignatura) {
        return ResponseEntity.ok(asignaturaService.getAsignaturaById(idAsignatura));
    }

    @Operation(summary = "Obtiene las asignaturas de una carrera")
    @GetMapping("/api/asignatura/getAsignaturasDeCarrera/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturasDeCarrera(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok(asignaturaService.getAsignaturasDeCarrera(idCarrera));
    }

    @Operation(summary = "Obtiene las asignaturas de un estudiante")
    @GetMapping("/api/asignatura/getAsignaturasDeEstudiante/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturasDeEstudiante(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(asignaturaService.getAsignaturasDeEstudiante(idUsuario));
    }

    @Operation(summary = "Obtiene las asignaturas de una carrera con examen")
    @GetMapping("/api/asignatura/getAsignaturasDeCarreraConExamen/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturasDeCarreraConExamen(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok(asignaturaService.getAsignaturasDeCarreraConExamen(idCarrera));
    }

    @Operation(summary = "Obtiene las asignaturas aprobadas de un estudiante")
    @GetMapping("/api/asignatura/getAsignaturasAprobadas/{idEstudiante}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturasAprobadas(@PathVariable Integer idEstudiante) {
        return ResponseEntity.ok(asignaturaService.getAsignaturasAprobadas(idEstudiante));
    }

    @Operation(summary = "Obtiene las asignaturas no aprobadas de un estudiante")
    @GetMapping("/api/asignatura/getAsignaturasNoAprobadas/{idEstudiante}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturasNoAprobadas(@PathVariable Integer idEstudiante) {
        return ResponseEntity.ok(asignaturaService.getAsignaturasNoAprobadas(idEstudiante));
    }

    @Operation(summary = "Obtiene las asignaturas con examen pendiente de un estudiante")
    @GetMapping("/api/asignatura/getAsignaturasConExamenPendiente/{idEstudiante}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturasConExamenPendiente(@PathVariable Integer idEstudiante, @RequestParam Integer idCarrera) {
        return ResponseEntity.ok(asignaturaService.getAsignaturasConExamenPendiente(idEstudiante,idCarrera));
    }

    @Operation(summary = "Obtiene los horarios de una asignatura")
    @GetMapping("/api/asignatura/getHorarios/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getHorarios(@PathVariable Integer idAsignatura) {
        return ResponseEntity.ok(asignaturaService.getHorarios(idAsignatura));
    }

    @Operation(summary = "Da de alta una asignatura en el sistema")
    @PostMapping("/api/asignatura/altaAsignatura")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A')")
    public ResponseEntity<?> altaAsignatura(@Valid @RequestBody DtNuevaAsignatura dtNuevaAsignatura) {
        return asignaturaService.altaAsignatura(dtNuevaAsignatura);
    }

    @Operation(summary = "Registra horarios a una asignatura")
    @PostMapping("/api/asignatura/registroHorarios/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_F') or hasRole('ROLE_A')")
    public ResponseEntity<?> registroHorarios(@PathVariable Integer idAsignatura, @Valid @RequestBody DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura) {
        return asignaturaService.registroHorarios(idAsignatura, dtNuevoHorarioAsignatura);
    }

    @Operation(summary = "Inscribe un estudiante a una asignatura")
    @PostMapping("/api/asignatura/inscripcionAsignatura")
    @PreAuthorize("hasRole('ROLE_E') or hasRole('ROLE_A')")
    public ResponseEntity<?> inscripcionAsignatura(@Valid @RequestBody DtNuevaInscripcionAsignatura inscripcion) {
        String message = asignaturaService.validateInscripcionAsignatura(inscripcion);
        return message == null
                ? asignaturaService.inscripcionAsignatura(inscripcion)
                : ResponseEntity.badRequest().body(message);
    }

    @Operation(summary = "Registra previaturas a una asignatura")
    @PostMapping("/api/asignatura/registrarPreviaturas/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A')")
    public ResponseEntity<?> registrarPreviaturas(@PathVariable Integer idAsignatura, @RequestBody List<Integer> previaturas) {
        return asignaturaService.registrarPreviaturas(idAsignatura, previaturas);
    }

    @Operation(summary = "Obtiene las cursadas pendientes de una asignatura en un año determinado")
    @GetMapping("/api/asignatura/cursadasPendientes")
    public ResponseEntity<?> getCursadasPendientes(@RequestParam Integer anio, @RequestParam Integer idAsignatura) {
        List<DtCursadaPendiente> pendientes = asignaturaService.getCursadasPendientesByAnioAndAsignatura(anio, idAsignatura);
        return ResponseEntity.ok(pendientes);
    }

    @Operation(summary = "Cambia los resultados de una cursada")
    @PostMapping("/api/asignatura/cambiarResultadoCursada/{idCursada}")
    public ResponseEntity<?> cambiarResultadoCursada(@PathVariable Integer idCursada, @RequestParam Integer calificacion) throws MessagingException, IOException {
        return asignaturaService.modificarResultadoCursada(idCursada, calificacion);
    }

    @Operation(summary = "Obtiene las previas de una asignatura")
    @GetMapping("/api/asignatura/getPreviasAsignatura/{idAsignatura}")
    public ResponseEntity<?> getPrevias(@PathVariable Integer idAsignatura) {
        return asignaturaService.getPreviasAsignatura(idAsignatura);
    }

    @Operation(summary = "Obtiene las asignaturas que no son previas de una asignatura")
    @GetMapping("/api/asignatura/getNoPreviasAsignatura/{idAsignatura}")
    public ResponseEntity<?> getNoPreviasAsignatura(@PathVariable Integer idAsignatura) {
        return asignaturaService.getNoPreviasAsignatura(idAsignatura);
    }

    @Operation(summary = "Obtiene el acta de una asignatura en un horario determinado")
    @GetMapping("/api/asignatura/getActa/{idHorario}")
    public ResponseEntity<?> getActa(@PathVariable Integer idHorario) {
        return asignaturaService.getActa(idHorario);
    }
}
