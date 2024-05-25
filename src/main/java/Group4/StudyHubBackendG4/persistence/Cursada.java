package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Cursada")
@Data
@NoArgsConstructor
public class Cursada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCursada;
    @ManyToOne
    @JoinColumn(name = "idAsignatura", nullable = false)
    private Asignatura asignatura;
    @ManyToOne
    @JoinColumn(name = "idHorarioAsignatura", nullable = false)
    private HorarioAsignatura horarioAsignatura;

    private String resultado;
}
