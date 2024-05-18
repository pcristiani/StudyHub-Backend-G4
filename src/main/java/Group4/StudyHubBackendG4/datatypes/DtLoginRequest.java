package Group4.StudyHubBackendG4.datatypes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DtLoginRequest {
    private String cedula;
    private String password;

    public DtLoginRequest() {
    }

    public DtLoginRequest(String cedula, String password) {
        this.cedula = cedula;
        this.password = password;
    }

}
