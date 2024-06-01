package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtNuevoExamen;
import Group4.StudyHubBackendG4.datatypes.DtPeriodoExamenRequest;
import Group4.StudyHubBackendG4.services.CarreraService;
import Group4.StudyHubBackendG4.services.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ExamenController {
    @Autowired
    private ExamenService examenService;

    @PostMapping("/api/examen/registroAsignaturaAPeriodo")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> registroAsignaturaAPeriodo(@RequestBody DtNuevoExamen nuevoExamen) {
        return examenService.registroAsignaturaAPeriodo(nuevoExamen);
    }
}
