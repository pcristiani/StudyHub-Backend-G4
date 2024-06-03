package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.*;
import Group4.StudyHubBackendG4.utils.converters.AsignaturaConverter;
import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
import Group4.StudyHubBackendG4.utils.enums.ResultadoAsignatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepo asignaturaRepo;

    @Autowired
    private PreviaturasRepo previaturasRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private CarreraRepo carreraRepo;

    @Autowired
    private DocenteRepo docenteRepo;

    @Autowired
    private HorarioAsignaturaRepo horarioAsignaturaRepo;

    @Autowired
    private HorarioDiasRepo horarioDiasRepo;

    @Autowired
    private DocenteAsignaturaRepo docenteAsignaturaRepo;

    @Autowired
    private DocenteHorarioAsignaturaRepo docenteHorarioAsignaturaRepo;

    @Autowired
    private EstudianteCursadaRepo estudianteCursadaRepo;

    @Autowired
    private CursadaRepo cursadaRepo;

    @Autowired
    private InscripcionCarreraRepo inscripcionCarreraRepo;

    @Autowired
    private AsignaturaConverter asignaturaConverter;

    @Autowired
    private HorarioAsignaturaConverter horarioAsignaturaConverter;

    private List<DtAsignatura> convertToDtAsignatura (List<Asignatura> asignaturas) {
        return asignaturas.stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList());
    }
    public List<DtAsignatura> getAsignaturas() {
        return convertToDtAsignatura(asignaturaRepo.findAll());
    }

    public ResponseEntity<?> getAsignaturasDeCarrera(Integer idCarrera) {
        Carrera carrera = carreraRepo.findById(idCarrera)
                .orElse(null);

        if (carrera == null){
            return ResponseEntity.badRequest().body("Carrera no encontrada");
        }

        return ResponseEntity.ok().body(asignaturaRepo.findByCarrera(carrera)
                .stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList()));
        return convertToDtAsignatura(asignaturaRepo.findByCarrera(carrera));
    }

    public ResponseEntity<?> getAsignaturasDeCarreraConExamen(Integer idCarrera) {
        Carrera carrera = carreraRepo.findById(idCarrera)
                .orElse(null);

        return convertToDtAsignatura(asignaturaRepo.findByCarreraAndTieneExamen(carrera, true));
        if (carrera == null){
            return ResponseEntity.badRequest().body("Carrera no encontrada");
        }

        return ResponseEntity.ok().body(asignaturaRepo.findByCarreraAndTieneExamen(carrera, true)
                .stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList()));
    }

    public List<DtAsignatura> getAsignaturasAprobadas(Integer idEstudiante) {
        Usuario user = usuarioRepo.findById(idEstudiante).orElse(null) ;
        return convertToDtAsignatura(estudianteCursadaRepo.findAprobadasByEstudiante(user));
    }

    public List<DtAsignatura> getAsignaturasNoAprobadas(Integer idEstudiante) {
        Usuario user = usuarioRepo.findById(idEstudiante).orElse(null) ;
        return convertToDtAsignatura(estudianteCursadaRepo.findNoAprobadasByEstudiante(user));
    }

    public List<DtHorarioAsignatura> getHorarios(Integer id) {
        Asignatura asig = asignaturaRepo.findById(id).orElse(null);
        return asig == null ? null :
        horarioAsignaturaRepo.findByAsignatura(asig)
                .stream()
                .map(DtHorarioAsignatura::horarioAsignaturafromDtHorarioAsignatura)
                .toList();
    }

    public ResponseEntity<?> altaAsignatura(DtNuevaAsignatura dtNuevaAsignatura) {

        Carrera carrera = carreraRepo.findById(dtNuevaAsignatura.getIdCarrera()).orElse(null);
        if (carrera == null) {
            return ResponseEntity.badRequest().body("Carrera no encontrada.");
        }

        List<Integer> idDocentes = dtNuevaAsignatura.getIdDocentes();
        if (idDocentes == null || idDocentes.isEmpty()) {
            return ResponseEntity.badRequest().body("Ingrese al menos un docente.");
        }

        List<Docente> docentes = idDocentes.stream()
                .map(docenteRepo::findById)
                .map(optionalDocente -> optionalDocente.orElse(null))
                .collect(Collectors.toList());

        if (docentes.contains(null)) {
            return ResponseEntity.badRequest().body("Uno o más docentes no encontrados.");
        }

        if (asignaturaRepo.existsByNombreAndCarrera(dtNuevaAsignatura.getNombre(), carrera)) {
            return ResponseEntity.badRequest().body("La asignatura ya existe.");
        }

        if (this.validarCircularidad(null, dtNuevaAsignatura.getPreviaturas())) {
            return ResponseEntity.badRequest().body("Existen circularidades en las previaturas seleccionadas.");
        }

        DtAsignatura dtAsignatura = dtNuevaAsignatura.dtAsignaturaFromDtNuevaAsignatura(dtNuevaAsignatura);
        Asignatura asignatura = asignaturaConverter.convertToEntity(dtAsignatura);

        // Prepare and validate Previaturas
        List<Integer> idsPreviaturas = dtNuevaAsignatura.getPreviaturas();
        List<Previaturas> previaturas = new ArrayList<>();
        if (idsPreviaturas != null && !idsPreviaturas.isEmpty()) {
            for (Integer idPrevia : idsPreviaturas) {
                Asignatura previaAsignatura = asignaturaRepo.findById(idPrevia).orElse(null);
                if (previaAsignatura == null) {
                    return ResponseEntity.badRequest().body("Previa asignatura no encontrada");
                }

                // Validate if Previaturas belong to the same Carrera
                if (!previaAsignatura.getCarrera().equals(carrera)) {
                    return ResponseEntity.badRequest().body("Todas las previaturas deben pertenecer a la misma carrera.");
                }

                Previaturas previatura = new Previaturas();
                previatura.setAsignatura(asignatura);
                previatura.setPrevia(previaAsignatura);
                previaturas.add(previatura);
            }
        }

        // Save Asignatura
        asignaturaRepo.save(asignatura);

        // Save DocenteAsignatura
        for (Docente docente : docentes) {
            DocenteAsignatura da = new DocenteAsignatura();
            da.setDocente(docente);
            da.setAsignatura(asignatura);
            docenteAsignaturaRepo.save(da);
        }

        // Save Previaturas
        for (Previaturas previatura : previaturas) {
            previaturasRepo.save(previatura);
        }

        return ResponseEntity.ok().body("Asignatura creada exitosamente.");
    }

