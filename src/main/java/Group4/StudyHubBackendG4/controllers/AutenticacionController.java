package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtLoginRequest;
import Group4.StudyHubBackendG4.services.AutenticacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class AutenticacionController {

    @Autowired
    private AutenticacionService autenticacionService;

    @PostMapping("/iniciarSesion")
    public ResponseEntity<?> login(@RequestBody DtLoginRequest loginRequest) {
        try {
            String token = autenticacionService.authenticateUser(loginRequest.getCedula(), loginRequest.getPassword());
            if (token != null) {
                return ResponseEntity.ok().body(token);
            } else {
                return ResponseEntity.status(403).body("Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo iniciar sesion, verifique datos enviados.");
        }
    }

    @PostMapping("/cerrarSesion")
    public ResponseEntity<?> logout(@RequestBody String jwt) {
        try {
            autenticacionService.logoutUser(jwt);
            return ResponseEntity.ok().body("Cerro sesion correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Un error inesperado ocurrio.");
        }
    }
}
