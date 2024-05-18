package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignaturaRepo extends JpaRepository<Carrera, Integer>{

}
