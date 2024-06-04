package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursadaExamenRepo extends JpaRepository<CursadaExamen, Integer> {
        List<CursadaExamen> findByCedulaEstudianteAndExamen(String cedulaEstudiante, Examen examen);
}
