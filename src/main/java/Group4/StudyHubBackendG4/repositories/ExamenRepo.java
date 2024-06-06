package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.datatypes.DtExamen;
import Group4.StudyHubBackendG4.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamenRepo extends JpaRepository<Examen, Integer> {

    boolean existsByAsignaturaAndFechaHora(Asignatura asignatura, LocalDateTime fechaHoraExamen);

    List<Examen> findByAsignatura(Asignatura asignatura);

    List<Examen> findByPeriodoExamen(PeriodoExamen periodoExamen);
}

