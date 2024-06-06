package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.persistence.Actividad;
import Group4.StudyHubBackendG4.repositories.ActividadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepo actividadRepo;

    public void save(Actividad actividad) {
        actividadRepo.save(actividad);
    }
}
