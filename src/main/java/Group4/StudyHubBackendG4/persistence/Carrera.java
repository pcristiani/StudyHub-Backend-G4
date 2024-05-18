package Group4.StudyHubBackendG4.persistence;

import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.datatypes.DtNuevaCarrera;
import Group4.StudyHubBackendG4.datatypes.DtNuevoUsuario;
import Group4.StudyHubBackendG4.services.PasswordService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Carrera")
@Data
@NoArgsConstructor
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrera;
    private String nombre;
    private String descripcion;
    private Boolean activa;

    public Carrera CarreraFromDtNuevaCarrera(DtNuevaCarrera dtNuevaCarrera) {
        Carrera carrera = new Carrera();
        carrera.setNombre(dtNuevaCarrera.getNombre());
        carrera.setDescripcion(dtNuevaCarrera.getDescripcion());
        carrera.setActiva(true);
        return carrera;
    }
}
