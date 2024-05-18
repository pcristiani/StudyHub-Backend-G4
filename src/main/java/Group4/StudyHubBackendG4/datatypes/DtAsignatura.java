package Group4.StudyHubBackendG4.datatypes;

import Group4.StudyHubBackendG4.persistence.Carrera;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtAsignatura {
    private Integer idAsignatura;
    private Carrera carrera;
    private String nombre;
    private Integer creditos;
    private String descripcion;
    private String departamento;
    private Boolean activa;
}
