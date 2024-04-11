package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtUser {
    private Integer id;
    private String ci;
    private String name;
    private String surname;
    private String email;
    private String birthdate;
    private String username;

}
