package Group4.StudyHubBackendG4.persistence;

import Group4.StudyHubBackendG4.datatypes.DtUser;
import Group4.StudyHubBackendG4.services.PasswordService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Users")
@Data
public class User {
        @Id
        private Integer id;
        private String name;
        private String surname;
        private String email;
        private String birthdate;
        private String username;
        private String password;
        private String jwtToken;

        public User() {

        }

        public User(DtUser dtUser) {
                this.id = dtUser.getId();
                this.name = dtUser.getName();
                this.surname = dtUser.getSurname();
                this.email = dtUser.getEmail();
                this.birthdate = dtUser.getBirthdate();
                this.username = dtUser.getUsername();
                this.password = dtUser.getPassword();
                this.jwtToken = dtUser.getJwtToken();
        }

        public void encryptPassword(){
                this.password = PasswordService.getInstance().hashPassword(this.password);
        }

        public void decryptPassword(){
                this.password = PasswordService.getInstance().hashPassword(this.password);
        }

        public void setRandomJwt() {
                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                Map<String, Object> claims = new HashMap<>();
                claims.put("username", this.username);
                claims.put("userId", this.id);
                claims.put("email", this.email);
                claims.put("name", this.name);
                claims.put("surname", this.surname);
                claims.put("birthdate", this.birthdate);

                long expirationTime = 1000 * 60 * 60 * 24;
                Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

                    this.jwtToken = Jwts.builder()
                                .setClaims(claims)
                                .setSubject("userDetails")
                                .setIssuedAt(new Date(System.currentTimeMillis()))
                                .setExpiration(expirationDate)
                                .signWith(key)
                                .compact();
        }
}
