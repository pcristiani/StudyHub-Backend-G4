package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AutenticacionService {

    @Autowired
    private UsuarioRepo usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    private static final long EXPIRATION_TIME = 900_000;

    private final String secretKeyString = "44b4ff323b3509c5b897e8199c0655197797128fa71d81335f68b9a2a3286f30";

    private final Key secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

    @Transactional
    public String authenticateUser(String username, String candidatePassword) {
        Usuario usuario = usuarioRepository.findByCedula(username);
        if (usuario != null && PasswordService.getInstance().checkPasswordHash(candidatePassword, usuario.getPassword())){

            if(!usuario.getValidado() || !usuario.getActivo()){
                return "notJoined";
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", usuario.getIdUsuario());
            claims.put("ci", usuario.getCedula());
            claims.put("rol", usuario.getRol());

            String jwt = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(usuario.getCedula())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();

            usuarioService.actualizarJwt(usuario, jwt);
            return jwt;
        } else {
            return null;
        }
    }

    @Transactional
    public void logoutUser(String jwt) {
        Usuario usuario = usuarioService.getUserByJwt(jwt);
        usuarioService.actualizarJwt(usuario, null);
    }
}
