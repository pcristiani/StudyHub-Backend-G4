package Group4.StudyHubBackendG4.utils.converters;

import Group4.StudyHubBackendG4.datatypes.DtDocente;
import Group4.StudyHubBackendG4.persistence.Docente;
import org.springframework.stereotype.Component;

@Component
public class DocenteConverter extends AbstractGenericConverter<Docente, DtDocente> {
    public DocenteConverter() {
        super(Docente.class, DtDocente.class);
    }
}
