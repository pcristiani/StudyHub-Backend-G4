package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRepo extends JpaRepository<Actividad, Integer>{

}
