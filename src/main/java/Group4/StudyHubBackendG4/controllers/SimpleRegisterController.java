package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtUser;
import Group4.StudyHubBackendG4.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleRegisterController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/register/test")
    public ResponseEntity<?> registerUser(@RequestBody DtUser dtUser) {
        try {
            DtUser registeredUser = registrationService.registerUser(dtUser);
            if (registeredUser != null) {
                return ResponseEntity.ok().body("Usuario registrado con éxito.");
            } else {
                return ResponseEntity.status(400).body("No se pudo registrar al usuario.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
