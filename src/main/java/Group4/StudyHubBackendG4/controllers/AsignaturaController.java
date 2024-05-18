package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtAsignatura;
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

    @PostMapping("/api/asignatura/altaAsignatura")
    public ResponseEntity<?> altaAsignatura(@Valid @RequestBody DtAsignatura dtAsignatura) {
        return asignaturaService.altaAsignatura(dtAsignatura);
    }

}
