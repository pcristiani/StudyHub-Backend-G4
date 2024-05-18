package Group4.StudyHubBackendG4.utils.converters;

import Group4.StudyHubBackendG4.datatypes.DtAsignatura;
import Group4.StudyHubBackendG4.datatypes.DtDocente;
import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.Docente;
import org.springframework.stereotype.Component;

@Component
public class AsignaturaConverter extends AbstractGenericConverter<Asignatura, DtAsignatura> {
    public AsignaturaConverter() {
        super(Asignatura.class, DtAsignatura.class);
    }
}

