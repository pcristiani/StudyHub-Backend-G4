package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.DocenteExamen;
import Group4.StudyHubBackendG4.persistence.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocenteExamenRepo extends JpaRepository<DocenteExamen, Integer> {
    @Query("SELECT de.docente FROM DocenteExamen de WHERE de.idExamen = :examen")
    List<Docente> findDocentesByExamen(@Param("examen") Examen examen);
}
