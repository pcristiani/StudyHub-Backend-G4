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

    @Min(0)
    @Max(23)
    private Integer horaInicio;

    @Min(0)
    @Max(23)
    private Integer horaFin;

    @ManyToOne
    @JoinColumn(name = "idHorarioAsignatura", nullable = false)
    private HorarioAsignatura horarioAsignatura;


    public void setHoraInicio(Integer horaInicio) {
        if (horaInicio < 0 || horaInicio > 23) {
            throw new IllegalArgumentException("horaInicio debe ser un valor entre 0 y 23");
        }
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(Integer horaFin) {
        if (horaFin < 0 || horaFin > 23) {
            throw new IllegalArgumentException("horaFin debe ser un valor entre 0 y 23");
        }
        this.horaFin = horaFin;
    }
}
