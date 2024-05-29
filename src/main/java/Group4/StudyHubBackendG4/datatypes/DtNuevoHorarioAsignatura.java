package Group4.StudyHubBackendG4.datatypes;

import lombok.Data;

import java.util.List;

@Data
public class DtNuevoHorarioAsignatura {
    private Integer idDocente;
    private Integer anio;
    private List<DtHorarioDias> dtHorarioDias;     //Array de Integers, ej [2, 4]

}
