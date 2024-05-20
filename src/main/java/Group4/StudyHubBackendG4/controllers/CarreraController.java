package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.datatypes.DtFecha;
import Group4.StudyHubBackendG4.datatypes.DtInscripcionCarrera;
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

    @GetMapping("/api/carrera/getCarrerasInscripcionesPendientes")
    public ResponseEntity<?> getCarrerasInscripcionesPendientes() {
        return ResponseEntity.ok().body(carreraService.getCarrerasInscripcionesPendientes());
    }

    @GetMapping("/api/carrera/getInscriptosPendientes/{id}")
    public ResponseEntity<?> getInscriptosPendientes(@PathVariable Integer id) {
        return ResponseEntity.ok().body(carreraService.getInscriptosPendientes(id));
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

    //inscripcion a carrera
    @PostMapping("/api/carrera/inscripcionCarrera")
    public ResponseEntity<?> inscripcionCarrera(@RequestBody DtInscripcionCarrera dtInscripcionCarrera) {
        return carreraService.inscripcionCarrera(dtInscripcionCarrera);
    }

    @PutMapping("/api/carrera/acceptEstudianteCarrera")
    public ResponseEntity<?> acceptEstudianteCarrera(@RequestBody DtInscripcionCarrera dtInscripcionCarrera) throws MessagingException, IOException {
        return carreraService.acceptEstudianteCarrera(dtInscripcionCarrera);
    }

    //Este metodo es temporal, se va a sacar: asignar coordinador a carrera
    @PutMapping("/api/carrera/asignarCoordinadorCarrera/{id}")
    public ResponseEntity<?> asignarCoordinadorCarrera(@PathVariable Integer id, @RequestBody Integer idUsuario) {
        return carreraService.asignarCoordinador(id, idUsuario);
    }
}
