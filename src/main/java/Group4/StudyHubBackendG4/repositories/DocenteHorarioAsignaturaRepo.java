package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.DocenteHorarioAsignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteHorarioAsignaturaRepo extends JpaRepository<DocenteHorarioAsignatura, Integer> {


}