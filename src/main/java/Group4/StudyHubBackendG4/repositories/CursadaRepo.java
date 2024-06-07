package Group4.StudyHubBackendG4.repositories;

import Group4.StudyHubBackendG4.datatypes.DtCursadaPendiente;
import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.Cursada;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.persistence.HorarioAsignatura;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursadaRepo extends JpaRepository<Cursada, Integer> {

    @Query("SELECT new Group4.StudyHubBackendG4.datatypes.DtCursadaPendiente(ec.usuario.idUsuario, c.idCursada, ec.usuario.nombre, ec.usuario.apellido, ec.usuario.email) " +
            "FROM EstudianteCursada ec JOIN ec.cursada c " +
            "WHERE c.horarioAsignatura.anio = :anio AND c.asignatura.idAsignatura = :idAsignatura AND c.resultado = :resultado")
    List<DtCursadaPendiente> findCursadasPendientesByAnioAndAsignatura(@Param("anio") Integer anio, @Param("idAsignatura") Integer idAsignatura, @Param("resultado") ResultadoAsignatura resultado);

    @Query("SELECT ec.usuario FROM EstudianteCursada ec JOIN ec.cursada c WHERE c.horarioAsignatura = :horarioAsignatura")
    List<Usuario> findEstudianteByHorario(@Param("horarioAsignatura") HorarioAsignatura horarioAsignatura);
}

