package Group4.StudyHubBackendG4.datatypes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtNewUser {
    @NotBlank(message = "Ingrese un nombre.")
    private String name;

    @NotBlank(message = "Ingrese un apellido.")
    private String surname;

    @NotBlank(message = "Ingrese un email.")
    @Email(message = "Ingrese un Email valido.")
    private String email;

    @NotBlank(message = "Ingrese una fecha de nacimiento.")
    private String birthdate;

    @NotBlank(message = "Ingrese un nombre de usuario.")
    private String username;

    @NotBlank(message = "Ingrese una contraseña.")
    private String password;

}
