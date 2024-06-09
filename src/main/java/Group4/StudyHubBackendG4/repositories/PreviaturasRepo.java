package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.Carrera;
import Group4.StudyHubBackendG4.persistence.Previaturas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreviaturasRepo extends JpaRepository<Previaturas, Integer> {
    List<Previaturas> findByAsignatura(Asignatura asignatura);

    @Query("SELECT p FROM Previaturas p WHERE p.asignatura.carrera.id = :idCarrera OR p.previa.carrera.id = :idCarrera")
    List<Previaturas> findByCarreraId(Integer idCarrera);
}
