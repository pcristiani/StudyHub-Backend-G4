package Group4.StudyHubBackendG4.utils;

import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.services.UsuarioService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class JwtUserDetailsUtil implements UserDetailsService {
    private final UsuarioService usuarioService;

    public JwtUserDetailsUtil(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UserDetails loadUserByCedula(String cedula) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.getUserByUsername(cedula);

        if (usuario == null) {
            throw new UsernameNotFoundException("No existe un usuario registrado con la cedula:  " + cedula);
        }

        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(String.valueOf("AUTH_USER")));
        return new org.springframework.security.core.userdetails.User(usuario.getCedula(), usuario.getPassword(), authorities);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}