package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Usuario_TR")
@Data
@NoArgsConstructor
public class UsuarioTR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTR;
    @OneToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;
    private String jwt;
    private String tokenReseteoContrasenia;
}
