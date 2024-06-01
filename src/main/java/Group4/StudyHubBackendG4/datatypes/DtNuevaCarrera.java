package Group4.StudyHubBackendG4.datatypes;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtNuevaCarrera {
    @NotBlank(message = "Ingrese un nombre.")
    private String nombre;

    @NotBlank(message = "Ingrese una descripción.")
    private String descripcion;

    private String requisitos;
    private Integer duracion;
}
