package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.persistence.UsuarioTR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioTrRepo extends JpaRepository<UsuarioTR, Integer> {
    UsuarioTR findByUsuario(Usuario usuario);
    UsuarioTR findByJwt(String jwt);
}