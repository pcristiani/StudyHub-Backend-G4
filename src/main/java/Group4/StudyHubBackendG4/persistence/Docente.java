package Group4.StudyHubBackendG4.persistence;

import Group4.StudyHubBackendG4.datatypes.DtNuevoDocente;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Docente")
@Data
@NoArgsConstructor
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDocente;
    private Integer codigoDocente;
    private String nombre;
    private Boolean activo;

    public Docente DocenteFromDtNuevoDocente(DtNuevoDocente dtNuevoDocente) {
        Docente docente = new Docente();
        docente.setCodigoDocente(dtNuevoDocente.getCodigoDocente());
        docente.setNombre(dtNuevoDocente.getNombre());
        docente.setActivo(true);
        return docente;
    }
}
