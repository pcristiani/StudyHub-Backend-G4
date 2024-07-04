package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.services.CarreraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Carrera", description = "Endpoints para la operativa de Carreras")
public class CarreraController {

    @Autowired
    private CarreraService carreraService;

    @Operation(summary = "Obtiene todas las carreras públicas")
    @GetMapping("/getCarrerasPublic")
    public ResponseEntity<?> getCarrerasPublic() {
        return ResponseEntity.ok().body(carreraService.getCarreras());
    }

    @Operation(summary = "Obtiene todas las carreras")
    @GetMapping("/api/carrera/getCarreras")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getCarreras() {
        return ResponseEntity.ok().body(carreraService.getCarreras());
    }

    @Operation(summary = "Obtiene una carrera por su id")
    @GetMapping("/api/carrera/getCarreraById/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_C') or hasRole('ROLE_A') or hasRole('ROLE_F') or hasRole('ROLE_E')")
    public ResponseEntity<?> getCarreraById(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok().body(carreraService.getCarreraById(idCarrera));
    }

    @Operation(summary = "Obtiene las carreras con inscripciones pendientes")
    @GetMapping("/api/carrera/getCarrerasInscripcionesPendientes")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getCarrerasInscripcionesPendientes() {
        return ResponseEntity.ok().body(carreraService.getCarrerasInscripcionesPendientes());
    }

    @Operation(summary = "Obtiene las carreras en las que está inscripto un estudiante")
    @GetMapping("/api/carrera/getCarrerasInscripto/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getCarrerasInscripto(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok().body(carreraService.getCarrerasInscripto(idUsuario));
    }

    @Operation(summary = "Obtiene las carreras en las que es coordinador un usuario")
    @GetMapping("/api/carrera/getCarrerasCoordinador/{idUsuario}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> getCarrerasCoordinador(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok().body(carreraService.getCarrerasCoordinador(idUsuario));
    }

    @Operation(summary = "Obtiene las carreras con inscripciones pendientes")
    @GetMapping("/api/carrera/getInscriptosPendientes/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getInscriptosPendientes(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok().body(carreraService.getInscriptosPendientes(idCarrera));
    }

    @Operation(summary = "Obtiene las carreras con periodos de examen")
    @GetMapping("/api/carrera/getCarrerasConPeriodo")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getCarrerasConPeriodo() {
        return ResponseEntity.ok().body(carreraService.getCarrerasConPeriodo());
    }

    @Operation(summary = "Obtiene las los periodos de examen de una carrera")
    @GetMapping("/api/carrera/getPeriodosDeCarrera/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> getPeriodosDeCarrera(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok().body(carreraService.getPeriodosDeCarrera(idCarrera));
    }

    @Operation(summary = "Obtiene las previaturas de una carrera")
    @GetMapping("/getPreviaturasGrafo/{idCarrera}")
    public ResponseEntity<?> getPreviaturasGrafo(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok().body(carreraService.getPreviaturasGrafo(idCarrera));
    }

    @Operation(summary = "Da de alta una carrera")
    @PostMapping("/api/carrera/altaCarrera")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_C')")
    public ResponseEntity<?> altaCarrera(@Valid @RequestBody DtNuevaCarrera dtNuevaCarrera) {
        return carreraService.nuevaCarrera(dtNuevaCarrera);
    }

    @Operation(summary = "Da de alta un periodo de examen")
    @PostMapping("/api/carrera/altaPeriodoDeExamen/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> altaPeriodoDeExamen(@PathVariable Integer idCarrera, @RequestBody DtPeriodoExamenRequest fechas) {
        return carreraService.altaPeriodoDeExamen(idCarrera, fechas);
    }

    @Operation(summary = "Inscribe un estudiante a una carrera")
    @PostMapping("/api/carrera/inscripcionCarrera")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_E')")
    public ResponseEntity<?> inscripcionCarrera(@RequestBody DtInscripcionCarrera dtInscripcionCarrera) {
        return carreraService.inscripcionCarrera(dtInscripcionCarrera);
    }

    @Operation(summary = "Acepta la inscripción de un estudiante a una carrera")
    @PutMapping("/api/carrera/acceptEstudianteCarrera")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_F')")
    public ResponseEntity<?> acceptEstudianteCarrera(@RequestBody DtInscripcionCarrera dtInscripcionCarrera) throws MessagingException, IOException {
        return carreraService.acceptEstudianteCarrera(dtInscripcionCarrera);
    }

    @Operation(summary = "Asigna un coordinador a una carrera")
    @PutMapping("/api/carrera/asignarCoordinadorCarrera/{idCarrera}")
    @PreAuthorize("hasRole('ROLE_A') or hasRole('ROLE_C')")
    public ResponseEntity<?> asignarCoordinadorCarrera(@PathVariable Integer idCarrera, @RequestBody Integer idUsuario) {
        return carreraService.asignarCoordinador(idCarrera, idUsuario);
    }
}
