package Group4.StudyHubBackendG4.utils.converters;

import Group4.StudyHubBackendG4.datatypes.DtPeriodoExamen;
import Group4.StudyHubBackendG4.persistence.PeriodoExamen;
import org.springframework.stereotype.Component;

@Component
public class PeriodoConverter extends AbstractGenericConverter<PeriodoExamen, DtPeriodoExamen> {
    public PeriodoConverter() {
        super(PeriodoExamen.class, DtPeriodoExamen.class);
    }
}
