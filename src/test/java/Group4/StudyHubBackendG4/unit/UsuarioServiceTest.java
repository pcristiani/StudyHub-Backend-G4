package Group4.StudyHubBackendG4.unit;

import Group4.StudyHubBackendG4.datatypes.DtDocente;
import Group4.StudyHubBackendG4.datatypes.DtNuevoUsuario;
import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.persistence.UsuarioTR;
import Group4.StudyHubBackendG4.repositories.DocenteAsignaturaRepo;
import Group4.StudyHubBackendG4.repositories.DocenteRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioTrRepo;
import Group4.StudyHubBackendG4.services.UsuarioService;
import Group4.StudyHubBackendG4.utils.JwtUtil;
import Group4.StudyHubBackendG4.utils.converters.DocenteConverter;
import Group4.StudyHubBackendG4.utils.converters.UsuarioConverter;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private DocenteRepo docenteRepo;

    @Mock
    private DocenteAsignaturaRepo docenteAsignaturaRepo;

    @Mock
    private UsuarioTrRepo usuarioTrRepo;

    @Mock
    private UsuarioConverter usuarioConverter;

    @Mock
    private DocenteConverter docenteConverter;


    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario user1;
    private Usuario user2;
    private DtUsuario dtUser1;
    private DtUsuario dtUser2;
    private DtUsuario dtUserForModifyWithExistingCedula;
    private DtUsuario dtUserForModifyWithExistingEmail;
    private DtNuevoUsuario nuevoUsuario;
    private Docente docente1;
    private Docente docente2;
    private DtDocente dtDocente1;
    private DtDocente dtDocente2;

    private UsuarioTR usuarioTR1;

    @BeforeEach
    public void setUp() {
        user1 = new Usuario();
        user1.setIdUsuario(1);
        user1.setNombre("John");
        user1.setApellido("Doe");
        user1.setCedula("12345678");
        user1.setEmail("johndoe@example.com");

        user2 = new Usuario();
        user2.setIdUsuario(2);
        user2.setNombre("Jane");
        user2.setApellido("Smith");
        user2.setCedula("87654321");
        user2.setEmail("janesmith@example.com");

        dtUser1 = new DtUsuario();
        dtUser1.setIdUsuario(1);
        dtUser1.setNombre("John");
        dtUser1.setApellido("Doe");
        dtUser1.setCedula("12345678");
        dtUser1.setEmail("johndoe@example.com");

        dtUser2 = new DtUsuario();
        dtUser2.setIdUsuario(2);
        dtUser2.setNombre("Jane");
        dtUser2.setApellido("Smith");
        dtUser2.setCedula("87654321");
        dtUser2.setEmail("janesmith@example.com");

        dtUserForModifyWithExistingCedula = new DtUsuario();
        dtUserForModifyWithExistingCedula.setIdUsuario(1);
        dtUserForModifyWithExistingCedula.setNombre("John");
        dtUserForModifyWithExistingCedula.setApellido("Doe");
        dtUserForModifyWithExistingCedula.setCedula("87654321");
        dtUserForModifyWithExistingCedula.setEmail("johndoe@example.com");

        dtUserForModifyWithExistingEmail = new DtUsuario();
        dtUserForModifyWithExistingEmail.setIdUsuario(1);
        dtUserForModifyWithExistingEmail.setNombre("John");
        dtUserForModifyWithExistingEmail.setApellido("Doe");
        dtUserForModifyWithExistingEmail.setCedula("12345678");
        dtUserForModifyWithExistingEmail.setEmail("janesmith@example.com");

        nuevoUsuario = new DtNuevoUsuario();
        nuevoUsuario.setCedula("12345678");
        nuevoUsuario.setNombre("John");
        nuevoUsuario.setApellido("Doe");
        nuevoUsuario.setEmail("john.doe@example.com");
        nuevoUsuario.setRol("E");

        docente1 = new Docente();
        docente1.setIdDocente(1);
        docente1.setCodigoDocente(123);
        docente1.setNombre("Carlos Fabian");
        docente1.setActivo(true);

        docente2 = new Docente();
        docente2.setIdDocente(2);
        docente2.setCodigoDocente(234);
        docente2.setNombre("Pedro Pablo");
        docente2.setActivo(true);

        dtDocente1 = new DtDocente();
        dtDocente1.setIdDocente(1);
        dtDocente1.setCodigoDocente(123);
        dtDocente1.setNombre("Carlos Fabian");
        dtDocente1.setActivo(true);

        dtDocente2 = new DtDocente();
        dtDocente2.setIdDocente(2);
        dtDocente2.setCodigoDocente(234);
        dtDocente2.setNombre("Pedro Pablo");
        dtDocente2.setActivo(true);

        usuarioTR1 = new UsuarioTR();
        usuarioTR1.setIdTR(1);
        usuarioTR1.setUsuario(user1);
        usuarioTR1.setJwt("fake-JWT");
        usuarioTR1.setMobileToken("fake-mobile-token");

    }

    @Test
    public void getUsuariosReturnsOk() {
        // When
        when(usuarioRepo.findAll()).thenReturn(List.of(user1, user2));
        when(usuarioConverter.convertToDto(user1)).thenReturn(dtUser1);
        when(usuarioConverter.convertToDto(user2)).thenReturn(dtUser2);

        // Act
        List<DtUsuario> result = usuarioService.getUsuarios();

        // Assert
        assertEquals(2, result.size());
        assertEquals(dtUser1, result.get(0));
        assertEquals(dtUser2, result.get(1));
    }

    @Test
    public void getDocentesReturnsOk() {
        // When
        when(docenteRepo.findAll()).thenReturn(List.of(docente1, docente2));
        when(docenteConverter.convertToDto(docente1)).thenReturn(dtDocente1);
        when(docenteConverter.convertToDto(docente2)).thenReturn(dtDocente2);

        // Act
        List<DtDocente> result = usuarioService.getDocentes();

        // Assert
        assertEquals(2, result.size());
        assertEquals(dtDocente1, result.get(0));
        assertEquals(dtDocente2, result.get(1));
    }

    @Test
    public void getDocentesByAsignaturaIdReturnsOk() {
        // Arrange
        Integer asignaturaId = 1;
        when(docenteAsignaturaRepo.findDocentesByAsignaturaId(asignaturaId)).thenReturn(List.of(docente1, docente2));
        when(docenteConverter.convertToDto(docente1)).thenReturn(dtDocente1);
        when(docenteConverter.convertToDto(docente2)).thenReturn(dtDocente2);

        // Act
        List<DtDocente> result = usuarioService.getDocentesByAsignaturaId(asignaturaId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(dtDocente1, result.get(0));
        assertEquals(dtDocente2, result.get(1));
    }

    @Test
    public void registerReturnsOk() throws IOException, MessagingException {

        // When
        when(usuarioRepo.findByCedula(nuevoUsuario.getCedula())).thenReturn(null);
        when(usuarioRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<String> response = usuarioService.register(nuevoUsuario);

        // Assert
        assertEquals("Usuario registrado con éxito.", response.getBody());
    }

    @Test
    public void registerReturnsBadRequest() throws IOException, MessagingException {
        // When
        when(usuarioRepo.findByCedula(nuevoUsuario.getCedula())).thenReturn(user1);

        // Act
        ResponseEntity<String> response = usuarioService.register(nuevoUsuario);

        // Assert
        assertEquals("La cedula ingresada ya existe en el sistema.", response.getBody());
    }

    @Test
    public void testModificarUsuarioSuccess() {
        // When
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(user1));
        when(usuarioRepo.save(any())).thenReturn(user1);

        // Act
        ResponseEntity<?> response = usuarioService.modificarUsuario(1, dtUser1);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario actualizado con exitosamente", response.getBody());
        verify(usuarioRepo, times(1)).save(any());
    }

    @Test
    public void testModificarUsuarioWithNewCedulaSuccess() {
        // When
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(user1));
        when(usuarioRepo.save(any())).thenReturn(user1);
        when(usuarioTrRepo.findByUsuario(user1)).thenReturn(usuarioTR1);
        when(jwtUtil.isTokenExpired(usuarioTR1.getJwt())).thenReturn(true);

        // Act
        ResponseEntity<?> response = usuarioService.modificarUsuario(1, dtUser2);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario actualizado con exitosamente", response.getBody());
        verify(usuarioRepo, times(1)).save(any());
    }

    @Test
    public void testModificarUsuarioWithNewCedulaConflict() {
        // When
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(user1));
        when(usuarioTrRepo.findByUsuario(user1)).thenReturn(usuarioTR1);
        when(jwtUtil.isTokenExpired(usuarioTR1.getJwt())).thenReturn(false);

        // Act
        ResponseEntity<?> response = usuarioService.modificarUsuario(1, dtUser2);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("No se puede modificar la cedula porque el usuario tiene una sesion activa.", response.getBody());
    }

    @Test
    public void testModificarUsuarioWithNewCedulaAlreadyExists() {
        // When
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(user1));
        when(usuarioRepo.existsByCedula(dtUserForModifyWithExistingCedula.getCedula())).thenReturn(true);

        // Act
        ResponseEntity<?> responseEntity = usuarioService.modificarUsuario(1, dtUserForModifyWithExistingCedula);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("La nueva cedula ya existe en el sistema.", responseEntity.getBody());
        verify(usuarioRepo, never()).save(any());
    }

    @Test
    public void testModificarUsuarioEmailAlreadyExists() {
        // When
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(user1));
        when(usuarioRepo.existsByEmail(dtUserForModifyWithExistingEmail.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<?> response = usuarioService.modificarUsuario(1, dtUserForModifyWithExistingEmail);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El email ingresado ya existe en el sistema.", response.getBody());
        verify(usuarioRepo, never()).save(any());
    }
}
