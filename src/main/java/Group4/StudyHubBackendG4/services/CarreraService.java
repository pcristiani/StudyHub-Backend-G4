package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.CarreraRepo;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarreraService {

    @Autowired
    private CarreraRepo carreraRepo;

    public List<DtCarrera> getAllCarreras() {
        return carreraRepo.findAll().stream()
                .map(Usuario::userToDtUser)     //Cambiar a mapeo con util de pedro
                .collect(Collectors.toList());
    }
}
