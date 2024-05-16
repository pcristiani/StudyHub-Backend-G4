package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Docente_Horario_Asignatura")
@Data
@NoArgsConstructor
public class DocenteHorarioAsignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDHA;
    @ManyToOne
    @JoinColumn(name = "idDocente", nullable = false)
    private Docente docente;
    @ManyToOne
    @JoinColumn(name = "idHorarioAsignatura", nullable = false)
    private HorarioAsignatura horarioAsignatura;
}
