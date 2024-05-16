package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Carrera_Estudiante")
@Data
@NoArgsConstructor
public class CarreraEstudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCE;
    @ManyToOne
    @JoinColumn(name = "idEstudiante", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "idCarrera", nullable = false)
    private Carrera carrera;

}
