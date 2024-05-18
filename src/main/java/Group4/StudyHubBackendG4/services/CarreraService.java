package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.CarreraRepo;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import Group4.StudyHubBackendG4.utils.converters.CarreraConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarreraService {

    @Autowired
    private CarreraRepo carreraRepo;

    @Autowired
    private CarreraConverter carreraConverter;

    public List<DtCarrera> getAllCarreras() {
        return carreraRepo.findAll().stream()
                .map(carreraConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
