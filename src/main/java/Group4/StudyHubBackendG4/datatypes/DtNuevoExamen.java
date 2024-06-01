package Group4.StudyHubBackendG4.datatypes;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class DtNuevoExamen {

    private Integer idAsignatura;
    private Integer idPeriodo;
    private List<Integer> idsDocentes;
    private LocalDateTime fechaHora;
}
