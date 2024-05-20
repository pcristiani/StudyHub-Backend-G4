package Group4.StudyHubBackendG4.datatypes;

import lombok.Data;

@Data
public class DtHorarioAsignatura {
    private Integer anio;
    private DtDocente docente;
    private DtHorarioDias dias;
    private Integer horaInicio;
    private Integer horaFin;
}
