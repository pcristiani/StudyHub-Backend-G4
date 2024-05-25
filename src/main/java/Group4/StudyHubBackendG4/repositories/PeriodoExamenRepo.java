package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Carrera;
import Group4.StudyHubBackendG4.persistence.PeriodoExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PeriodoExamenRepo extends JpaRepository<PeriodoExamen, Integer> {
    List<PeriodoExamen> findByCarreraAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(Carrera carrera, LocalDate fechaFin, LocalDate fechaInicio);
}
