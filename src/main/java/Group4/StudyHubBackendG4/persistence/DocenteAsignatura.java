package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Docente_Asignatura")
@Data
@NoArgsConstructor
public class DocenteAsignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDA;
    @ManyToOne
    @JoinColumn(name = "idDocente", nullable = false)
    private Docente docente;
    @ManyToOne
    @JoinColumn(name = "idAsignatura", nullable = false)
    private Asignatura asignatura;
}
