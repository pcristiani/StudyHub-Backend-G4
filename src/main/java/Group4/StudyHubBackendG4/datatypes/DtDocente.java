package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtDocente {
    private Integer idDocente;
    private Integer codigoDocente;
    private String nombre;
    private Boolean activo;
}
