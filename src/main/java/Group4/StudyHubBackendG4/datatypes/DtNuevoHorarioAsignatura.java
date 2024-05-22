package Group4.StudyHubBackendG4.datatypes;

import lombok.Data;

import java.util.List;

@Data
public class DtNuevoHorarioAsignatura {
    private Integer idHorarioAsignatura;
    private Integer idDocente;
    private Integer anio;
    private Integer horaInicio;
    private Integer horaFin;
    private List<Integer> dias;     //Array de Integers, ej [2, 4]

}

/*
Validar:
- Obtener todos los HorarioAsignatura de la Asignatura elegida para un año lectivo dado, JOIN horarioDia para obtener tambien los dias en la consulta.
- En esos obtenidos, que no se solapen horaInicio, horaFin, y que sea el mismo dia.

- Obtener todos los HorarioAsignatura del Docente desde DocenteHorarioAsignaturaRepo, JOIN HorarioAsignatura JOIN HorarioDia

  SELECT distinct(HorarioAsignatura)
- FROM HorarioAsignatura JOIN DocenteHorarioAsignatura JOIN HorarioDias
  WHERE docente="pepe" AND anio="123" AND (dia ="2" OR dia ="4")

  con ésto validar que no se solapen los horarios seleccionados, con los devueltos


Crear HorarioAsignatura con primeros 4 datos,
 */