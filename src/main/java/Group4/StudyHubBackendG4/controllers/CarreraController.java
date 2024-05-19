package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.datatypes.DtInscripcionCarrera;
import Group4.StudyHubBackendG4.datatypes.DtNuevaCarrera;
import Group4.StudyHubBackendG4.services.CarreraService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class CarreraController {

    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping("/api/carrera/getAllCarreras")
    public ResponseEntity<?> getCarreras() {
        return ResponseEntity.ok().body(carreraService.getCarreras());
    }

    @PostMapping("/api/carrera/altaCarrera")
    public ResponseEntity<?> altaCarrera(@Valid @RequestBody DtNuevaCarrera dtNuevaCarrera) throws MessagingException, IOException {
        return carreraService.nuevaCarrera(dtNuevaCarrera);
    }

    //TODO: Sacar éste método ya que habría que hacer mil validaciones y se va del scope actual
    /*
    @DeleteMapping("/api/carrera/bajaCarrera/{id}")
    public ResponseEntity<?> bajaCarrera(@PathVariable Integer id) {
        return carreraService.bajaCarrera(id);
    }

     */

    @PutMapping("/api/carrera/modificarCarrera/{id}")
    public ResponseEntity<?> modificarCarrera(@PathVariable Integer id, @RequestBody DtCarrera dtCarrera) {
        return carreraService.modificarCarrera(id, dtCarrera);
    }

    //inscripcion a carrera
    @PostMapping("/api/carrera/inscripcionCarrera")
    public ResponseEntity<?> inscripcionCarrera(@RequestBody DtInscripcionCarrera dtInscripcionCarrera) {
        return carreraService.inscripcionCarrera(dtInscripcionCarrera);
    }


}
