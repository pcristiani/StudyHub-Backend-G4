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

    public UsuarioTR(Usuario usuario, String jwt, String tokenReseteoContrasenia) {
        this.usuario = usuario;
        this.jwt = jwt;
        this.tokenReseteoContrasenia = tokenReseteoContrasenia;
    }

    public Integer getIdTR() {
        return idTR;
    }

    public void setIdTR(Integer idTR) {
        this.idTR = idTR;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getTokenReseteoContrasenia() {
        return tokenReseteoContrasenia;
    }

    public void setTokenReseteoContrasenia(String tokenReseteoContrasenia) {
        this.tokenReseteoContrasenia = tokenReseteoContrasenia;
    }
}
