package Group4.StudyHubBackendG4.datatypes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtNuevoUsuario {
    @NotBlank(message = "Ingrese un nombre.")
    private String nombre;

    @NotBlank(message = "Ingrese un apellido.")
    private String apellido;

    @NotBlank(message = "Ingrese un email.")
    @Email(message = "Ingrese un Email valido.")
    private String email;

    @NotBlank(message = "Ingrese una fecha de nacimiento.")
    private String fechaNacimiento;

    @NotBlank(message = "Ingrese una cedula.")
    private String cedula;

    @NotBlank(message = "Ingrese una contraseña.")
    private String password;

    @NotBlank(message = "Ingrese una cedula.")
    private String rol;
}
