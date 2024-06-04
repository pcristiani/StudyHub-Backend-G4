package Group4.StudyHubBackendG4.datatypes;

import lombok.Data;

@Data
public class DtPeriodoExamenRequest {
    private String nombre;
    private DtFecha inicio;
    private DtFecha fin;
}
