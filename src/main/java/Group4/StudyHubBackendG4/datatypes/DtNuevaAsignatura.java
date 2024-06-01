package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtNuevaAsignatura {
    private Integer IdCarrera;
    private List<Integer> IdDocentes;
    private String nombre;
    private Integer creditos;
    private String descripcion;
    private String departamento;
    private Boolean tieneExamen;
    private Boolean activa;
    private List<Integer> previaturas;

    public DtAsignatura dtAsignaturaFromDtNuevaAsignatura(DtNuevaAsignatura dtNuevaAsignatura) {
        return new DtAsignatura(
                null,
                dtNuevaAsignatura.getIdCarrera(),
                dtNuevaAsignatura.getNombre(),
                dtNuevaAsignatura.getCreditos(),
                dtNuevaAsignatura.getDescripcion(),
                dtNuevaAsignatura.getDepartamento(),
                dtNuevaAsignatura.getTieneExamen(),
                dtNuevaAsignatura.getActiva(),
                dtNuevaAsignatura.getPreviaturas());
    }
}


