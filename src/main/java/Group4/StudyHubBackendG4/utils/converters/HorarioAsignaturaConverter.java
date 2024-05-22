package Group4.StudyHubBackendG4.utils.converters;

import Group4.StudyHubBackendG4.datatypes.DtHorarioAsignatura;
import Group4.StudyHubBackendG4.persistence.HorarioAsignatura;
import org.springframework.stereotype.Component;

@Component
public class HorarioAsignaturaConverter extends AbstractGenericConverter<HorarioAsignatura, DtHorarioAsignatura> {
    public HorarioAsignaturaConverter() {
        super(HorarioAsignatura.class, DtHorarioAsignatura.class);
    }
}