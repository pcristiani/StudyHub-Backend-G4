package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtLoginRequest;
import Group4.StudyHubBackendG4.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class SimpleLoginController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login/test")
    public ResponseEntity<?> login(@RequestBody DtLoginRequest loginRequest) {
        try {
            String token = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            if (token != null) {
                return ResponseEntity.ok().body(token);
            } else {
                return ResponseEntity.status(403).body("Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Un error ocurrio al intentar autenticar al usuario");
        }
    }
}
