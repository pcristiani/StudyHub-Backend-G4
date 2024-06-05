package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.EstudianteCursada;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
import Group4.StudyHubBackendG4.utils.enums.ResultadoExamen;
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
            "WHERE ec.usuario = :usuario AND (c.resultado = 'APROBADO' OR ce.resultado = :resultado)")
    List<Asignatura> findAprobadasByEstudiante(@Param("usuario") Usuario usuario, @Param("resultado") ResultadoAsignatura resultado);

    @Query("SELECT DISTINCT a FROM EstudianteCursada ec " +
            "JOIN ec.cursada c " +
            "JOIN c.asignatura a " +
            "LEFT JOIN CursadaExamen ce ON ce.cursada.idCursada = c.idCursada " +
            "WHERE ec.usuario = :usuario AND (c.resultado != :resultadoAsignatura AND (ce.resultado IS NULL OR ce.resultado != :resultadoExamen))")
    List<Asignatura> findNoAprobadasByEstudiante(@Param("usuario") Usuario usuario, @Param("resultadoAsignatura") ResultadoAsignatura resultadoAsignatura, @Param("resultadoExamen") ResultadoExamen resultadoExamen);

    @Query("SELECT ec.cursada.asignatura FROM EstudianteCursada ec WHERE ec.usuario = :usuario")
    List<Asignatura> findByEstudiante(@Param("usuario") Usuario usuario);
}
