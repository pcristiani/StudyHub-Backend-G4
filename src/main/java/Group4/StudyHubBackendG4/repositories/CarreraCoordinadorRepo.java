package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Carrera;
import Group4.StudyHubBackendG4.persistence.CarreraCoordinador;
import Group4.StudyHubBackendG4.persistence.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarreraCoordinadorRepo extends JpaRepository<CarreraCoordinador, Integer> {
    @Query("SELECT c.carrera FROM CarreraCoordinador c WHERE c.usuario.idUsuario = :coordinadorId")
    List<Carrera> findCarrerasByCoordinadorId(@Param("coordinadorId") Integer coordinadorId);
    @Query("SELECT COUNT(c) FROM CarreraCoordinador c WHERE c.carrera.idCarrera = :carreraId")
    long countCoordinadoresByCarreraId(@Param("carreraId") Integer carreraId);

    boolean existsByCarreraAndUsuario(Carrera carrera, Usuario usuario);
}




