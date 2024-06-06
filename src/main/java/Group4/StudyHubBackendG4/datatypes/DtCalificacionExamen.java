package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtCalificacionExamen {
    private Integer idAsignatura;
    private String asignatura;
    private Integer idExamen;
    private String calificacion;
}
