package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Docente_Examen")
@Data
@NoArgsConstructor
public class DocenteExamen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDE;
    @ManyToOne
    @JoinColumn(name = "idDocente", nullable = false)
    private Docente docente;
    @ManyToOne
    @JoinColumn(name = "idExamen", nullable = false)
    private Examen idExamen;
}
