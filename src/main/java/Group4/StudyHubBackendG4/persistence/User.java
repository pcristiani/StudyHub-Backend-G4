package Group4.StudyHubBackendG4.persistence;

import Group4.StudyHubBackendG4.datatypes.DtUser;
import Group4.StudyHubBackendG4.services.PasswordService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.*;
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
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String name;
        private String surname;
        private String email;
        private String birthdate;
        private String username;
        private String password;
        @Column(columnDefinition = "VARCHAR(MAX)")
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
}
