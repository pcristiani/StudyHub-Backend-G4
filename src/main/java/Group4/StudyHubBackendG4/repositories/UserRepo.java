package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
}
