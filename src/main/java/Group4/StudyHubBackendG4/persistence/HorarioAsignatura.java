package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer anio;
    private Integer horaInicio;
    private Integer horaFin;
}
