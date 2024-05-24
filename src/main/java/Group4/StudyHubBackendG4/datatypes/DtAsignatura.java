package Group4.StudyHubBackendG4.datatypes;

import Group4.StudyHubBackendG4.persistence.Carrera;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtAsignatura {
    private Integer idAsignatura;
    private Integer IdCarrera;
    private String nombre;
    private Integer creditos;
    private String descripcion;
    private String departamento;
    private Boolean activa;
    private List<Integer> previaturas;

    
}


