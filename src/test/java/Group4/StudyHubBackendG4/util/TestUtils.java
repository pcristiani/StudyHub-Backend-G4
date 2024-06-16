package Group4.StudyHubBackendG4.util;

import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.DocenteRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
import Group4.StudyHubBackendG4.services.AutenticacionService;
import Group4.StudyHubBackendG4.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private DocenteRepo docenteRepo;

    @Autowired
    private AutenticacionService autenticacionService;

    public Usuario createUsuario(Integer id, String nombre, String apellido, String email, String cedula, String rol, String password, boolean activo, boolean validado) {
        Usuario user = new Usuario();
        user.setIdUsuario(id);
        user.setNombre(nombre);
        user.setApellido(apellido);
        user.setEmail(email);
        user.setFechaNacimiento("19980903");
        user.setRol(rol);
        user.setPassword(PasswordService.getInstance().hashPassword(password));
        user.setCedula(cedula);
        user.setActivo(activo);
        user.setValidado(validado);
        return usuarioRepo.save(user);
    }

    public String authenticateUser(String cedula, String password) {
        return autenticacionService.authenticateUser(cedula, password);
    }

    public DtUsuario createDtUsuario(String nombre, String apellido, String email, String fechaNacimiento, String rol, String cedula, boolean activo, boolean validado) {
        DtUsuario dtUsuario = new DtUsuario();
        dtUsuario.setNombre(nombre);
        dtUsuario.setApellido(apellido);
        dtUsuario.setEmail(email);
        dtUsuario.setFechaNacimiento(fechaNacimiento);
        dtUsuario.setRol(rol);
        dtUsuario.setCedula(cedula);
        dtUsuario.setActivo(activo);
        dtUsuario.setValidado(validado);
        return dtUsuario;
    }

    public Docente createDocente(Integer codigoDocente, String nombre, Boolean activo) {
        Docente docente = new Docente();
        docente.setCodigoDocente(codigoDocente);
        docente.setNombre(nombre);
        docente.setActivo(activo);
        return docenteRepo.save(docente);
    }
}
