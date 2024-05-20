package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Horario_Dias")
@Data
@NoArgsConstructor
public class HorarioDias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHorarioDias;

    private Integer idDia;
    @ManyToOne
    @JoinColumn(name = "idHorarioAsignatura", nullable = false)
    private HorarioAsignatura horarioAsignatura;
}
