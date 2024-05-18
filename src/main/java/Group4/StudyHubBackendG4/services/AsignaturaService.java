package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtAsignatura;
import Group4.StudyHubBackendG4.repositories.AsignaturaRepo;
import Group4.StudyHubBackendG4.utils.converters.AsignaturaConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepo asignaturaRepo;

    @Autowired
    private AsignaturaConverter asignaturaConverter;

    public List<DtAsignatura> getAllAsignaturas() {
        return asignaturaRepo.findAll().stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
