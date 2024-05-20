package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.datatypes.DtFecha;
import Group4.StudyHubBackendG4.datatypes.DtNuevaCarrera;
import Group4.StudyHubBackendG4.services.CarreraService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping("/api/carrera/getCarreras")
    public ResponseEntity<?> getCarreras() {
        return ResponseEntity.ok().body(carreraService.getCarreras());
    }

    @PostMapping("/api/carrera/altaCarrera")
    public ResponseEntity<?> altaCarrera(@Valid @RequestBody DtNuevaCarrera dtNuevaCarrera) throws MessagingException, IOException {
        return carreraService.nuevaCarrera(dtNuevaCarrera);
    }

    @PostMapping("/api/carrera/altaPeriodoDeExamen")
    public ResponseEntity<?> altaPeriodoDeExamen(@Valid @RequestBody Integer idCarrera, DtFecha inicio, DtFecha fin) {
        return carreraService.altaPeriodoDeExamen(idCarrera, inicio, fin);
    }



    @PutMapping("/api/carrera/modificarCarrera/{id}")
    public ResponseEntity<?> modificarCarrera(@PathVariable Integer id, @RequestBody DtCarrera dtCarrera) {
        return carreraService.modificarCarrera(id, dtCarrera);
    }

}
