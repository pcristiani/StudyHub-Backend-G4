package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.*;
import Group4.StudyHubBackendG4.utils.converters.AsignaturaConverter;
import Group4.StudyHubBackendG4.utils.converters.HorarioAsignaturaConverter;
import Group4.StudyHubBackendG4.utils.enums.DiaSemana;
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

    public List<DtAsignatura> getAsignaturas() {
        return asignaturaRepo.findAll()
                .stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DtAsignatura> getAsignaturasDeCarrera(Integer idCarrera) {
        Carrera carrera = carreraRepo.findById(idCarrera)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));

        return asignaturaRepo.findByCarrera(carrera)
                .stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public Object getAsignaturasDeCarreraConExamen(Integer idCarrera) {
        Carrera carrera = carreraRepo.findById(idCarrera)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));

        return asignaturaRepo.findByCarreraAndTieneExamen(carrera, true)
                .stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DtAsignatura> getAsignaturasAprobadas(Integer idEstudiante) {
        Usuario user = usuarioRepo.findById(idEstudiante).orElse(null) ;
        return estudianteCursadaRepo.findAprobadasByEstudiante(user)
                .stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DtAsignatura> getAsignaturasNoAprobadas(Integer idEstudiante) {
        Usuario user = usuarioRepo.findById(idEstudiante).orElse(null) ;
        return estudianteCursadaRepo.findNoAprobadasByEstudiante(user)
                .stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DtHorarioAsignatura> getHorarios(Integer id) {
        Asignatura asig = asignaturaRepo.findById(id).orElse(null);
        return asig == null ? null :
        horarioAsignaturaRepo.findByAsignatura(asig)
                .stream()
                .map(horarioAsignaturaConverter::convertToDto)
                .toList();
    }

    public ResponseEntity<?> altaAsignatura(DtNuevaAsignatura dtNuevaAsignatura) {
        // Validate Carrera existence
        Carrera carrera = carreraRepo.findById(dtNuevaAsignatura.getIdCarrera()).orElse(null);
        if (carrera == null) {
            return ResponseEntity.badRequest().body("Carrera no encontrada.");
        }

        // Validate Docentes
        List<Integer> idDocentes = dtNuevaAsignatura.getIdDocentes();
        if (idDocentes == null || idDocentes.isEmpty()) {
            return ResponseEntity.badRequest().body("Ingrese al menos un docente.");
        }

        List<Docente> docentes = idDocentes.stream()
                .map(docenteRepo::findById)
                .map(optionalDocente -> optionalDocente.orElse(null))
                .collect(Collectors.toList());

        // Check if all docentes were found
        if (docentes.contains(null)) {
            return ResponseEntity.badRequest().body("Uno o más docentes no encontrados.");
        }

        // Validate Asignatura uniqueness
        if (asignaturaRepo.existsByNombreAndCarrera(dtNuevaAsignatura.getNombre(), carrera)) {
            return ResponseEntity.badRequest().body("La asignatura ya existe.");
        }

        // Validate Previaturas for circularity
        if (this.validarCircularidad(dtNuevaAsignatura.getPreviaturas())) {
            return ResponseEntity.badRequest().body("Existen circularidades en las previaturas seleccionadas.");
        }

        // Convert and prepare Asignatura entity
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

    //TODO: Contemplar con las previaturas ya existentes en la BD
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
            Integer horaInicio = newHorarioDia.getHoraInicio();
            Integer horaFin = newHorarioDia.getHoraFin();

            if (horaInicio < 0 || horaInicio > 23 || horaFin < 0 || horaFin > 23 || horaInicio >= horaFin) {
                return ResponseEntity.badRequest().body("horaInicio debe ser menor a horaFin, y tener un valor entre 0-23");
            }

            for (HorarioDias existingHorarioDia : existingHorarioDias) {
                if (existingHorarioDia.getDiaSemana().equals(diaSemana)) {
                    if (horaInicio < existingHorarioDia.getHoraFin() && horaFin > existingHorarioDia.getHoraInicio()) {
                        return ResponseEntity.badRequest().body("Horarios superpuestos detectados para el dia " + diaSemana);
                    }
                }
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

        //Validaciones basicas
        if (asignatura == null) {
           return "La asignatura no existe";
        }
        if (horario == null) {
            return "El horario no existe";
        }
        if (usuario == null) {
            return "El usuario no existe";
        }
        if (!usuario.getRol().equals("E")) {
            return "El usuario no es un estudiante";
        }
        if (horario.getAsignatura().getIdAsignatura() != asignatura.getIdAsignatura()) {
            return "El horario no pertenece a la asignatura seleccionada";
        }
        Carrera carrera = asignatura.getCarrera();
        InscripcionCarrera inscripcionCarrera = inscripcionCarreraRepo.findByUsuarioAndCarreraAndActivaAndValidada(usuario,carrera,true,true).orElse(null);

        if (inscripcionCarrera == null) {
            return "El usuario no está inscripto en la carrera correspondiente a la asignatura";
        }
        // TODO: Realizar validacion ya cursada
        List<EstudianteCursada> listCursadas = estudianteCursadaRepo.findByEstudianteAndAsignatura(usuario, asignatura);
        List<Cursada> aprobadasCursadas = listCursadas.stream()
                .map(EstudianteCursada::getCursada)
                .filter(cursada -> "APROBADA".equals(cursada.getResultado()))
                .toList();

        if(!aprobadasCursadas.isEmpty()){
            return  "La asignatura ya fue aprobada!";
        }

        // Realizar validacion inscripcion pendiente
        List<Cursada> inscripcionPendiente = listCursadas.stream()
                .map(EstudianteCursada::getCursada)
                .filter(cursada -> "PENDIENTE".equals(cursada.getResultado()))
                .toList();

        if(!inscripcionPendiente.isEmpty()){
            return  "Tiene una inscripcion pendiente.";
        }

        // Realizar validacion previas
        List<Previaturas> previas = previaturasRepo.findByAsignatura(asignatura);
        List<Asignatura> asignaturasPrevias = previas.stream()
                .map(Previaturas::getPrevia)
                .toList();

        // Para cada previa obtengo todas las cursadas y valido si alguna de ellas fue aprobada
        boolean previasAprobadas = asignaturasPrevias.stream()
                .allMatch(previa -> {
                    List<EstudianteCursada> cursadasPrevia = estudianteCursadaRepo.findByEstudianteAndAsignatura(usuario, previa);
                    return cursadasPrevia.stream()
                            .map(EstudianteCursada::getCursada)
                            .anyMatch(cursada -> cursada.getResultado().equals("APROBADA"));
                });

        if (!previasAprobadas) {
            return "No se han aprobado todas las asignaturas previas requeridas.";
        }
        return null;
    }

    public ResponseEntity<?> inscripcionAsignatura(DtNuevaInscripcionAsignatura inscripcion) {
        // TODO: Realizar inscripcion
        Asignatura asignatura = asignaturaRepo.findById(inscripcion.getIdAsignatura()).orElse(null);
        HorarioAsignatura horario = horarioAsignaturaRepo.findById(inscripcion.getIdHorario()).orElse(null);
        Usuario user = usuarioRepo.findById(inscripcion.getIdEstudiante()).orElse(null);

        Cursada cursada = new Cursada();
        cursada.setAsignatura(asignatura);
        cursada.setHorarioAsignatura(horario);
        cursada.setResultado("PENDIENTE");
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
}
