package Group4.StudyHubBackendG4.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Group4.StudyHubBackendG4.datatypes.DtNuevoUsuario;
import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
import Group4.StudyHubBackendG4.services.ActividadService;
import Group4.StudyHubBackendG4.services.UsuarioService;
import Group4.StudyHubBackendG4.utils.converters.UsuarioConverter;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private UsuarioConverter usuarioConverter;

    @Mock
    private ActividadService actividadService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario user1;
    private Usuario user2;
    private DtUsuario dtUser1;
    private DtUsuario dtUser2;
    private DtNuevoUsuario nuevoUsuario;

    @BeforeEach
    public void setUp() {
        // Arrange
        user1 = new Usuario();
        user1.setIdUsuario(1);
        user1.setNombre("John");
        user1.setApellido("Doe");

        user2 = new Usuario();
        user2.setIdUsuario(2);
        user2.setNombre("Jane");
        user2.setApellido("Smith");

        dtUser1 = new DtUsuario();
        dtUser1.setIdUsuario(1);
        dtUser1.setNombre("John");
        dtUser1.setApellido("Doe");

        dtUser2 = new DtUsuario();
        dtUser2.setIdUsuario(2);
        dtUser2.setNombre("Jane");
        dtUser2.setApellido("Smith");

        nuevoUsuario = new DtNuevoUsuario();
        nuevoUsuario.setCedula("12345678");
        nuevoUsuario.setNombre("John");
        nuevoUsuario.setApellido("Doe");
        nuevoUsuario.setEmail("john.doe@example.com");
        nuevoUsuario.setRol("E");
    }

    @Test
    public void testGetUsuariosReturnsOk() {
        // When
        when(usuarioRepo.findAll()).thenReturn(Arrays.asList(user1, user2));
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
    public void testRegisterReturnsOk() throws IOException, MessagingException {

        // When
        when(usuarioRepo.findByCedula(nuevoUsuario.getCedula())).thenReturn(null);
        when(usuarioRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<String> response = usuarioService.register(nuevoUsuario);

        // Assert
        assertEquals("Usuario registrado con éxito.", response.getBody());
    }

    @Test
    public void testRegisterReturnsBadRequest() throws IOException, MessagingException {
        // When
        when(usuarioRepo.findByCedula(nuevoUsuario.getCedula())).thenReturn(user1);

        // Act
        ResponseEntity<String> response = usuarioService.register(nuevoUsuario);

        // Assert
        assertEquals("La cedula ingresada ya existe en el sistema.", response.getBody());
    }
}
