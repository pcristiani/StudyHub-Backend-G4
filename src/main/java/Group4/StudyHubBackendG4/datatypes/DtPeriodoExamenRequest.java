package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtPeriodoExamenRequest {
    private String nombre;
    private DtFecha inicio;
    private DtFecha fin;
}
