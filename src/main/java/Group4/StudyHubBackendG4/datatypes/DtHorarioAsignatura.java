package Group4.StudyHubBackendG4.datatypes;

import Group4.StudyHubBackendG4.persistence.HorarioAsignatura;
import Group4.StudyHubBackendG4.persistence.HorarioDias;
import lombok.Data;

@Data
public class DtHorarioAsignatura {
    private Integer idAsignatura;
    private Integer anio;
    private DtHorarioDias dtHorarioDias;

    public static DtHorarioAsignatura horarioAsignaturafromDtHorarioAsignatura(HorarioAsignatura horarioAsignatura) {
        if (horarioAsignatura == null) {
            return null;
        }

        DtHorarioAsignatura dto = new DtHorarioAsignatura();
        dto.setIdAsignatura(horarioAsignatura.getAsignatura().getIdAsignatura());
        dto.setAnio(horarioAsignatura.getAnio());

        if (horarioAsignatura.getHorarioDias() != null && !horarioAsignatura.getHorarioDias().isEmpty()) {
            HorarioDias horarioDias = horarioAsignatura.getHorarioDias().get(0);
            dto.setDtHorarioDias(DtHorarioDias.horarioDiasfromDtHorarioDias(horarioDias));
        }

        return dto;
    }
}
