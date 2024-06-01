package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.Carrera;
import Group4.StudyHubBackendG4.persistence.Docente;
import Group4.StudyHubBackendG4.persistence.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface ExamenRepo extends JpaRepository<Examen, Integer> {

    boolean existsByAsignaturaAndFechaHora(Asignatura asignatura, LocalDateTime fechaHoraExamen);
}

