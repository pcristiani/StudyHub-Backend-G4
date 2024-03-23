package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import Group4.StudyHubBackendG4.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class SimpleUserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/getUsers")
    public String getAllUsers(){
        return userRepo.findAll().stream().toList().toString();
    }

    @GetMapping("/insertUsersAuto")
    public String insertUser(){
        Random rand = new Random();
        List<User> users = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");

        for (int i = 1; i <= 5; i++) {
            User u = new User();
            String name = "Nombre" + (rand.nextInt(900) + 100);
            String surname = "Apellido" + (rand.nextInt(900) + 100);
            String email = "usuario" + (rand.nextInt(900) + 100) + "@ejemplo.com";
            String username = "usuario_" + (rand.nextInt(900) + 100);

            int minDay = (int) LocalDate.of(1970, 1, 1).toEpochDay();
            int maxDay = (int) LocalDate.of(2000, 12, 31).toEpochDay();
            long randomDay = minDay + rand.nextInt(maxDay - minDay);
            LocalDate birthdate = LocalDate.ofEpochDay(randomDay);
            String formattedDate = birthdate.format(dtf);

            u.setName(name);
            u.setSurname(surname);
            u.setEmail(email);
            u.setUsername(username);
            u.setBirthdate(formattedDate);
            u.setPassword("1234");
            u.setJwtToken(JwtService.getInstance().generateJwt(u));
            u.encryptPassword();
            users.add(u);
            userRepo.save(u);
        }
        return users.toString();
    }
}
