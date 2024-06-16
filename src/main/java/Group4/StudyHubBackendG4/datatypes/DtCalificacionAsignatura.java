package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtCalificacionAsignatura {
    private Integer idAsignatura;
    private String asignatura;
    private List<DtDetalleCalificacionAsignatura> calificaciones;
}
