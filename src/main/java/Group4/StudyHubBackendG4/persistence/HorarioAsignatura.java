package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Horario_Asignatura")
@Data
@NoArgsConstructor
public class HorarioAsignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHorarioAsignatura;

    @ManyToOne
    @JoinColumn(name = "idAsignatura", nullable = false)
    private Asignatura asignatura;

    @Min(1900)
    @Max(2200)
    private Integer anio;

    @OneToMany(mappedBy = "horarioAsignatura")
    private List<HorarioDias> horarioDias;

}
