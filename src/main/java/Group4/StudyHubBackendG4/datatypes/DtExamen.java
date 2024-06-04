package Group4.StudyHubBackendG4.datatypes;

import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.PeriodoExamen;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class DtExamen {
    private Integer idExamen;
    private String asignatura;
    private String periodoExamen;
    private LocalDateTime fechaHora;
}

