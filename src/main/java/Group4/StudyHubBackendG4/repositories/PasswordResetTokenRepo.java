package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Integer> {

    PasswordResetToken findByToken(String token);

}
