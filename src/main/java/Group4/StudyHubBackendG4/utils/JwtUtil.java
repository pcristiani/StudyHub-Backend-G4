package Group4.StudyHubBackendG4.utils;

import Group4.StudyHubBackendG4.persistence.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static JwtUtil instance = null;

    private final String secretKeyString = "44b4ff323b3509c5b897e8199c0655197797128fa71d81335f68b9a2a3286f30";

    private final Key secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

    private JwtUtil() {
    }

    public static JwtUtil getInstance() {
        if (instance == null) {
            instance = new JwtUtil();
        }
        return instance;
    }
    public String generateJwt(Usuario usuario) {
        String jwtToken = "";
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getIdUsuario());
        claims.put("cedula", usuario.getCedula());
        claims.put("rol", usuario.getRol());
        claims.put("nombre", usuario.getNombre());
        claims.put("apellido", usuario.getApellido());
        claims.put("email", usuario.getEmail());
        claims.put("fechaNacimiento", usuario.getFechaNacimiento());

        long expirationTime = 1000L * 60 * 60 * 24 * 365;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        jwtToken = Jwts.builder()
                .setClaims(claims)
                .setSubject("userDetails")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return jwtToken;
    }

    public Usuario validateJwt(String jwtToken) {
        Usuario usuario = new Usuario();
        Map<String, Object> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken).getBody();

        usuario.setCedula((String) claims.get("cedula"));
        usuario.setEmail((String) claims.get("email"));
        usuario.setNombre((String) claims.get("nombre"));
        usuario.setApellido((String) claims.get("apellido"));
        usuario.setFechaNacimiento((String) claims.get("fechaNacimiento"));
        usuario.setRol((String) claims.get("rol"));

        return usuario;
    }

    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("rol", String.class);
    }

    public String getCedulaFromToken(String token) {
        Claims c =  getAllClaimsFromToken(token);
        return c.get("cedula", String.class);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String cedula = getCedulaFromToken(token);
        return (cedula.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
}
