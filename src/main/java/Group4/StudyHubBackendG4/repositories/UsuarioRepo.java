package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.Actividad;
import Group4.StudyHubBackendG4.persistence.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {
    Usuario findByCedula(String cedula);
    Usuario findByEmail(String email);
    @Query("SELECT u FROM Usuario u WHERE u.validado = :validado")
    List<Usuario> findAllByValidado(@Param("validado") Boolean validado);
    Boolean existsByCedula(String cedula);
    Boolean existsByEmail(String email);

    @Query("SELECT a FROM Actividad a WHERE a.usuario = :usuario")
    List<Actividad> findActividadesByUsuario(@Param("usuario") Usuario usuario);

    List<Usuario> findByRol(String c);
}
