package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtNewUser;
import Group4.StudyHubBackendG4.datatypes.DtUser;
import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/getAllUsers")
    public List<DtUser> getAllUsers() {
        return userRepo.findAll().stream()
                .map(User::userToDtUser)
                .collect(Collectors.toList());
    }

    public ResponseEntity<DtUser> getUserById(Integer id) {
        return userRepo.findById(id)
                .map(User::userToDtUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    public ResponseEntity<String> createUser(DtNewUser dtNewUser) {

        Optional<User> existingUser = Optional.ofNullable(userRepo.findByUsername(dtNewUser.getUsername()));
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe, intente con otro.");
        }

        User user = existingUser.orElseGet(User::new)
                .UserFromDtNewUser(dtNewUser);

        userRepo.save(user);

        return ResponseEntity.ok().body("Usuario registrado con éxito.");
    }

    public ResponseEntity<?> updateUser(Integer id, User user) {
        String message = "No se encontró usuario.";
        if (userRepo.existsById(id)) {
            User aux = userRepo.getById(id);
            if (Objects.equals(user.getUsername(), aux.getUsername()) || (!Objects.equals(user.getUsername(), aux.getUsername()) && !userRepo.existsByUsername(user.getUsername()))){
                    aux.setName(user.getName());
                    aux.setSurname(user.getSurname());
                    aux.setEmail(user.getEmail());
                    aux.setBirthdate(user.getBirthdate());
                    aux.setUsername(user.getUsername());
                    userRepo.save(aux);
                    return ResponseEntity.ok().body("Usuario actualizado exitosamente.");
            }else{
               message = "Nombre de usuario ya existe.";
            }
        }
        return ResponseEntity.status(403).body(message);
    }

    public ResponseEntity<?> deleteUser(Integer id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);

            return ResponseEntity.ok().body("Usuario eliminado exitosamente.");
        }
        return ResponseEntity.badRequest().body("Id no existe.");

    }


    public String populateDBWithUsers(){
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
