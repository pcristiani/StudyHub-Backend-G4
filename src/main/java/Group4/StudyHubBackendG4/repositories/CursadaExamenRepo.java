package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursadaExamenRepo extends JpaRepository<CursadaExamen, Integer> {
        List<CursadaExamen> findByCedulaEstudianteAndExamen(String cedulaEstudiante, Examen examen);

        @Query("SELECT ce.examen FROM CursadaExamen ce WHERE ce.cedulaEstudiante = :cedulaEstudiante")
        List<Examen> findAllExamenesByCedulaEstudiante(@Param("cedulaEstudiante") String cedulaEstudiante);
}
