package Group4.StudyHubBackendG4.datatypes;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DtPeriodoExamen {
    private Integer idPeriodoExamen;
    private Integer idCarrera;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

}
