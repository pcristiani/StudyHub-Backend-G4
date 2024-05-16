package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Previaturas")
@Data
@NoArgsConstructor
public class Previaturas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPrevAsig;
    @ManyToOne
    @JoinColumn(name = "idAsignatura", nullable = false)
    private Asignatura asignatura;
    @ManyToOne
    @JoinColumn(name = "idPrevia", nullable = false)
    private Asignatura previa;

}