/*
    private Boolean validarCircularidad(List<Integer> idsPreviaturas) {
        if (idsPreviaturas == null || idsPreviaturas.isEmpty()) {
            return false;
        }

        Set<Integer> visitado = new HashSet<>();
        Set<Integer> stack = new HashSet<>();

        for (Integer idPrevia : idsPreviaturas) {
            if (esCiclico(idPrevia, visitado, stack)) {
                return true;
            }
        }

        return false;
    }

    private boolean esCiclico(Integer idAsignatura, Set<Integer> visitado, Set<Integer> stack) {
        if (stack.contains(idAsignatura)) {
            return true; // Ciclo detectado
        }

        if (visitado.contains(idAsignatura)) {
            return false;
        }

        visitado.add(idAsignatura);
        stack.add(idAsignatura);

        Asignatura asignatura = asignaturaRepo.findById(idAsignatura).orElse(null);
        List<Previaturas> previaturas = previaturasRepo.findByAsignatura(asignatura);
        for (Previaturas previatura : previaturas) {
            if (esCiclico(previatura.getPrevia().getIdAsignatura(), visitado, stack)) {
                return true;
            }
        }

        stack.remove(idAsignatura);
        return false;
    }
*/
    public ResponseEntity<?> registroHorarios(Integer idAsignatura, DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura) {
        Asignatura asignatura = asignaturaRepo.findById(idAsignatura)
                .orElse(null);

        if (asignatura == null) {
            return ResponseEntity.badRequest().body("idAsignatura invalido");
        }

        Docente docente = docenteRepo.findById(dtNuevoHorarioAsignatura.getIdDocente())
                .orElse(null);

        if (docente == null) {
            return ResponseEntity.badRequest().body("idDocente invalido");
        }

        List<HorarioDias> existingHorarioDias = docenteHorarioAsignaturaRepo.findHorarioDiasByDocenteIdAndAnio(docente.getIdDocente(), dtNuevoHorarioAsignatura.getAnio());

        for (DtHorarioDias newHorarioDia : dtNuevoHorarioAsignatura.getDtHorarioDias()) {
            DiaSemana diaSemana = newHorarioDia.getDiaSemana();
            String horaInicioStr = newHorarioDia.getHoraInicio();
            String horaFinStr = newHorarioDia.getHoraFin();

            // Validar y convertir horaInicio y horaFin
            if (!esHoraValida(horaInicioStr) || !esHoraValida(horaFinStr)) {
                return ResponseEntity.badRequest().body("Formato de hora inválido. Use HH:mm");
            }

            int horaInicio = convertirHora(horaInicioStr);
            int horaFin = convertirHora(horaFinStr);

            if (horaInicio >= horaFin) {
                return ResponseEntity.badRequest().body("horaInicio debe ser menor a horaFin, y los valores deben ser válidos (por ejemplo, 10:30 para 10:30)");
            }

            boolean solapado = existingHorarioDias.stream()
                    .filter(horario -> horario.getDiaSemana().equals(diaSemana))
                    .anyMatch(existingHorarioDia -> {
                        int existingHoraInicio = convertirHora(existingHorarioDia.getHoraInicio());
                        int existingHoraFin = convertirHora(existingHorarioDia.getHoraFin());
                        return horaInicio < existingHoraFin && horaFin > existingHoraInicio;
                    });

            if (solapado) {
                return ResponseEntity.badRequest().body("Horarios superpuestos detectados para el dia " + diaSemana);
            }
        }

        HorarioAsignatura horarioAsignatura = createAndSaveHorarioAsignatura(asignatura, dtNuevoHorarioAsignatura.getAnio());
        createAndSaveHorarioDias(horarioAsignatura, dtNuevoHorarioAsignatura.getDtHorarioDias());
        createAndSaveDocenteHorarioAsignatura(docente, horarioAsignatura);

        return ResponseEntity.ok("Horarios registered successfully");
    }


    public String validateInscripcionAsignatura(DtNuevaInscripcionAsignatura inscripcion) {
        Asignatura asignatura = asignaturaRepo.findById(inscripcion.getIdAsignatura()).orElse(null);
        HorarioAsignatura horario = horarioAsignaturaRepo.findById(inscripcion.getIdHorario()).orElse(null);
        Usuario usuario = usuarioRepo.findById(inscripcion.getIdEstudiante()).orElse(null);

        // Validaciones básicas
        if (asignatura == null) {
            return "La asignatura no existe";
        }
        if (horario == null) {
            return "El horario no existe";
        }
        if (usuario == null) {
            return "El usuario no existe";
        }
        if (!"E".equals(usuario.getRol())) {
            return "El usuario no es un estudiante";
        }
        if (!horario.getAsignatura().getIdAsignatura().equals(asignatura.getIdAsignatura())) {
            return "El horario no pertenece a la asignatura seleccionada";
        }
        Carrera carrera = asignatura.getCarrera();
        InscripcionCarrera inscripcionCarrera = inscripcionCarreraRepo.findByUsuarioAndCarreraAndActivaAndValidada(usuario, carrera, true, true).orElse(null);

        if (inscripcionCarrera == null) {
            return "El usuario no está inscripto en la carrera correspondiente a la asignatura";
        }

        List<EstudianteCursada> listCursadas = estudianteCursadaRepo.findByEstudianteAndAsignatura(usuario, asignatura);
        List<Cursada> aprobadasCursadas = listCursadas.stream()
                .map(EstudianteCursada::getCursada)
                .filter(cursada -> cursada.getResultado() == ResultadoAsignatura.EXONERADO)
                .toList();

        if (!aprobadasCursadas.isEmpty()) {
            return "La asignatura ya fue aprobada!";
        }

        // Realizar validación de inscripción pendiente
        List<Cursada> inscripcionPendiente = listCursadas.stream()
                .map(EstudianteCursada::getCursada)
                .filter(cursada -> cursada.getResultado() == ResultadoAsignatura.PENDIENTE)
                .toList();

        if (!inscripcionPendiente.isEmpty()) {
            return "Tiene una inscripción pendiente.";
        }

        // Realizar validación de previas
        List<Previaturas> previas = previaturasRepo.findByAsignatura(asignatura);
        List<Asignatura> asignaturasPrevias = previas.stream()
                .map(Previaturas::getPrevia)
                .toList();

        // Para cada previa, obtener todas las cursadas y validar si alguna de ellas fue aprobada
        boolean previasAprobadas = asignaturasPrevias.stream()
                .allMatch(previa -> {
                    List<EstudianteCursada> cursadasPrevia = estudianteCursadaRepo.findByEstudianteAndAsignatura(usuario, previa);
                    return cursadasPrevia.stream()
                            .map(EstudianteCursada::getCursada)
                            .anyMatch(cursada -> cursada.getResultado() == ResultadoAsignatura.EXONERADO);
                });

        if (!previasAprobadas) {
            return "No se han aprobado todas las asignaturas previas requeridas.";
        }
        return null;
    }

    public ResponseEntity<?> inscripcionAsignatura(DtNuevaInscripcionAsignatura inscripcion) {
        Asignatura asignatura = asignaturaRepo.findById(inscripcion.getIdAsignatura()).orElse(null);
        HorarioAsignatura horario = horarioAsignaturaRepo.findById(inscripcion.getIdHorario()).orElse(null);
        Usuario user = usuarioRepo.findById(inscripcion.getIdEstudiante()).orElse(null);

        Cursada cursada = new Cursada();
        cursada.setAsignatura(asignatura);
        cursada.setHorarioAsignatura(horario);
        cursada.setResultado(ResultadoAsignatura.PENDIENTE);
        cursadaRepo.save(cursada);

        EstudianteCursada estudianteCursada = new EstudianteCursada();
        estudianteCursada.setCursada(cursada);
        estudianteCursada.setUsuario(user);
        estudianteCursadaRepo.save(estudianteCursada);

        return ResponseEntity.ok().body("Se realizó la inscripcion a la asignatura");
    }

    private HorarioAsignatura createAndSaveHorarioAsignatura(Asignatura asignatura, Integer anio) {
        HorarioAsignatura horarioAsignatura = new HorarioAsignatura();
        horarioAsignatura.setAsignatura(asignatura);
        horarioAsignatura.setAnio(anio);
        return horarioAsignaturaRepo.save(horarioAsignatura);
    }

    private void createAndSaveHorarioDias(HorarioAsignatura horarioAsignatura, List<DtHorarioDias> dtHorarioDiasList) {
        for (DtHorarioDias dtHorarioDias : dtHorarioDiasList) {
            HorarioDias horarioDias = new HorarioDias();
            horarioDias.setDiaSemana(dtHorarioDias.getDiaSemana());
            horarioDias.setHoraInicio(dtHorarioDias.getHoraInicio());
            horarioDias.setHoraFin(dtHorarioDias.getHoraFin());
            horarioDias.setHorarioAsignatura(horarioAsignatura);
            horarioDiasRepo.save(horarioDias);
        }
    }

    private void createAndSaveDocenteHorarioAsignatura(Docente docente, HorarioAsignatura horarioAsignatura) {
        DocenteHorarioAsignatura docenteHorarioAsignatura = new DocenteHorarioAsignatura();
        docenteHorarioAsignatura.setDocente(docente);
        docenteHorarioAsignatura.setHorarioAsignatura(horarioAsignatura);
        docenteHorarioAsignaturaRepo.save(docenteHorarioAsignatura);
    }

    private boolean esHoraValida(String horaStr) {
        String[] partes = horaStr.split(":");
        if (partes.length != 2) {
            return false;
        }

        try {
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);

            return horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int convertirHora(String horaStr) {
        String[] partes = horaStr.split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);
        return horas * 60 + minutos;
    }

    public ResponseEntity<?> registrarPreviaturas(Integer idAsignatura, List<Integer> nuevasPrevias) {
        Asignatura asignatura = asignaturaRepo.findById(idAsignatura).orElse(null);
        if (asignatura == null) {
            return ResponseEntity.badRequest().body("Asignatura no encontrada.");
        }

        // Obtener previaturas existentes de la asignatura
        List<Previaturas> previaturasAsignatura = previaturasRepo.findByAsignatura(asignatura);
        Set<Integer> previasExistentes = previaturasAsignatura.stream()
                .map(previatura -> previatura.getPrevia().getIdAsignatura())
                .collect(Collectors.toSet());

        // Agregar nuevas previaturas y evitar duplicados
        Set<Integer> previasCompletas = new HashSet<>(previasExistentes);
        previasCompletas.addAll(nuevasPrevias);

        // Evitar que una asignatura se inscriba a sí misma
        previasCompletas.add(idAsignatura);

        List<Asignatura> previasAsignaturas = previasCompletas.stream()
                .map(asignaturaRepo::findById)
                .map(optionalAsignatura -> optionalAsignatura.orElse(null))
                .collect(Collectors.toList());

        if (previasAsignaturas.contains(null)) {
            return ResponseEntity.badRequest().body("Una o más previas no encontradas.");
        }

        // Verificar la circularidad en todas las previaturas combinadas
        boolean circularidad = validarCircularidad(idAsignatura, nuevasPrevias);
        if (circularidad) {
            return ResponseEntity.badRequest().body("Existen circularidades en las previaturas seleccionadas.");
        }

        // Guardar nuevas previaturas
        for (Integer idPrevia : nuevasPrevias) {
            Asignatura previa = asignaturaRepo.findById(idPrevia).orElse(null);
            if (previa == null) {
                return ResponseEntity.badRequest().body("Previa asignatura no encontrada.");
            }

            // Solo guardar las nuevas previaturas que no existen actualmente
            if (!previasExistentes.contains(idPrevia)) {
                Previaturas previatura = new Previaturas();
                previatura.setAsignatura(asignatura);
                previatura.setPrevia(previa);
                previaturasRepo.save(previatura);
            }
        }

        return ResponseEntity.ok().body("Previaturas registradas exitosamente.");
    }

    private boolean validarCircularidad(Integer idAsignatura, List<Integer> nuevasPrevias) {
        Set<Integer> visitado = new HashSet<>();
        Set<Integer> stack = new HashSet<>();

        // Verificar todas las nuevas previas para detectar ciclos
        for (Integer idPrevia : nuevasPrevias) {
            if (esCiclico(idAsignatura, idPrevia, visitado, stack)) {
                return true;
            }
        }

        return false;
    }

    private boolean esCiclico(Integer idAsignatura, Integer idPrevia, Set<Integer> visitado, Set<Integer> stack) {
        if (stack.contains(idPrevia)) {
            return true; // Ciclo detectado
        }

        if (visitado.contains(idPrevia)) {
            return false;
        }

        visitado.add(idPrevia);
        stack.add(idPrevia);

        // Simular la existencia de la nueva previa
        if (idPrevia.equals(idAsignatura)) {
            return true; // Ciclo detectado con la asignatura principal
        }

        Asignatura asignatura = asignaturaRepo.findById(idPrevia).orElse(null);
        if (asignatura != null) {
            List<Previaturas> previaturas = previaturasRepo.findByAsignatura(asignatura);
            for (Previaturas previatura : previaturas) {
                if (esCiclico(idAsignatura, previatura.getPrevia().getIdAsignatura(), visitado, stack)) {
                    return true;
                }
            }
        }

        stack.remove(idPrevia);
        return false;
    }

    public List<DtCursadaPendiente> getCursadasPendientesByAnioAndAsignatura(Integer anio, Integer idAsignatura) {
        return cursadaRepo.findCursadasPendientesByAnioAndAsignatura(anio, idAsignatura, ResultadoAsignatura.PENDIENTE);
    }

    public ResponseEntity<?> modificarResultadoCursada(Integer idCursada, ResultadoAsignatura nuevoResultado) {
        Cursada cursada = cursadaRepo.findById(idCursada)
                .orElse(null) ;

        if (cursada == null) {
            return ResponseEntity.badRequest().body("No se encontró la cursada.");
        }

        cursada.setResultado(nuevoResultado);
        cursadaRepo.save(cursada);

        return ResponseEntity.ok().body("Resultado de la cursada con ID " + idCursada + " cambiado exitosamente a " + nuevoResultado);
    }
}
