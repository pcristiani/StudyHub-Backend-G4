package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtNewUser;
import Group4.StudyHubBackendG4.datatypes.DtUser;
import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private UserRepo userRepository;

    public DtUser registerUser(DtNewUser dtNewUser) throws Exception {        //Todo: Mover a userService
        User userAux = userRepository.findByUsername(dtNewUser.getUsername());

        if (userAux != null) {
            throw new Exception("El nombre de usuario ya existe, intente con otro.");
        }

        User user = new User();
        user.setName(dtNewUser.getName());
        user.setSurname(dtNewUser.getSurname());
        user.setEmail(dtNewUser.getEmail());
        user.setBirthdate(dtNewUser.getBirthdate());
        user.setUsername(dtNewUser.getUsername());
        user.setPassword(PasswordService.getInstance().hashPassword(dtNewUser.getPassword()));
        user.setJwtToken(JwtService.getInstance().generateJwt(user));

        user = userRepository.save(user);
        return new DtUser(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getBirthdate(), user.getUsername(), user.getPassword(), user.getJwtToken());
    }
}
