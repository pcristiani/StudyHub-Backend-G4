package Group4.StudyHubBackendG4.repositories;
import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.HorarioAsignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface HorarioAsignaturaRepo extends JpaRepository<HorarioAsignatura, Integer> {

    Collection<HorarioAsignatura> findByAsignatura(Asignatura asig);

    /*
    @Query("SELECT DISTINCT ha FROM HorarioAsignatura ha " +
            "JOIN ha.dias hd " +
            "JOIN DocenteHorarioAsignatura dha ON ha = dha.horarioAsignatura " +
            "WHERE dha.docente.nombre = :docenteNombre " +
            "AND ha.anio = :anio " +
            "AND hd.idDia IN :dias " +
            "AND ha.asignatura.idAsignatura = :idAsignatura")
    List<HorarioAsignatura> findHorarioAsignaturasByDocenteAndAnioAndDiasAndAsignaturaId(
            @Param("idAsignatura") Integer idAsignatura,
            @Param("docenteNombre") String docenteNombre,
            @Param("anio") Integer anio,
            @Param("dias") List<Integer> dias);

     */





}
