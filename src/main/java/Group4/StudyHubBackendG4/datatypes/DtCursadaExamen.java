package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtCursadaExamen {
    private Integer idCursadaExamen;
    private Integer idCursada;
    private Integer idExamen;
    private String nombreEstudiante;
    private String apellidoEstudiante;
    private String mailEstudiante;
    private String cedulaEstudiante;
}