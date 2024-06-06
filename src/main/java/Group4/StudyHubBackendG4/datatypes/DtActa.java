package Group4.StudyHubBackendG4.datatypes;

import lombok.Data;

import java.util.List;

@Data
public class DtActa {
    private List<DtUsuario> estudiantes;
    private List<DtDocente> docentes;
    private String asignatura;
    private DtExamen examen;
    private DtHorarioAsignatura horarioAsignatura;
}
