package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.EstudianteCursada;
import Group4.StudyHubBackendG4.persistence.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteCursadaRepo extends JpaRepository<EstudianteCursada, Integer> {
    @Query("SELECT ec FROM EstudianteCursada ec JOIN ec.cursada c JOIN c.asignatura a WHERE ec.usuario = :usuario AND a = :asignatura")
    List<EstudianteCursada> findByEstudianteAndAsignatura(@Param("usuario") Usuario usuario, @Param("asignatura") Asignatura asignatura);
}
