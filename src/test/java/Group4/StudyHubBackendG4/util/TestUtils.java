package Group4.StudyHubBackendG4.util;

import Group4.StudyHubBackendG4.datatypes.DtNuevoUsuario;
import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.*;
import Group4.StudyHubBackendG4.services.ActividadService;
import Group4.StudyHubBackendG4.services.AutenticacionService;
import Group4.StudyHubBackendG4.services.PasswordService;
import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TestUtils {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private DocenteRepo docenteRepo;

    @Autowired
    private AsignaturaRepo asignaturaRepo;

    @Autowired
    private CarreraRepo carreraRepo;

    @Autowired
    private DocenteAsignaturaRepo docenteAsignaturaRepo;

    @Autowired
    private EstudianteCursadaRepo estudianteCursadaRepo;

    @Autowired
    private CursadaRepo cursadaRepo;

    @Autowired
    private HorarioDiasRepo horarioDiasRepo;

    @Autowired
    private HorarioAsignaturaRepo horarioAsignaturaRepo;

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private AutenticacionService autenticacionService;

    public Usuario createUsuario(String nombre, String apellido, String email, String cedula, String rol, String password, boolean activo, boolean validado) {
        Usuario user = new Usuario();
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

    public DtNuevoUsuario createDtNuevoUsuario(String nombre, String apellido, String email, String fechaNacimiento,
                                               String cedula, String password, String rol) {
        DtNuevoUsuario nuevoUsuario = new DtNuevoUsuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setFechaNacimiento(fechaNacimiento);
        nuevoUsuario.setCedula(cedula);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setRol(rol);
        return nuevoUsuario;
    }

    public DocenteAsignatura createDocenteAsignatura(Docente docente, Asignatura asignatura) {
        DocenteAsignatura docenteAsignatura = new DocenteAsignatura();
        docenteAsignatura.setDocente(docente);
        docenteAsignatura.setAsignatura(asignatura);
        return docenteAsignaturaRepo.save(docenteAsignatura);
    }


    public void createActividad(Usuario user){
        Actividad actividad = new Actividad();
        actividad.setUsuario(user);
        actividad.setFechaHora(LocalDateTime.now());
        actividad.setAccion("Cierre de sesion");
        actividadService.save(actividad);
    }

}
