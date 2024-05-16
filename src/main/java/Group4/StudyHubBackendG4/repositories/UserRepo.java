package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Usuario, Integer> {
    Usuario findByCedula(String cedula);
    Usuario findByEmail(String email);
    Boolean existsByCedula(String cedula);
    Boolean existsByEmail(String email);
}
