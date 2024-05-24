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
    private Integer horaInicio;

    @NotNull
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

    public Integer getHoraFin() {
        return horaFin;
    }

}
