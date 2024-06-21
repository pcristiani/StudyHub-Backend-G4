package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
public class DtNuevoExamen {

    private Integer idAsignatura;
    private Integer idPeriodo;
    private List<Integer> idsDocentes;
    private LocalDateTime fechaHora;
}
