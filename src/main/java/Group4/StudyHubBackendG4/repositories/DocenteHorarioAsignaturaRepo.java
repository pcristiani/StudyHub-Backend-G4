package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.DocenteHorarioAsignatura;
import Group4.StudyHubBackendG4.persistence.HorarioDias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteHorarioAsignaturaRepo extends JpaRepository<DocenteHorarioAsignatura, Integer> {
    @Query("SELECT dha FROM DocenteHorarioAsignatura dha WHERE dha.docente.idDocente = :docenteId")
    List<DocenteHorarioAsignatura> findByDocenteId(Integer docenteId);

    @Query("SELECT DISTINCT hd FROM DocenteHorarioAsignatura dha " +
            "JOIN dha.horarioAsignatura ha " +
            "JOIN ha.horarioDias hd " +
            "WHERE dha.docente.idDocente = :docenteId " +
            "AND ha.anio = :anio")
    List<HorarioDias> findHorarioDiasByDocenteIdAndAnio(Integer docenteId, Integer anio);


}