package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtNewUser;
import Group4.StudyHubBackendG4.datatypes.DtUser;
import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.services.JwtService;
import Group4.StudyHubBackendG4.services.RegistrationService;
import Group4.StudyHubBackendG4.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/users")
@RestController
public class SimpleUserController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public List<DtUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<DtUser> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> createUser(@RequestBody DtNewUser dtNewUser) {      //TODO: Devolver https desde service
        try {
            DtUser registeredUser = registrationService.registerUser(dtNewUser);
            if (registeredUser != null) {
                return ResponseEntity.ok().body("Usuario registrado con éxito.");
            } else {
                return ResponseEntity.status(400).body("No se pudo registrar al usuario.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody DtUser dtuser) {
        User user = new User(dtuser);
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/populateDBWithUsers")
    public String populateDBWithUsers(){
        return userService.populateDBWithUsers();
    }
}
