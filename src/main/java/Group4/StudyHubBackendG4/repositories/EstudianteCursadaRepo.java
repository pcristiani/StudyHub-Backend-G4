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

    @Query("SELECT DISTINCT a FROM EstudianteCursada ec " +
            "JOIN ec.cursada c " +
            "JOIN c.asignatura a " +
            "LEFT JOIN CursadaExamen ce ON ce.cursada.idCursada = c.idCursada " +
            "WHERE ec.usuario = :usuario AND (c.resultado = 'APROBADO' OR ce.resultado = 'APROBADO')")
    List<Asignatura> findAprobadasByEstudiante(@Param("usuario") Usuario usuario);

    @Query("SELECT DISTINCT a FROM EstudianteCursada ec " +
            "JOIN ec.cursada c " +
            "JOIN c.asignatura a " +
            "LEFT JOIN CursadaExamen ce ON ce.cursada.idCursada = c.idCursada " +
            "WHERE ec.usuario = :usuario AND (c.resultado != 'APROBADO' AND (ce.resultado IS NULL OR ce.resultado != 'APROBADO'))")
    List<Asignatura> findNoAprobadasByEstudiante(@Param("usuario") Usuario usuario);
}
