package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtUsuario {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String fechaNacimiento;
    private String rol;
    private String cedula;
    private Boolean activo;
}
