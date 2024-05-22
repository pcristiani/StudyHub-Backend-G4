package Group4.StudyHubBackendG4.repositories;
import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.HorarioAsignatura;
import Group4.StudyHubBackendG4.persistence.HorarioDias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface HorarioDiasRepo extends JpaRepository<HorarioDias, Integer> {
}
