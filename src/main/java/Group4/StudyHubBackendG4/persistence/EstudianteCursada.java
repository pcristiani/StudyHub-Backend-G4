package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Estudiante_Cursada")
@Data
@NoArgsConstructor
public class EstudianteCursada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEC;
    @ManyToOne
    @JoinColumn(name = "idEstudiante", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "idCursada", nullable = false)
    private Cursada cursada;

}
