package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Carrera_Coordinador")
@Data
@NoArgsConstructor
public class CarreraCoordinador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCC;
    @ManyToOne
    @JoinColumn(name = "idCoordinador", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "idCarrera", nullable = false)
    private Carrera carrera;
}
