package Group4.StudyHubBackendG4.datatypes;

import Group4.StudyHubBackendG4.persistence.HorarioAsignatura;
import Group4.StudyHubBackendG4.persistence.HorarioDias;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DtHorarioAsignatura {
    private Integer idHorarioAsignatura;
    private Integer idAsignatura;
    private Integer anio;
    private List<DtHorarioDias> dtHorarioDias;

    public static DtHorarioAsignatura horarioAsignaturafromDtHorarioAsignatura(HorarioAsignatura horarioAsignatura) {

        if (horarioAsignatura == null) {
            return null;
        }

        DtHorarioAsignatura dto = new DtHorarioAsignatura();
        dto.setIdHorarioAsignatura(horarioAsignatura.getIdHorarioAsignatura());
        dto.setIdAsignatura(horarioAsignatura.getAsignatura().getIdAsignatura());
        dto.setAnio(horarioAsignatura.getAnio());

        if (horarioAsignatura.getHorarioDias() != null && !horarioAsignatura.getHorarioDias().isEmpty()) {
            List<DtHorarioDias> dtHorarioDiasList = horarioAsignatura.getHorarioDias().stream()
                    .map(HorarioDias::horarioDiasfromDtHorarioDias)
                    .collect(Collectors.toList());
            dto.setDtHorarioDias(dtHorarioDiasList);
        }
        return dto;
    }
}
