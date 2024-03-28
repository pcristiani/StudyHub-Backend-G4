package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.persistence.User;
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
public class JwtService {

    private static JwtService instance = null;

    private final String secretKeyString = "44b4ff323b3509c5b897e8199c0655197797128fa71d81335f68b9a2a3286f30";

    private final Key secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

    private JwtService() {
    }

    public static JwtService getInstance() {
        if (instance == null) {
            instance = new JwtService();
        }
        return instance;
    }
    public String generateJwt(User user) {
        String jwtToken = "";
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("surname", user.getSurname());
        claims.put("birthdate", user.getBirthdate());

        long expirationTime = 1000 * 60 * 60 * 24;
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

    public User validateJwt(String jwtToken) {
        User user = new User();
        Map<String, Object> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken).getBody();

        user.setUsername((String) claims.get("username"));
        user.setEmail((String) claims.get("email"));
        user.setName((String) claims.get("name"));
        user.setSurname((String) claims.get("surname"));
        user.setBirthdate((String) claims.get("birthdate"));

        return user;
    }

    public String getUsernameFromToken(String token) {
        Claims c =  getAllClaimsFromToken(token);
        return c.get("username", String.class);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
}
