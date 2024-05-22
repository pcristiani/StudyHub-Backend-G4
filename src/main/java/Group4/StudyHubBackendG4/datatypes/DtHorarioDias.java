package Group4.StudyHubBackendG4.datatypes;

import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtHorarioDias {
    @NotNull
    private DiaSemana diaSemana;

    @NotNull
    @Min(0)
    @Max(23)
    private Integer horaInicio;

    @NotNull
    @Min(0)
    @Max(23)
    private Integer horaFin;

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Integer getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Integer horaInicio) {
        if (horaInicio < 0 || horaInicio > 23) {
            throw new IllegalArgumentException("horaInicio debe ser un valor entre 0 y 23");
        }
        this.horaInicio = horaInicio;
    }

    public Integer getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Integer horaFin) {
        if (horaFin < 0 || horaFin > 23) {
            throw new IllegalArgumentException("horaFin debe ser un valor entre 0 y 23");
        }
        this.horaFin = horaFin;
    }
}
