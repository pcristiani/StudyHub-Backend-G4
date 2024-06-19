package Group4.StudyHubBackendG4.util;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.CarreraRepo;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.services.AsignaturaService;
import Group4.StudyHubBackendG4.services.AutenticacionService;
import Group4.StudyHubBackendG4.services.CarreraService;
import Group4.StudyHubBackendG4.services.UsuarioService;
import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SetUpHelper {

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private AutenticacionService autenticacionService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AsignaturaService asignaturaService;

    @Autowired
    private CarreraService carreraService;

    @Autowired
    private CarreraRepo carreraRepo;

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;

    public Usuario userAdmin1;
    public Usuario userEstudiante1;
    public Usuario userEstudiante2;
    public Usuario userEstudiante3;
    public Usuario useruserEstudianteNotValidated;
    public Usuario userCoordinador1;
    public Usuario userCoordinador2;
    public Docente docente1;
    public Docente docente2;
    public String token1;
    public String token2;
    public String token4;
    public Carrera carrera1;
    public Carrera carrera2;
    public Asignatura asignatura1;
    public Asignatura asignatura2;
    public DocenteAsignatura docenteAsignatura1;
    public DocenteAsignatura docenteAsignatura2;
    public DtHorarioDias dtHorarioDias;
    public DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura;
    public DtNuevaInscripcionAsignatura dtNuevaInscripcionAsignatura;
    public DtUsuario dtUserModifiedNombreApellidoEmail;
    public DtUsuario dtUserModifiedCedula;
    public DtDocente dtDocente1;
    public DtDocente dtDocente2;
    public DtNuevoUsuario dtNuevoUsuario;
    public DtNuevoDocente dtNuevoDocente1;
    public DtNuevoDocente dtNuevoDocente2;
    public DtNewPassword dtNewPassword;
    public DtLoginRequest dtLoginRequest1;
    public DtLoginRequest dtLoginRequest2;
    public DtLoginRequest dtLoginRequest3;
    public DtPerfil dtPerfil1;
    public DtPerfil dtPerfil2;
    public DtNuevaCarrera dtNuevaCarrera1;
    public DtNuevaCarrera dtNuevaCarrera2;
    public DtNuevaCarrera dtNuevaCarrera3;
    public DtNuevaCarrera dtNuevaCarrera4Invalid;
    public DtInscripcionCarrera dtInscripcionCarrera1;
    public DtInscripcionCarrera dtInscripcionCarrera2;
    public DtInscripcionCarrera dtInscripcionCarrera3;
    public DtInscripcionCarrera dtInscripcionCarreraUserNotFound;
    public DtInscripcionCarrera dtInscripcionCarreraUserNotStudent;
    public DtInscripcionCarrera dtInscripcionCarreraCarreraNotFound;
    public DtPeriodoExamenRequest dtPeriodoExamenRequest;
    public DtPeriodoExamenRequest dtPeriodoExamenRequestInvalid;


    public void setUp() {
        setUpUsers();
        setUpTokens();
        setUpDocentes();
        setUpDtNuevaCarreras();
        setUpCarrerasAndAsignaturas();
        setUpDocenteAsignaturas();
        setUpActividades();
        setUpDtUsuarios();
        setUpDtDocentes();
        setUpDtPerfiles();
        setUpDtLoginRequests();
        setUpDtInscripcionCarreras();
        setUpHorarios();
        setUpInscripciones();
        setUpDtPeriodoExamenRequests();
        setUpPasswordReset();
    }

    public void setUpUsers() {
        userAdmin1 = testUtils.createUsuario("John", "Doe", "john.doe@example.com", "12345678", "A", "123", true, true);
        userEstudiante1 = testUtils.createUsuario("Jane", "Smith", "jane.smith@example.com", "87654321", "E", "123", true, true);
        userEstudiante2 = testUtils.createUsuario("Bran", "Done", "bran.done@example.com", "123654786", "E", "123", true, true);
        userEstudiante3 = testUtils.createUsuario("Betty", "Brown", "betty.brown@example.com", "789654123", "E", "123", true, true);
        useruserEstudianteNotValidated = testUtils.createUsuario("Samba", "Rodriguez", "samba.rodriguez@example.com", "65465465", "E", "123", false, false);
        userCoordinador1 = testUtils.createUsuario("Paul", "Atreides", "paul.atreides@example.com", "987321654", "C", "123", true, true);
        userCoordinador2 = testUtils.createUsuario("Usul", "Muadhib", "usul.muadhib@example.com", "123987452", "C", "123", true, true);
    }

    public void setUpTokens() {
        token1 = testUtils.authenticateUser(userAdmin1.getCedula(), "123");
        token2 = testUtils.authenticateUser(userEstudiante1.getCedula(), "123");
        token4 = testUtils.authenticateUser(userEstudiante3.getCedula(), "123");
        autenticacionService.logoutUser(token2);
    }

    public void setUpDocentes() {
        docente1 = testUtils.createDocente(1001, "Alan Grant", true);
        docente2 = testUtils.createDocente(1002, "Ellen Sattler", true);
    }

    public void setUpDtNuevaCarreras() {
        this.dtNuevaCarrera1 = new DtNuevaCarrera("Ingeniería Informática", "Descripción de Ingeniería Informática ", "Requisitos de Ingeniería Civil", 5, userCoordinador1.getIdUsuario());
        this.dtNuevaCarrera2 = new DtNuevaCarrera("Medicina", "Descripción de Medicina", "Requisitos de Medicina", 6, userCoordinador1.getIdUsuario());
        this.dtNuevaCarrera3 = new DtNuevaCarrera("Traductorado", "Descripción de Traductorado", "Requisitos de Traductorado", 2, userCoordinador1.getIdUsuario());
        this.dtNuevaCarrera4Invalid = new DtNuevaCarrera("Ingeniería Informática", "Descripción de Ingenieria", "Requisitos de Ingeniería Civil", 5, userCoordinador1.getIdUsuario());
    }

    public void setUpCarrerasAndAsignaturas() {
        carreraService.nuevaCarrera(this.dtNuevaCarrera1);
        carreraService.nuevaCarrera(this.dtNuevaCarrera2);
        carrera1 = carreraRepo.findById(1).get();
        carrera2 = carreraRepo.findById(2).get();
        asignatura1 = testUtils.createAsignatura(carrera1, "Programacion 1", 12, "Descripcion", "Informatica", false, true);
        asignatura2 = testUtils.createAsignatura(carrera1, "Sistemas Operativos", 12, "Descripcion", "Informatica", false, true);
    }

    public void setUpDocenteAsignaturas() {
        docenteAsignatura1 = testUtils.createDocenteAsignatura(docente1, asignatura1);
        docenteAsignatura2 = testUtils.createDocenteAsignatura(docente2, asignatura2);
    }

    public void setUpActividades() {
        for (int i = 0; i < 10; i++) {
            testUtils.createActividad(userEstudiante3);
        }
    }

    public void setUpDtUsuarios() {
        dtUserModifiedNombreApellidoEmail = testUtils.createDtUsuario("Mike", "Tyson", "mike.tyson@example.com", "19980903", "A", "12345678", true, true);
        dtUserModifiedCedula = testUtils.createDtUsuario("Mike", "Tyson", "mike.tyson@example.com", "19980903", "A", "654654321", true, true);
        dtNuevoUsuario = testUtils.createDtNuevoUsuario("Michael", "Jordan", "michael.jordan@example.com", "1985-02-17", "23456789", "123", "ROLE_A");
    }

    public void setUpDtDocentes() {
        dtNuevoDocente1 = new DtNuevoDocente(1003, "Jack Johnson");
        dtNuevoDocente2 = new DtNuevoDocente(1001, "Jack Johnson");
        dtDocente1 = new DtDocente(null, 1006, "Marcelo Tinelli", true);
        dtDocente2 = new DtDocente(null, 1001, "Marcelo Tinelli", true);
    }

    public void setUpDtPerfiles() {
        dtPerfil1 = new DtPerfil("Andrew", "Dornen", "andrew.dornen@example.com", "19960606");
        dtPerfil2 = new DtPerfil("Andrew", "Dornen", "john.doe@example.com", "19960606");
    }

    public void setUpDtLoginRequests(){
        dtLoginRequest1 = new DtLoginRequest(userAdmin1.getCedula(), "123");
        dtLoginRequest2 = new DtLoginRequest(useruserEstudianteNotValidated.getCedula(), "123");
        dtLoginRequest3 = new DtLoginRequest(userAdmin1.getCedula(), "wrongPass");
    }

    public void setUpDtInscripcionCarreras() {
        dtInscripcionCarrera1 = new DtInscripcionCarrera(carrera1.getIdCarrera(), userEstudiante1.getIdUsuario(), true);
        dtInscripcionCarrera2 = new DtInscripcionCarrera(carrera2.getIdCarrera(), userEstudiante3.getIdUsuario(), false);
        dtInscripcionCarrera3 = new DtInscripcionCarrera(carrera2.getIdCarrera(), userEstudiante2.getIdUsuario(), false);
        dtInscripcionCarreraUserNotFound = new DtInscripcionCarrera(1, 60, false);
        dtInscripcionCarreraUserNotStudent = new DtInscripcionCarrera(1, userCoordinador1.getIdUsuario(), false);
        dtInscripcionCarreraCarreraNotFound = new DtInscripcionCarrera(60, userEstudiante3.getIdUsuario(), false);
        carreraService.inscripcionCarrera(dtInscripcionCarrera2);
        carreraService.inscripcionCarrera(dtInscripcionCarrera3);
        carreraService.validateInscripcion(dtInscripcionCarrera2);
    }

    public void setUpDtPeriodoExamenRequests() {
        dtPeriodoExamenRequest = new DtPeriodoExamenRequest("Exámenes de Julio", new DtFecha(2024, 7, 1), new DtFecha(2024, 7, 15));
        dtPeriodoExamenRequestInvalid = new DtPeriodoExamenRequest("Exámenes de Julio", new DtFecha(1, 60, 50), new DtFecha(2024, 7, 15));
    }

    public void setUpHorarios() {
        dtHorarioDias = new DtHorarioDias(DiaSemana.LUNES, "10:30","12:30");
        dtNuevoHorarioAsignatura = new DtNuevoHorarioAsignatura(docente1.getIdDocente(), 2022, List.of(dtHorarioDias));
        asignaturaService.registroHorarios(asignatura1.getIdAsignatura(), dtNuevoHorarioAsignatura);
    }

    public void setUpInscripciones() {
        dtNuevaInscripcionAsignatura = new DtNuevaInscripcionAsignatura(userEstudiante1.getIdUsuario(), asignatura1.getIdAsignatura(), 1);
        asignaturaService.inscripcionAsignatura(dtNuevaInscripcionAsignatura);
    }

    public void setUpPasswordReset() {
        usuarioService.generatePasswordResetToken(userAdmin1);
        dtNewPassword = new DtNewPassword();
        dtNewPassword.setToken(String.valueOf(passwordResetTokenRepo.findAll().get(0)));
        dtNewPassword.setNewPassword("newPass123");
    }

}
