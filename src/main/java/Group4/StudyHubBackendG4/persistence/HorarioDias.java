package Group4.StudyHubBackendG4.persistence;

import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
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

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    private String horaInicio;

    private String horaFin;

    @ManyToOne
    @JoinColumn(name = "idHorarioAsignatura", nullable = false)
    private HorarioAsignatura horarioAsignatura;

}
