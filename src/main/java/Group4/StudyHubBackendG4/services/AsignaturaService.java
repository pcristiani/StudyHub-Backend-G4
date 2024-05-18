package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtAsignatura;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.AsignaturaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepo asignaturaRepo;

    public List<DtAsignatura> getAllAsignaturas() {
        return carreraRepo.findAll().stream()
                .map(Usuario::userToDtUser)     //Cambiar a mapeo con util de pedro
                .collect(Collectors.toList());
    }
}
