package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AsignaturaRepo extends JpaRepository<Asignatura, Integer>{

    Boolean existsByNombreAndCarrera(String nombreAsignatura, Carrera nombreCarrera);

    List<Asignatura> findByCarrera(Carrera idCarrera);

    List<Asignatura> findByCarreraAndTieneExamen(Carrera carrera, Boolean tieneExamen);
}
