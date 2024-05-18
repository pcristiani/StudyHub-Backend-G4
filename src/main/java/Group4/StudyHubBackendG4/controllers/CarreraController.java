package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.datatypes.DtNuevaCarrera;
import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.services.CarreraService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class CarreraController {

    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @PostMapping("/api/carrera/altaCarrera")
    public ResponseEntity<?> altaCarrera(@Valid @RequestBody DtNuevaCarrera dtNuevaCarrera) throws MessagingException, IOException {
        return carreraService.nuevaCarrera(dtNuevaCarrera);
    }

    @DeleteMapping("/api/carrera/bajaCarrera/{id}")
    public ResponseEntity<?> bajaCarrera(@PathVariable Integer id) {
        return carreraService.bajaCarrera(id);
    }

    @PutMapping("/api/carrera/modificarCarrera/{id}")
    public ResponseEntity<?> modificarCarrera(@PathVariable Integer id, @RequestBody DtCarrera dtCarrera) {
        return carreraService.modificarCarrera(id, dtCarrera);
    }
}
