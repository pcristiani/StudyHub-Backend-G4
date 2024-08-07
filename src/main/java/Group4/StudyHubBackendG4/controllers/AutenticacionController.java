package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtLoginRequest;
import Group4.StudyHubBackendG4.persistence.Actividad;
import Group4.StudyHubBackendG4.services.AutenticacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Tag(name = "Autenticacion", description = "Endpoints para la operativa de Autenticacion")
public class AutenticacionController {

    @Autowired
    private AutenticacionService autenticacionService;

    @PostMapping("/iniciarSesion")
    @Operation(summary = "Inicia sesion de un usuario")
    public ResponseEntity<?> login(@RequestBody DtLoginRequest loginRequest) {
        try {
            String token = autenticacionService.authenticateUser(loginRequest.getCedula(), loginRequest.getPassword());
            if (token != null && !token.equals("notJoined")) {
                return ResponseEntity.ok().body(token);
            } else if ("notJoined".equals(token)) {
                return ResponseEntity.status(403).body("El usuario no esta validado o se encuentra inactivo.");
            }else {
                return ResponseEntity.status(403).body("Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo iniciar sesion, verifique datos enviados.");
        }
    }

    @PostMapping("/cerrarSesion")
    @Operation(summary = "Cierra sesion de un usuario")
    public ResponseEntity<?> logout(@RequestBody String jwt) {
        try {
            autenticacionService.logoutUser(jwt);
            return ResponseEntity.ok().body("Cerro sesion correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Un error inesperado ocurrio.");
        }
    }
}
