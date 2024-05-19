package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtAsignatura;
import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.services.AsignaturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    @GetMapping("/api/asignatura/getAsignaturas")
    public ResponseEntity<?> getAsignaturas() {
        return asignaturaService.getAsignaturas();
    }

    @GetMapping("/api/asignatura/getAsignaturasDeCarrera")
    public ResponseEntity<?> getAsignaturasDeCarrera(Integer idCarrera) {
        return asignaturaService.getAsignaturasDeCarrera(idCarrera);
    }

    @PostMapping("/api/asignatura/altaAsignatura")
    public ResponseEntity<?> altaAsignatura(@Valid @RequestBody DtAsignatura dtAsignatura) {
        return asignaturaService.altaAsignatura(dtAsignatura);
    }

}
