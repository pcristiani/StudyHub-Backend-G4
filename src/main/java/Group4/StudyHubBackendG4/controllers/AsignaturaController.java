package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtAsignatura;
import Group4.StudyHubBackendG4.datatypes.DtNuevaInscripcionAsignatura;
import Group4.StudyHubBackendG4.datatypes.DtNuevaAsignatura;
import Group4.StudyHubBackendG4.datatypes.DtNuevoHorarioAsignatura;
import Group4.StudyHubBackendG4.repositories.HorarioAsignaturaRepo;
import Group4.StudyHubBackendG4.services.AsignaturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;
    @Autowired
    private HorarioAsignaturaRepo horarioAsignaturaRepo;

    @GetMapping("/api/asignatura/getAsignaturas")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturas() {
        return ResponseEntity.ok(asignaturaService.getAsignaturas());
    }

    @GetMapping("/api/asignatura/getAsignaturasDeCarrera/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturasDeCarrera(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok(asignaturaService.getAsignaturasDeCarrera(idCarrera));
    }

    @GetMapping("/api/asignatura/getAsignaturasDeCarreraConExamen/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturasDeCarreraConExamen(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok(asignaturaService.getAsignaturasDeCarreraConExamen(idCarrera));
    }

    @GetMapping("/api/asignatura/getAsignaturasAprobadas/{idEstudiante}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturasAprobadas(@PathVariable Integer idEstudiante) {
        return ResponseEntity.ok(asignaturaService.getAsignaturasAprobadas(idEstudiante));
    }

    @GetMapping("/api/asignatura/getAsignaturasNoAprobadas/{idEstudiante}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getAsignaturasNoAprobadas(@PathVariable Integer idEstudiante) {
        return ResponseEntity.ok(asignaturaService.getAsignaturasNoAprobadas(idEstudiante));
    }

    @PostMapping("/api/asignatura/getHorarios/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getHorarios(@PathVariable Integer idAsignatura) {
        return ResponseEntity.ok(asignaturaService.getHorarios(idAsignatura));
    }

    @PostMapping("/api/asignatura/altaAsignatura")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A')")
    public ResponseEntity<?> altaAsignatura(@Valid @RequestBody DtNuevaAsignatura dtNuevaAsignatura) {
        return asignaturaService.altaAsignatura(dtNuevaAsignatura);
    }


    @PostMapping("/api/asignatura/registroHorarios/{idAsignatura}")
    @PreAuthorize("hasRole('ROLE_F') or hasRole('ROLE_A')")
    public ResponseEntity<?> registroHorarios(@PathVariable Integer idAsignatura, @Valid @RequestBody DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura) {
        //TODO: Impl
        return ResponseEntity.ok(asignaturaService.registroHorarios(idAsignatura, dtNuevoHorarioAsignatura));

    }

    @PostMapping("/api/asignatura/inscripcionAsignatura")
    @PreAuthorize("hasRole('ROLE_E') or hasRole('ROLE_A')")
    public ResponseEntity<?> inscripcionAsignatura(@Valid @RequestBody DtNuevaInscripcionAsignatura inscripcion) {
        String message = asignaturaService.validateInscripcionAsignatura(inscripcion);
        return message == null
                ? asignaturaService.inscripcionAsignatura(inscripcion)
                : ResponseEntity.badRequest().body(message);


    }
}
