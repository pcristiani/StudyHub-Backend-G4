package Group4.StudyHubBackendG4.controllers;

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

@RequestMapping("/api/users")
@RestController
public class SimpleUserController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;
    
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> createUser(@RequestBody DtUser dtuser) {
        try {
            DtUser registeredUser = registrationService.registerUser(dtuser);
            if (registeredUser != null) {
                return ResponseEntity.ok().body("Usuario registrado con éxito.");
            } else {
                return ResponseEntity.status(400).body("No se pudo registrar al usuario.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody DtUser dtuser) {
        User user = new User(dtuser);
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/populateDBWithUsers")
    public String populateDBWithUsers(){
        return userService.populateDBWithUsers();
    }
}
