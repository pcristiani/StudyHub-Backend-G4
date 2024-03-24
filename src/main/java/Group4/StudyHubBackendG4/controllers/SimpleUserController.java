package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtNewUser;
import Group4.StudyHubBackendG4.datatypes.DtUser;
import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users")
@RestController
public class SimpleUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public List<DtUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> createUser(@RequestBody DtNewUser dtNewUser) {
        return userService.createUser(dtNewUser);
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
