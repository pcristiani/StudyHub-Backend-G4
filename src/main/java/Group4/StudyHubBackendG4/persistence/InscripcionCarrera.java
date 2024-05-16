package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Inscripcion_Carrera")
@Data
@NoArgsConstructor
public class InscripcionCarrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInscripcion;
    @ManyToOne
    @JoinColumn(name = "idEstudiante", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "idCarrera", nullable = false)
    private Carrera carrera;
    private Boolean activa;
}
