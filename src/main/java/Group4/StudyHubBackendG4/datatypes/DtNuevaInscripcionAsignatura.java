package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtNuevaInscripcionAsignatura {
    private Integer idEstudiante;
    private Integer idAsignatura;
    private Integer idHorario;
}
