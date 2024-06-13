package com.example.demo;

import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
import Group4.StudyHubBackendG4.services.UsuarioService;
import Group4.StudyHubBackendG4.utils.converters.UsuarioConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private UsuarioConverter usuarioConverter;

    @InjectMocks
    private UsuarioService usuarioService;

    private List<Usuario> usuarios;
    private List<DtUsuario> dtUsuarios;

    @BeforeEach
    public void setUp() {
        // Set up mock data
        Usuario user1 = new Usuario();
        Usuario user2 = new Usuario();
        usuarios = List.of(user1, user2);

        DtUsuario dtUser1 = new DtUsuario();
        DtUsuario dtUser2 = new DtUsuario();
        dtUsuarios = List.of(dtUser1, dtUser2);
    }

    @Test
    public void testGetUsuarios() {
        // Define behavior of mocks
        when(usuarioRepo.findAll()).thenReturn(usuarios);
        when(usuarioConverter.convertToDto(usuarios.get(0))).thenReturn(dtUsuarios.get(0));
        when(usuarioConverter.convertToDto(usuarios.get(1))).thenReturn(dtUsuarios.get(1));

        // Call the method under test
        List<DtUsuario> result = usuarioService.getUsuarios();

        // Verify the results
        assertEquals(2, result.size());
        assertEquals(dtUsuarios.get(0), result.get(0));
        assertEquals(dtUsuarios.get(1), result.get(1));
    }
}
