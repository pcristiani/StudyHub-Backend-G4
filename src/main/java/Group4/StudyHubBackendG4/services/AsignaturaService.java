package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.repositories.AsignaturaRepo;
import Group4.StudyHubBackendG4.utils.converters.AsignaturaConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepo asignaturaRepo;

    @Autowired
    private AsignaturaConverter asignaturaConverter;

}
