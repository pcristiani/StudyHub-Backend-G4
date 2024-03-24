package Group4.StudyHubBackendG4.persistence;

import Group4.StudyHubBackendG4.datatypes.DtUser;
import Group4.StudyHubBackendG4.services.PasswordService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
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

        public User(DtUser dtUser) {
                this.name = dtUser.getName();
                this.surname = dtUser.getSurname();
                this.email = dtUser.getEmail();
                this.birthdate = dtUser.getBirthdate();
                this.username = dtUser.getUsername();
        }

        public DtUser toDtUser() {
                DtUser dtUser = new DtUser();
                dtUser.setId(this.getId());
                dtUser.setName(this.getName());
                dtUser.setSurname(this.getSurname());
                dtUser.setEmail(this.getEmail());
                dtUser.setBirthdate(this.getBirthdate());
                dtUser.setUsername(this.getUsername());
                return dtUser;
        }

        public void encryptPassword(){
                this.password = PasswordService.getInstance().hashPassword(this.password);
        }

        public void decryptPassword(){
                this.password = PasswordService.getInstance().hashPassword(this.password);
        }
}
