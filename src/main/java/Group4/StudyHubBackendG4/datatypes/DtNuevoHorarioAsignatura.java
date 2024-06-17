package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DtNuevoHorarioAsignatura {
    private Integer idDocente;
    private Integer anio;
    private List<DtHorarioDias> dtHorarioDias;

}
