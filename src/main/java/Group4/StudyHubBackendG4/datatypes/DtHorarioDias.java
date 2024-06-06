package Group4.StudyHubBackendG4.datatypes;

import Group4.StudyHubBackendG4.persistence.HorarioDias;
import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtHorarioDias {
    @Setter
    @NotNull
    private DiaSemana diaSemana;

    @NotNull
    private String horaInicio;

    @NotNull
    private String horaFin;

    public static DtHorarioDias horarioDiasfromDtHorarioDias(HorarioDias horarioDias) {
        if (horarioDias == null) {
            return null;
        }

        DtHorarioDias dto = new DtHorarioDias();
        dto.setDiaSemana(horarioDias.getDiaSemana());
        dto.setHoraInicio(horarioDias.getHoraInicio());
        dto.setHoraFin(horarioDias.getHoraFin());

        return dto;
    }

}
