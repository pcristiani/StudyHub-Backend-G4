package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepository;

    private static final long EXPIRATION_TIME = 900_000;

    private final String secretKeyString = "44b4ff323b3509c5b897e8199c0655197797128fa71d81335f68b9a2a3286f30";

    private final Key secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

    @Transactional
    public String authenticateUser(String username, String candidatePassword) {
        User user = userRepository.findByUsername(username);
        if (user != null && PasswordService.getInstance().checkPasswordHash(candidatePassword, user.getPassword())){

            Map<String, Object> claims = new HashMap<>();
            claims.put("name", user.getName());
            claims.put("surname", user.getSurname());
            claims.put("email", user.getEmail());
            claims.put("username", user.getUsername());
            claims.put("birthdate", user.getBirthdate());

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();
        } else {
            return null;
        }
    }
}
