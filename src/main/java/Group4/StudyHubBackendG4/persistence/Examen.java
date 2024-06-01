package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Examen")
@Data
@NoArgsConstructor
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idExamen;
    @ManyToOne
    @JoinColumn(name = "idAsignatura", nullable = false)
    private Asignatura asignatura;
    @ManyToOne
    @JoinColumn(name = "idPeriodoExamen", nullable = false)
    private PeriodoExamen periodoExamen;
    private LocalDateTime fechaHora;
}
