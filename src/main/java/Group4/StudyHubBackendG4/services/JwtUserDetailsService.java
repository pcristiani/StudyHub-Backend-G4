package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.persistence.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("No existe un usuario registrado con el usuario:  " + username);
        }

        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(String.valueOf("AUTH_USER")));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}