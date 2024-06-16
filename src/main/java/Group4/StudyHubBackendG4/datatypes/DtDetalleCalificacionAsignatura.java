package Group4.StudyHubBackendG4.datatypes;

import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtDetalleCalificacionAsignatura {
    private ResultadoAsignatura resultado;
    private int calificacion;
}
