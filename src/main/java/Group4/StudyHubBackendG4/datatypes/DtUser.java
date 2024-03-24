package Group4.StudyHubBackendG4.datatypes;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
public class DtUser {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String birthdate;
    private String username;

    public DtUser() {
    }

    public DtUser(Integer id, String name, String surname, String email, String birthdate, String username, String password, String jwtToken) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.birthdate = birthdate;
        this.username = username;
    }

}
