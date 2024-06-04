package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.datatypes.DtCursadaExamen;
import Group4.StudyHubBackendG4.persistence.CursadaExamen;
import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursadaExamenRepo extends JpaRepository<CursadaExamen, Integer> {

    @Query("SELECT new Group4.StudyHubBackendG4.datatypes.DtCursadaExamen(ce.idCursadaExamen, ce.cursada.idCursada, ce.examen.idExamen, ec.usuario.nombre, ec.usuario.apellido, ec.usuario.email, ec.usuario.cedula, ce.resultado) " +
            "FROM CursadaExamen ce " +
            "JOIN ce.cursada c " +
            "JOIN EstudianteCursada ec ON ec.cursada.idCursada = c.idCursada " +
            "WHERE c.horarioAsignatura.anio = :anio AND c.asignatura.idAsignatura = :idAsignatura AND c.resultado = :resultado")
    List<DtCursadaExamen> findCursadasAExamenByAnioAndAsignatura(@Param("anio") Integer anio, @Param("idAsignatura") Integer idAsignatura, @Param("resultado") ResultadoAsignatura resultado);

        List<CursadaExamen> findByCedulaEstudianteAndExamen(String cedulaEstudiante, Examen examen);

        @Query("SELECT ce.examen FROM CursadaExamen ce WHERE ce.cedulaEstudiante = :cedulaEstudiante")
        List<Examen> findAllExamenesByCedulaEstudiante(@Param("cedulaEstudiante") String cedulaEstudiante);
}
