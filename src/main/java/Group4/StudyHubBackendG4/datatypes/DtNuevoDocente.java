package Group4.StudyHubBackendG4.datatypes;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtNuevoDocente {
    private Integer codigoDocente;

    @NotBlank(message = "Ingrese un nombre.")
    private String nombre;
}
