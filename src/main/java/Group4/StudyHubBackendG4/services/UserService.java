package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtNewUser;
import Group4.StudyHubBackendG4.datatypes.DtUpdateUser;
import Group4.StudyHubBackendG4.datatypes.DtUser;
import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordResetTokenRepo tokenRepo;

    @Autowired
    private JwtService jwtService;

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

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
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

    public ResponseEntity<?> updateUser(Integer id, DtUpdateUser dtUpdateUser) {
        String message = "No se encontró usuario.";
        Optional<User> userOptional = userRepo.findById(id);

        if (userOptional.isPresent()) {
            User aux = userOptional.get();

            if (Objects.equals(dtUpdateUser.getUsername(), aux.getUsername()) || (!Objects.equals(dtUpdateUser.getUsername(), aux.getUsername()) && !userRepo.existsByUsername(dtUpdateUser.getUsername()))) {
                aux.setName(dtUpdateUser.getName() == null || dtUpdateUser.getName().isEmpty() ? aux.getName() : dtUpdateUser.getName());
                aux.setSurname(dtUpdateUser.getSurname() == null || dtUpdateUser.getSurname().isEmpty() ? aux.getSurname() : dtUpdateUser.getSurname());
                aux.setEmail(dtUpdateUser.getEmail() == null || dtUpdateUser.getEmail().isEmpty() ? aux.getEmail() : dtUpdateUser.getEmail());
                aux.setBirthdate(dtUpdateUser.getBirthdate() == null || dtUpdateUser.getBirthdate().isEmpty() ? aux.getBirthdate() : dtUpdateUser.getBirthdate());
                aux.setUsername(dtUpdateUser.getUsername() == null || dtUpdateUser.getUsername().isEmpty() ? aux.getUsername() : dtUpdateUser.getUsername());

                userRepo.save(aux);
                return ResponseEntity.ok().body("Usuario actualizado con exitosamente");
            } else {
                message = "Nombre de usuario ya existe.";
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
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

    public ResponseEntity<?> recuperarPassword(String token, String newPassword) {
        var passwordToken = tokenRepo.findByToken(token);
        if (passwordToken != null) {
            LocalDateTime expiration = passwordToken.getExpiryDateTime();
            LocalDateTime now = LocalDateTime.now();
            if(expiration.isAfter(now)){
                User user = userRepo.getReferenceById(passwordToken.getUser().getId());
                user.setPassword(PasswordService.getInstance().hashPassword(newPassword));
                userRepo.save(user);
                return ResponseEntity.ok().body("Contraseña actualizada con exito");
            } else {
                return ResponseEntity.badRequest().body("Expired Token.");
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
    }

}
