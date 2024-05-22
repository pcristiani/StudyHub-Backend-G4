package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.DocenteAsignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteAsignaturaRepo extends JpaRepository<DocenteAsignatura, Integer> {
    @Query("SELECT da.docente FROM DocenteAsignatura da WHERE da.asignatura.idAsignatura = :asignaturaId")
    List<Docente> findDocentesByAsignaturaId(@Param("asignaturaId") Integer asignaturaId);
}
