package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.persistence.Carrera;
import Group4.StudyHubBackendG4.persistence.InscripcionCarrera;
import Group4.StudyHubBackendG4.persistence.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface InscripcionCarreraRepo extends JpaRepository<InscripcionCarrera, Integer> {
    InscripcionCarrera findByIdInscripcion(Integer id);
    Boolean existsByUsuario(Usuario u);
    Optional<InscripcionCarrera> findByUsuarioAndCarreraAndActivaAndValidada(Usuario usuario, Carrera carrera, boolean activa, boolean validada);

    Optional<InscripcionCarrera> findByUsuarioAndCarreraAndActiva(Usuario usuario, Carrera carrera, boolean activa);
    @Query("SELECT ic FROM InscripcionCarrera ic WHERE ic.validada = false")
    Collection<InscripcionCarrera> findInscripcionesPendientes();
    @Query("SELECT ic FROM InscripcionCarrera ic WHERE ic.validada = false AND ic.carrera = :carrera" )
    Collection<InscripcionCarrera> findInscriptosPendientes(@Param("carrera") Carrera carrera);
}

