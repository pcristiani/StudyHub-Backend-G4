package Group4.StudyHubBackendG4.persistence;

import Group4.StudyHubBackendG4.datatypes.DtNuevoUsuario;
import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.repositories.UsuarioTrRepo;
import Group4.StudyHubBackendG4.services.PasswordService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "Usuario")
@Data
@NoArgsConstructor
public class Usuario {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer idUsuario;
        private String nombre;
        private String apellido;
        private String email;
        private String fechaNacimiento;
        private String rol;
        private String password;
        private String cedula;
        private Boolean activo;
        private Boolean validado;

        public Usuario UserFromDtNewUser(DtNuevoUsuario dtNuevoUsuario) {
                Usuario usuario = new Usuario();
                usuario.setEmail(dtNuevoUsuario.getEmail());
                usuario.setNombre(dtNuevoUsuario.getNombre());
                usuario.setApellido(dtNuevoUsuario.getApellido());
                usuario.setFechaNacimiento(dtNuevoUsuario.getFechaNacimiento());
                usuario.setRol(dtNuevoUsuario.getRol());
                usuario.setCedula(dtNuevoUsuario.getCedula());
                usuario.setActivo(true);
                usuario.setValidado(!dtNuevoUsuario.getRol().equals("E"));
                usuario.setPassword(PasswordService.getInstance().hashPassword(dtNuevoUsuario.getPassword()));
                return usuario;
        }

        public DtUsuario userToDtUser() {
                DtUsuario dtUsuario = new DtUsuario();
                dtUsuario.setCedula(this.getCedula());
                dtUsuario.setIdUsuario(this.getIdUsuario());
                dtUsuario.setNombre(this.getNombre());
                dtUsuario.setApellido(this.getApellido());
                dtUsuario.setEmail(this.getEmail());
                dtUsuario.setFechaNacimiento(this.getFechaNacimiento());
                dtUsuario.setRol(this.getRol());
                dtUsuario.setActivo(this.getActivo());
                dtUsuario.setValidado(this.getValidado());
                return dtUsuario;
        }

        public void encryptPassword(){
                this.password = PasswordService.getInstance().hashPassword(this.password);
        }

        public void decryptPassword(){
                this.password = PasswordService.getInstance().hashPassword(this.password);
        }
}
