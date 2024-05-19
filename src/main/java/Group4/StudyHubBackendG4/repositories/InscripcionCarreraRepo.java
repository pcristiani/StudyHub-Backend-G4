package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Carrera;
import Group4.StudyHubBackendG4.persistence.InscripcionCarrera;
import Group4.StudyHubBackendG4.persistence.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InscripcionCarreraRepo extends JpaRepository<InscripcionCarrera, Integer> {
    InscripcionCarrera findByIdInscripcion(Integer id);
    Boolean existsByUsuario(Usuario u);
    Optional<InscripcionCarrera> findByUsuarioAndCarreraAndActivaAndValidada(Usuario usuario, Carrera carrera, boolean activa, boolean validada);
}

