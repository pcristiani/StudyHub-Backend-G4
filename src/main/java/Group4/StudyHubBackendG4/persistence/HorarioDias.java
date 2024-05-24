package Group4.StudyHubBackendG4.persistence;

import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    private Integer horaInicio;

    private Integer horaFin;

    @ManyToOne
    @JoinColumn(name = "idHorarioAsignatura", nullable = false)
    private HorarioAsignatura horarioAsignatura;

}
