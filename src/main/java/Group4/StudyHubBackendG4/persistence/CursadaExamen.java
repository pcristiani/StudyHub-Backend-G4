package Group4.StudyHubBackendG4.persistence;

import Group4.StudyHubBackendG4.utils.enums.ResultadoExamen;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Cursada_Examen")
@Data
@NoArgsConstructor
public class CursadaExamen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCursadaExamen;

    @ManyToOne
    @JoinColumn(name = "idCursada", nullable = false)
    private Cursada cursada;

    @ManyToOne
    @JoinColumn(name = "idExamen", nullable = false)
    private Examen examen;

    private String cedulaEstudiante;

    @Enumerated(EnumType.STRING)
    private ResultadoExamen resultado;

}
