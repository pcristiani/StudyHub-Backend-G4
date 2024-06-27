package Group4.StudyHubBackendG4.util;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.AsignaturaRepo;
import Group4.StudyHubBackendG4.repositories.CarreraRepo;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.services.*;
import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private ExamenService examenService;

    @Autowired
    private CarreraRepo carreraRepo;

    @Autowired
    private AsignaturaRepo asignaturaRepo;

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;

    public Usuario userAdmin1;
    public Usuario userEstudiante1;
    public Usuario userEstudiante2;
    public Usuario userEstudiante3;
    public Usuario userEstudiante4;
    public Usuario userEstudianteNotValidated;
    public Usuario userCoordinador1;
    public Usuario userCoordinador2;
    public Docente docente1;
    public Docente docente2;
    public String token1;
    public String token2;
    public String token4;
    public Carrera carrera1;
    public Carrera carrera2;
    public Carrera carrera3;
    public Asignatura asignatura1;
    public Asignatura asignatura2;
    public Asignatura asignatura3;
    public Asignatura asignatura4;
    public Asignatura asignatura5;
    public DocenteAsignatura docenteAsignatura1;
    public DocenteAsignatura docenteAsignatura2;
    public DtHorarioDias dtHorarioDias1;
    public DtHorarioDias dtHorarioDias2;
    public DtHorarioDias dtHorarioDias3;
    public DtHorarioDias dtHorarioDiasInvalidTimeFormat1;
    public DtHorarioDias dtHorarioDiasInvalidTimeFormat2;
    public DtHorarioDias dtHorarioDiasInvalidTimeFormat3;
    public DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura1;
    public DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura2;
    public DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura3;
    public DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura4;
    public DtNuevoHorarioAsignatura dtNuevoHorarioAsignaturaInvalidDocente;
    public DtNuevoHorarioAsignatura dtNuevoHorarioAsignaturaInvalidTimeFormat1;
    public DtNuevoHorarioAsignatura dtNuevoHorarioAsignaturaInvalidTimeFormat2;
    public DtNuevoHorarioAsignatura dtNuevoHorarioAsignaturaInvalidTimeFormat3;
    public DtNuevaInscripcionAsignatura dtNuevaInscripcionAsignatura1;
    public DtNuevaInscripcionAsignatura dtNuevaInscripcionAsignatura2;
    public DtNuevaInscripcionAsignatura dtNuevaInscripcionAsignatura3;
    public DtNuevaInscripcionAsignatura dtNuevaInscripcionAsignatura4;
    public DtNuevaInscripcionAsignatura dtNuevaInscripcionAsignatura5;
    public DtNuevaInscripcionAsignatura dtNuevaInscripcionAsignatura6;
    public DtNuevaInscripcionAsignatura dtNuevaInscripcionAsignatura7;
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
    public DtNuevaCarrera dtNuevaCarrera4;
    public DtNuevaCarrera dtNuevaCarrera4Invalid;
    public DtInscripcionCarrera dtInscripcionCarrera1;
    public DtInscripcionCarrera dtInscripcionCarrera2;
    public DtInscripcionCarrera dtInscripcionCarrera3;
    public DtInscripcionCarrera dtInscripcionCarrera4;
    public DtInscripcionCarrera dtInscripcionCarrera5;
    public DtInscripcionCarrera dtInscripcionCarreraUserNotFound;
    public DtInscripcionCarrera dtInscripcionCarreraUserNotStudent;
    public DtInscripcionCarrera dtInscripcionCarreraCarreraNotFound;
    public DtPeriodoExamenRequest dtPeriodoExamenRequest1;
    public DtPeriodoExamenRequest dtPeriodoExamenRequest2;
    public DtPeriodoExamenRequest dtPeriodoExamenRequestInvalid;
    public DtCarrera dtCarrera1;
    public DtCarrera dtCarrera2;
    public DtCarrera dtCarreraConflict ;
    public DtNuevaAsignatura dtNuevaAsignatura1;
    public DtNuevaAsignatura dtNuevaAsignatura2;
    public DtNuevaAsignatura dtNuevaAsignatura3;
    public DtNuevaAsignatura dtNuevaAsignatura4;
    public DtNuevaAsignatura dtNuevaAsignatura5;
    public DtNuevaAsignatura dtNuevaAsignaturaConflict1;
    public DtNuevaAsignatura dtNuevaAsignaturaConflict2;
    public DtNuevaAsignatura dtNuevaAsignaturaConflict3;
    public DtNuevaAsignatura dtNuevaAsignaturaConflict4;
    public DtNuevaAsignatura dtNuevaAsignaturaConflict5;
    public DtNuevaAsignatura dtNuevaAsignaturaConflict6;
    public DtNuevaAsignatura dtNuevaAsignaturaConPrevias1;
    public DtNuevaAsignatura dtNuevaAsignaturaConPrevias2;
    public DtInscripcionExamen dtInscripcionExamen1;
    public DtInscripcionExamen dtInscripcionExamen2;
    public DtInscripcionExamen dtInscripcionExamen3;
    public DtInscripcionExamen dtInscripcionExamenInvalidEstudiante;
    public DtInscripcionExamen dtInscripcionExamenInvalidExamen;
    public DtInscripcionExamen dtInscripcionExamenInvalidCursadaWithNoExamen;
    public DtInscripcionExamen dtInscripcionExamenInvalidAlreadyApproved;
    public DtNuevoExamen dtNuevoExamen1;
    public DtNuevoExamen dtNuevoExamen2;
    public DtNuevoExamen dtNuevoExamenaAlreadyExists;
    public DtNuevoExamen dtNuevoExamenInvalidAsignatura;
    public DtNuevoExamen dtNuevoExamenInvalidPeriodo;
    public DtNuevoExamen dtNuevoExamenInvalidDocente;
    public DtNuevoExamen dtNuevoExamenInvalidInvalidHorario;


    public void setUp() throws MessagingException, IOException {
        setUpUsers();
        setUpTokens();
        setUpDocentes();
        setUpDtNuevaCarreras();
        setUpCarreras();
        setUpDtCarreras();
        setUpDtAsignaturas();
        setUpAsignaturas();
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
        setUpDtNuevoExamenes();
        setUpDtInscripcionExamenes();
    }

    public void setUpUsers() {
        userAdmin1 = testUtils.createUsuario("John", "Doe", "john.doe@example.com", "12345678", "A", "123", true, true);
        userEstudiante1 = testUtils.createUsuario("Jane", "Smith", "jane.smith@example.com", "87654321", "E", "123", true, true);
        userEstudiante2 = testUtils.createUsuario("Bran", "Done", "bran.done@example.com", "123654786", "E", "123", true, true);
        userEstudiante3 = testUtils.createUsuario("Betty", "Brown", "betty.brown@example.com", "789654123", "E", "123", true, true);
        userEstudiante4 = testUtils.createUsuario("Marcos", "Baldez", "marcos.baldez@example.com", "6543814", "E", "123", true, true);
        userEstudianteNotValidated = testUtils.createUsuario("Samba", "Rodriguez", "samba.rodriguez@example.com", "65465465", "E", "123", false, false);
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
        this.dtNuevaCarrera1 = new DtNuevaCarrera("Ingeniería Informática", "Descripción de Ingeniería Informática ", "Requisitos de Ingeniería Informática", 5, userCoordinador1.getIdUsuario());
        this.dtNuevaCarrera2 = new DtNuevaCarrera("Medicina", "Descripción de Medicina", "Requisitos de Medicina", 6, userCoordinador1.getIdUsuario());
        this.dtNuevaCarrera3 = new DtNuevaCarrera("Traductorado", "Descripción de Traductorado", "Requisitos de Traductorado", 2, userCoordinador1.getIdUsuario());
        this.dtNuevaCarrera4 = new DtNuevaCarrera("Economia", "Descripción de Economia", "Requisitos de Economia", 2, userCoordinador1.getIdUsuario());
        this.dtNuevaCarrera4Invalid = new DtNuevaCarrera("Ingeniería Informática", "Descripción de Ingenieria", "Requisitos de Ingeniería Civil", 5, userCoordinador1.getIdUsuario());
    }

    public void setUpCarreras() {
        carreraService.nuevaCarrera(this.dtNuevaCarrera1);
        carreraService.nuevaCarrera(this.dtNuevaCarrera2);
        carreraService.nuevaCarrera(this.dtNuevaCarrera3);
        carrera1 = carreraRepo.findById(1).get();
        carrera2 = carreraRepo.findById(2).get();
        carrera3 = carreraRepo.findById(3).get();
    }

    public void setUpAsignaturas() {
        asignatura1 = asignaturaRepo.findById(1).get();
        asignatura2 = asignaturaRepo.findById(2).get();
        asignatura3 = asignaturaRepo.findById(3).get();
        asignatura4 = asignaturaRepo.findById(4).get();
        asignatura5 = asignaturaRepo.findById(5).get();
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
        dtLoginRequest2 = new DtLoginRequest(userEstudianteNotValidated.getCedula(), "123");
        dtLoginRequest3 = new DtLoginRequest(userAdmin1.getCedula(), "wrongPass");
    }

    public void setUpDtInscripcionCarreras() throws MessagingException, IOException {
        dtInscripcionCarrera1 = new DtInscripcionCarrera(carrera1.getIdCarrera(), userEstudiante2.getIdUsuario(), true);
        dtInscripcionCarrera2 = new DtInscripcionCarrera(carrera2.getIdCarrera(), userEstudiante3.getIdUsuario(), true);
        dtInscripcionCarrera3 = new DtInscripcionCarrera(carrera2.getIdCarrera(), userEstudiante2.getIdUsuario(), false);
        dtInscripcionCarrera4 = new DtInscripcionCarrera(carrera1.getIdCarrera(), userEstudiante1.getIdUsuario(), true);
        dtInscripcionCarrera5 = new DtInscripcionCarrera(carrera1.getIdCarrera(), userEstudiante4.getIdUsuario(), true);
        dtInscripcionCarreraUserNotFound = new DtInscripcionCarrera(1, 60, false);
        dtInscripcionCarreraUserNotStudent = new DtInscripcionCarrera(1, userCoordinador1.getIdUsuario(), false);
        dtInscripcionCarreraCarreraNotFound = new DtInscripcionCarrera(60, userEstudiante3.getIdUsuario(), false);
        carreraService.inscripcionCarrera(dtInscripcionCarrera2);
        carreraService.inscripcionCarrera(dtInscripcionCarrera3);
        carreraService.inscripcionCarrera(dtInscripcionCarrera4);
        carreraService.inscripcionCarrera(dtInscripcionCarrera5);
        carreraService.acceptEstudianteCarrera(dtInscripcionCarrera2);
        carreraService.acceptEstudianteCarrera(dtInscripcionCarrera4);
        carreraService.acceptEstudianteCarrera(dtInscripcionCarrera5);
    }

    public void setUpDtPeriodoExamenRequests() {
        dtPeriodoExamenRequest1 = new DtPeriodoExamenRequest("Exámenes de Julio 2024", new DtFecha(2024, 7, 1), new DtFecha(2024, 7, 15));
        dtPeriodoExamenRequest2 = new DtPeriodoExamenRequest("Exámenes de Diciembre 2024", new DtFecha(2024, 12, 1), new DtFecha(2024, 12, 15));
        dtPeriodoExamenRequestInvalid = new DtPeriodoExamenRequest("Exámenes de Julio", new DtFecha(1, 60, 50), new DtFecha(2024, 7, 15));
        carreraService.altaPeriodoDeExamen(1, dtPeriodoExamenRequest2);
    }

    public void setUpDtCarreras() {
        dtCarrera1 = new DtCarrera(carrera1.getIdCarrera(), "Ingeniería Civil Modificada", "Descripción modificada", "Nuevos requisitos", 6, true);
        dtCarrera2 = new DtCarrera(carrera2.getIdCarrera(), "Medicina Modificada", "Descripción modificada", "Nuevos requisitos", 7, true);
        dtCarreraConflict = new DtCarrera(carrera1.getIdCarrera(), "Ingeniería Informática", "Descripción modificada", "Nuevos requisitos", 6, true);
    }

    public void setUpDtAsignaturas() {
        dtNuevaAsignatura1 = new DtNuevaAsignatura(carrera1.getIdCarrera(), List.of(docente1.getIdDocente()), "Sistemas Operativos", 6, "Descripción de Sistemas Operativos", "Informatica", true, true, List.of());
        dtNuevaAsignatura2 = new DtNuevaAsignatura(carrera1.getIdCarrera(), List.of(docente2.getIdDocente()), "Principios de Programación", 12, "Principios de Programación", "Informatica", true, true, List.of());
        dtNuevaAsignatura3 = new DtNuevaAsignatura(carrera1.getIdCarrera(), List.of(docente2.getIdDocente()), "Ingenieria de Software", 12, "Ingenieria de Software", "Informatica", true, true, List.of());
        dtNuevaAsignatura4 = new DtNuevaAsignatura(carrera2.getIdCarrera(), List.of(docente2.getIdDocente()), "Bioestadística", 12, "Bioestadística", "Medicina", true, true, List.of());
        dtNuevaAsignatura5 = new DtNuevaAsignatura(carrera1.getIdCarrera(), List.of(docente2.getIdDocente()), "Matematica Discreta", 12, "Matematica Discreta", "Informatica", true, true, List.of());
        dtNuevaAsignaturaConPrevias1 = new DtNuevaAsignatura(carrera1.getIdCarrera(), List.of(docente2.getIdDocente()), "Programación de Aplicaciones", 12, "Descripción de Programación de Aplicaciones", "Informatica", true, true, List.of(1));
        dtNuevaAsignaturaConPrevias2 = new DtNuevaAsignatura(carrera1.getIdCarrera(), List.of(docente2.getIdDocente()), "Programación 3", 12, "Descripción de Programación 3", "Informatica", true, true, List.of(2));
        dtNuevaAsignaturaConflict1 = new DtNuevaAsignatura(carrera1.getIdCarrera(), List.of(docente2.getIdDocente()), "Principios de Programación", 12, "Descripción de Principios de Programación", "Informatica", true, true, List.of());
        dtNuevaAsignaturaConflict2 = new DtNuevaAsignatura(carrera1.getIdCarrera(), List.of(), "Principios de Programación", 12, "Descripción de Principios de Programación", "Informatica", true, true, List.of());
        dtNuevaAsignaturaConflict3 = new DtNuevaAsignatura(carrera1.getIdCarrera(), List.of(60), "Principios de Programación", 12, "Descripción de Principios de Programación", "Informatica", true, true, List.of());
        dtNuevaAsignaturaConflict4 = new DtNuevaAsignatura(60, List.of(docente2.getIdDocente()), "Principios de Programación", 12, "Descripción de Principios de Programación", "Informatica", true, true, List.of());
        dtNuevaAsignaturaConflict5 = new DtNuevaAsignatura(carrera1.getIdCarrera(), List.of(docente2.getIdDocente()), "Redes de Computadoras", 12, "Descripción de Redes de Computadoras", "Informatica", true, true, List.of(60));
        dtNuevaAsignaturaConflict6 = new DtNuevaAsignatura(carrera2.getIdCarrera(), List.of(docente2.getIdDocente()), "Bioquimica 1", 12, "Descripción de Bioquimica 1", "Medicina", true, true, List.of(3));
        asignaturaService.altaAsignatura(dtNuevaAsignatura2);
        asignaturaService.altaAsignatura(dtNuevaAsignaturaConPrevias1);
        asignaturaService.altaAsignatura(dtNuevaAsignatura3);
        asignaturaService.altaAsignatura(dtNuevaAsignatura4);
        asignaturaService.altaAsignatura(dtNuevaAsignatura5);
        asignaturaService.altaAsignatura(dtNuevaAsignaturaConPrevias2);
    }

    public void setUpHorarios() {
        dtHorarioDias1 = new DtHorarioDias(DiaSemana.LUNES, "10:30","12:30");
        dtHorarioDias2 = new DtHorarioDias(DiaSemana.LUNES, "14:00","20:00");
        dtHorarioDias3 = new DtHorarioDias(DiaSemana.MARTES, "14:00","20:00");
        dtHorarioDiasInvalidTimeFormat1 = new DtHorarioDias(DiaSemana.LUNES, "1300","20:00");
        dtHorarioDiasInvalidTimeFormat2 = new DtHorarioDias(DiaSemana.LUNES, "20:00","15:00");
        dtHorarioDiasInvalidTimeFormat3 = new DtHorarioDias(DiaSemana.LUNES, "11:00","15:00");
        dtNuevoHorarioAsignatura1 = new DtNuevoHorarioAsignatura(docente1.getIdDocente(), 2022, List.of(dtHorarioDias1));
        dtNuevoHorarioAsignatura2 = new DtNuevoHorarioAsignatura(docente2.getIdDocente(), 2022, List.of(dtHorarioDias2));
        dtNuevoHorarioAsignatura3 = new DtNuevoHorarioAsignatura(docente2.getIdDocente(), 2022, List.of(dtHorarioDias3));
        dtNuevoHorarioAsignatura4 = new DtNuevoHorarioAsignatura(docente2.getIdDocente(), 2023, List.of(dtHorarioDias3));
        dtNuevoHorarioAsignaturaInvalidDocente = new DtNuevoHorarioAsignatura(60, 2022, List.of(dtHorarioDias2));
        dtNuevoHorarioAsignaturaInvalidTimeFormat1 = new DtNuevoHorarioAsignatura(docente1.getIdDocente(), 2022, List.of(dtHorarioDiasInvalidTimeFormat1));
        dtNuevoHorarioAsignaturaInvalidTimeFormat2 = new DtNuevoHorarioAsignatura(docente1.getIdDocente(), 2022, List.of(dtHorarioDiasInvalidTimeFormat2));
        dtNuevoHorarioAsignaturaInvalidTimeFormat3 = new DtNuevoHorarioAsignatura(docente1.getIdDocente(), 2022, List.of(dtHorarioDiasInvalidTimeFormat3));
        asignaturaService.registroHorarios(asignatura1.getIdAsignatura(), dtNuevoHorarioAsignatura1);
        asignaturaService.registroHorarios(asignatura4.getIdAsignatura(), dtNuevoHorarioAsignatura2);
        asignaturaService.registroHorarios(asignatura5.getIdAsignatura(), dtNuevoHorarioAsignatura4);
    }

    public void setUpInscripciones() throws MessagingException, IOException {
        dtNuevaInscripcionAsignatura1 = new DtNuevaInscripcionAsignatura(userEstudiante1.getIdUsuario(), asignatura1.getIdAsignatura(), 1);
        dtNuevaInscripcionAsignatura2 = new DtNuevaInscripcionAsignatura(userEstudiante3.getIdUsuario(), asignatura1.getIdAsignatura(), 1);
        dtNuevaInscripcionAsignatura3 = new DtNuevaInscripcionAsignatura(userEstudiante3.getIdUsuario(), asignatura4.getIdAsignatura(), 2);
        dtNuevaInscripcionAsignatura4 = new DtNuevaInscripcionAsignatura(userEstudiante1.getIdUsuario(), asignatura5.getIdAsignatura(), 2);
        dtNuevaInscripcionAsignatura5 = new DtNuevaInscripcionAsignatura(userEstudiante1.getIdUsuario(), asignatura2.getIdAsignatura(), 2);
        dtNuevaInscripcionAsignatura6 = new DtNuevaInscripcionAsignatura(userEstudiante3.getIdUsuario(), asignatura2.getIdAsignatura(), 2);
        dtNuevaInscripcionAsignatura7 = new DtNuevaInscripcionAsignatura(userEstudiante4.getIdUsuario(), asignatura2.getIdAsignatura(), 2);
        asignaturaService.inscripcionAsignatura(dtNuevaInscripcionAsignatura1);
        asignaturaService.inscripcionAsignatura(dtNuevaInscripcionAsignatura4);
        asignaturaService.inscripcionAsignatura(dtNuevaInscripcionAsignatura5);
        asignaturaService.inscripcionAsignatura(dtNuevaInscripcionAsignatura6);
        asignaturaService.inscripcionAsignatura(dtNuevaInscripcionAsignatura7);
        asignaturaService.modificarResultadoCursada(3,10);
        asignaturaService.modificarResultadoCursada(1,4);
        asignaturaService.modificarResultadoCursada(4,4);
        asignaturaService.modificarResultadoCursada(5,4);
    }

    public void setUpPasswordReset() {
        usuarioService.generatePasswordResetToken(userAdmin1);
        dtNewPassword = new DtNewPassword();
        dtNewPassword.setToken(String.valueOf(passwordResetTokenRepo.findAll().get(0)));
        dtNewPassword.setNewPassword("newPass123");
    }

    private void setUpDtNuevoExamenes() {
        dtNuevoExamen1 = new DtNuevoExamen(asignatura1.getIdAsignatura(), 1, List.of(docente1.getIdDocente()), LocalDateTime.of(2024, 12, 5, 10, 0));
        dtNuevoExamen2 = new DtNuevoExamen(asignatura2.getIdAsignatura(), 1, List.of(docente1.getIdDocente()), LocalDateTime.of(2024, 12, 5, 10, 0));
        dtNuevoExamenaAlreadyExists = new DtNuevoExamen(asignatura2.getIdAsignatura(), 1, List.of(docente1.getIdDocente()), LocalDateTime.of(2024, 12, 5, 10, 0));
        dtNuevoExamenInvalidAsignatura = new DtNuevoExamen(60, 1, List.of(docente1.getIdDocente()), LocalDateTime.of(2024, 12, 5, 10, 0));
        dtNuevoExamenInvalidPeriodo = new DtNuevoExamen(asignatura1.getIdAsignatura(), 60, List.of(docente1.getIdDocente()), LocalDateTime.of(2024, 12, 5, 10, 0));
        dtNuevoExamenInvalidDocente = new DtNuevoExamen(asignatura1.getIdAsignatura(), 1, List.of(60), LocalDateTime.of(2024, 12, 5, 10, 0));
        dtNuevoExamenInvalidInvalidHorario = new DtNuevoExamen(asignatura1.getIdAsignatura(), 1, List.of(docente1.getIdDocente()), LocalDateTime.of(2024, 11, 5, 10, 0));
        examenService.registroAsignaturaAPeriodo(dtNuevoExamen2);
    }

    private void setUpDtInscripcionExamenes() {
        dtInscripcionExamen1 = new DtInscripcionExamen(4,1);
        dtInscripcionExamen2 = new DtInscripcionExamen(3,1);
        dtInscripcionExamen3 = new DtInscripcionExamen(userEstudiante4.getIdUsuario(),1);
        dtInscripcionExamenInvalidEstudiante = new DtInscripcionExamen(60,1);
        dtInscripcionExamenInvalidExamen = new DtInscripcionExamen(2,60);
        dtInscripcionExamenInvalidCursadaWithNoExamen = new DtInscripcionExamen(1,1);
        dtInscripcionExamenInvalidAlreadyApproved = new DtInscripcionExamen(userEstudiante1.getIdUsuario(),1);
        examenService.inscripcionExamen(dtInscripcionExamen3);
    }

}
