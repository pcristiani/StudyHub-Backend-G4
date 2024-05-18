package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocenteRepo extends JpaRepository<Docente, Integer>{

}
