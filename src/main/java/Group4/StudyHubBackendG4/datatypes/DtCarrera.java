package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtCarrera {
    private Integer idCarrera;
    private String nombre;
    private String descripcion;
    private String requisitos;
    private Integer duracion;
    private Boolean activa;
}
