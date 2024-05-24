package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.DocenteAsignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocenteAsignaturaRepo extends JpaRepository<DocenteAsignatura, Integer> {
}
