package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtInscripcionCarrera {
    private Integer idCarrera;
    private Integer idEstudiante;
    private Boolean validado;
}
