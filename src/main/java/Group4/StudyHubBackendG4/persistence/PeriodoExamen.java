package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Periodo_Examen")
@Data
@NoArgsConstructor
public class PeriodoExamen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPeriodoExamen;
    @ManyToOne
    @JoinColumn(name = "idCarrera", nullable = false)
    private Carrera carrera;
    private String nombre;
    private String descripcion;
    private String departamento;
    private Integer creditos;
    private Boolean activo;
}
