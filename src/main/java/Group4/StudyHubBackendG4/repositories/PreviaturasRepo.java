package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.Previaturas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreviaturasRepo extends JpaRepository<Previaturas, Integer> {
    List<Previaturas> findByAsignatura(Optional<Asignatura> asignatura);
}
