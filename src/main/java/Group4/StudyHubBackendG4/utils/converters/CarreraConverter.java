package Group4.StudyHubBackendG4.utils.converters;

import Group4.StudyHubBackendG4.datatypes.DtAsignatura;
import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.Carrera;
import org.springframework.stereotype.Component;

@Component
public class CarreraConverter extends AbstractGenericConverter<Carrera, DtCarrera> {
    public CarreraConverter() {
        super(Carrera.class, DtCarrera.class);
    }
}

