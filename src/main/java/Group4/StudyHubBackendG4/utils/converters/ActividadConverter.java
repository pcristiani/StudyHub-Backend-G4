package Group4.StudyHubBackendG4.utils.converters;

import Group4.StudyHubBackendG4.datatypes.DtActividad;
import Group4.StudyHubBackendG4.persistence.Actividad;
import org.springframework.stereotype.Component;

@Component
public class ActividadConverter extends AbstractGenericConverter<Actividad, DtActividad> {
    public ActividadConverter() {
        super(Actividad.class, DtActividad.class);
    }
}
