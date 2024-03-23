package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtUser;
import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private UserRepo userRepository;

    public DtUser registerUser(DtUser dtUser) throws Exception {
        User userAux = userRepository.findByUsername(dtUser.getUsername());

        if (userAux != null) {
            throw new Exception("El nombre de usuario ya existe, intente con otro.");
        }

        User user = new User();
        user.setName(dtUser.getName());
        user.setSurname(dtUser.getSurname());
        user.setEmail(dtUser.getEmail());
        user.setBirthdate(dtUser.getBirthdate());
        user.setUsername(dtUser.getUsername());
        user.setPassword(PasswordService.getInstance().hashPassword(dtUser.getPassword()));
        user.setJwtToken(JwtService.getInstance().generateJwt(user));

        user = userRepository.save(user);
        return new DtUser(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getBirthdate(), user.getUsername(), user.getPassword(), user.getJwtToken());
    }
}
