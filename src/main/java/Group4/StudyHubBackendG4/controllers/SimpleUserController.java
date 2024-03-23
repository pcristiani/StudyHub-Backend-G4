package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController
public class SimpleUserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/getUsers")
    public String getAllUsers(){
        return userRepo.findAll().stream().toList().toString();
    }

    @GetMapping("/insertUsers")
    public String insertUser(){
        User u = new User();
        u.setId(1);
        u.setName("Javier");
        u.setSurname("Rydel");
        u.setEmail("javierydel98@gmail.com");
        u.setUsername("colo_102");
        u.setBirthdate("19980903");
        u.setPassword("1234");
        u.encryptPassword();
        u.setRandomJwt();
        return userRepo.save(u).toString();
    }
}
