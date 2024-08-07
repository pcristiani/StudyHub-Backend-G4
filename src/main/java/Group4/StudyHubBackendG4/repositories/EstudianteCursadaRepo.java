package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.*;
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
            "WHERE ec.usuario = :usuario AND (c.resultado = :resultado OR ce.resultado = 'APROBADO')")
    List<Asignatura> findAprobadasByEstudiante(@Param("usuario") Usuario usuario, @Param("resultado") ResultadoAsignatura resultado);

    @Query("SELECT ec.cursada.asignatura FROM EstudianteCursada ec WHERE ec.usuario = :usuario")
    List<Asignatura> findByEstudiante(@Param("usuario") Usuario usuario);

    @Query("SELECT ec FROM EstudianteCursada ec WHERE ec.usuario = :usuario")
    List<EstudianteCursada> findCursadasEstudiante(@Param("usuario") Usuario usuario);

    @Query("SELECT ec.usuario FROM EstudianteCursada ec WHERE ec.cursada.idCursada = :cursadaId")
    Usuario findUsuarioByCursadaId(@Param("cursadaId") Integer cursadaId);

    @Query("SELECT DISTINCT a FROM EstudianteCursada ec " +
            "JOIN ec.cursada c " +
            "JOIN c.asignatura a " +
            "LEFT JOIN CursadaExamen ce ON ce.cursada.idCursada = c.idCursada " +
            "WHERE ec.usuario = :usuario AND (c.resultado = :resultadoAsignatura AND a.carrera = :carrera)")
    List<Asignatura> findExamenPendienteEstudiante(@Param("usuario") Usuario usuario, @Param("resultadoAsignatura") ResultadoAsignatura examen, @Param("carrera") Carrera carrera);
}
