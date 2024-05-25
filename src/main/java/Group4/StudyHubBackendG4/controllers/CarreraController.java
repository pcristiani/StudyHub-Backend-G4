package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.*;
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
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class CarreraController {

    @Autowired
    private CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping("/api/carrera/getCarreras")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getCarreras() {
        return ResponseEntity.ok().body(carreraService.getCarreras());
    }

    @GetMapping("/api/carrera/getCarrerasInscripcionesPendientes")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getCarrerasInscripcionesPendientes() {
        return ResponseEntity.ok().body(carreraService.getCarrerasInscripcionesPendientes());
    }

    @GetMapping("/api/carrera/getCarrerasInscripto/{id}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getCarrerasInscripto(@PathVariable Integer id) {
        return ResponseEntity.ok().body(carreraService.getCarrerasInscripto(id));
    }

    @GetMapping("/api/carrera/getInscriptosPendientes/{id}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getInscriptosPendientes(@PathVariable Integer id) {
        return ResponseEntity.ok().body(carreraService.getInscriptosPendientes(id));
    }

    @PostMapping("/api/carrera/altaCarrera")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_C')")
    public ResponseEntity<?> altaCarrera(@Valid @RequestBody DtNuevaCarrera dtNuevaCarrera) throws MessagingException, IOException {
        return carreraService.nuevaCarrera(dtNuevaCarrera);
    }


    @PostMapping("/api/carrera/altaPeriodoDeExamen/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> altaPeriodoDeExamen(@PathVariable Integer idCarrera, @RequestBody DtPeriodoExamenRequest fechas) {
        return carreraService.altaPeriodoDeExamen(idCarrera, fechas);
    }

    @PutMapping("/api/carrera/modificarCarrera/{id}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_C')")
    public ResponseEntity<?> modificarCarrera(@PathVariable Integer id, @RequestBody DtCarrera dtCarrera) {
        return carreraService.modificarCarrera(id, dtCarrera);
    }

    //inscripcion a carrera
    @PostMapping("/api/carrera/inscripcionCarrera")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> inscripcionCarrera(@RequestBody DtInscripcionCarrera dtInscripcionCarrera) {
        return carreraService.inscripcionCarrera(dtInscripcionCarrera);
    }

    @PutMapping("/api/carrera/acceptEstudianteCarrera")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> acceptEstudianteCarrera(@RequestBody DtInscripcionCarrera dtInscripcionCarrera) throws MessagingException, IOException {
        return carreraService.acceptEstudianteCarrera(dtInscripcionCarrera);
    }

    //Este metodo es temporal, se va a sacar: asignar coordinador a carrera
    @PutMapping("/api/carrera/asignarCoordinadorCarrera/{id}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_C')")
    public ResponseEntity<?> asignarCoordinadorCarrera(@PathVariable Integer id, @RequestBody Integer idUsuario) {
        return carreraService.asignarCoordinador(id, idUsuario);
    }
}
