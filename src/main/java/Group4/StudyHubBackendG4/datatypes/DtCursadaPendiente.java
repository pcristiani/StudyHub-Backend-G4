package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtCursadaPendiente {
    private Integer idEstudiante;
    private Integer idCursada;
    private String nombreEstudiante;
    private String apellidoEstudiante;
    private String mailEstudiante;

}