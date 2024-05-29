package Group4.StudyHubBackendG4.datatypes;

import lombok.Data;

@Data
public class DtHorarioAsignatura {
    private Integer idHorarioAsignatura;
    private Integer idAsignatura;
    private Integer anio;
    private Integer horaInicio;
    private Integer horaFin;
}
