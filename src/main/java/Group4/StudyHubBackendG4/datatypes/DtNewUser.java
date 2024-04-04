package Group4.StudyHubBackendG4.datatypes;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtNewUser {
    private String name;
    private String surname;
    @Email(message = "Ingrese un Email valido.")
    private String email;
    private String birthdate;
    private String username;
    private String password;

}
