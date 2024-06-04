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
@RequestMapping("/api/carrera")
public class CarreraController {

    @Autowired
    private CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping("/getCarreras")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getCarreras() {
        return ResponseEntity.ok().body(carreraService.getCarreras());
    }

    @GetMapping("/getCarrerasInscripcionesPendientes")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getCarrerasInscripcionesPendientes() {
        return ResponseEntity.ok().body(carreraService.getCarrerasInscripcionesPendientes());
    }

    @GetMapping("/getCarrerasInscripto/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getCarrerasInscripto(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok().body(carreraService.getCarrerasInscripto(idUsuario));
    }

    @GetMapping("/getInscriptosPendientes/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getInscriptosPendientes(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok().body(carreraService.getInscriptosPendientes(idCarrera));
    }
    @GetMapping("/getCarrerasConPeriodo")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getCarrerasConPeriodo() {
        return ResponseEntity.ok().body(carreraService.getCarrerasConPeriodo());
    }
    @GetMapping("/getPeriodosDeCarrera/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getPeriodosDeCarrera(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok().body(carreraService.getPeriodosDeCarrera(idCarrera));
    }


    @PostMapping("/altaCarrera")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_C')")
    public ResponseEntity<?> altaCarrera(@Valid @RequestBody DtNuevaCarrera dtNuevaCarrera) throws MessagingException, IOException {
        return carreraService.nuevaCarrera(dtNuevaCarrera);
    }


    @PostMapping("/altaPeriodoDeExamen/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> altaPeriodoDeExamen(@PathVariable Integer idCarrera, @RequestBody DtPeriodoExamenRequest fechas) {
        return carreraService.altaPeriodoDeExamen(idCarrera, fechas);
    }

    @PutMapping("/modificarCarrera/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_C')")
    public ResponseEntity<?> modificarCarrera(@PathVariable Integer idCarrera, @RequestBody DtCarrera dtCarrera) {
        return carreraService.modificarCarrera(idCarrera, dtCarrera);
    }

    //inscripcion a carrera
    @PostMapping("/inscripcionCarrera")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> inscripcionCarrera(@RequestBody DtInscripcionCarrera dtInscripcionCarrera) {
        return carreraService.inscripcionCarrera(dtInscripcionCarrera);
    }

    @PutMapping("/acceptEstudianteCarrera")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> acceptEstudianteCarrera(@RequestBody DtInscripcionCarrera dtInscripcionCarrera) throws MessagingException, IOException {
        return carreraService.acceptEstudianteCarrera(dtInscripcionCarrera);
    }

    @PutMapping("/asignarCoordinadorCarrera/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_C')")
    public ResponseEntity<?> asignarCoordinadorCarrera(@PathVariable Integer idCarrera, @RequestBody Integer idUsuario) {
        return carreraService.asignarCoordinador(idCarrera, idUsuario);
    }
}
