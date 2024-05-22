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
import org.springframework.transaction.annotation.Transactional;

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
    private AsignaturaConverter asignaturaConverter;

    @Autowired
    private HorarioAsignaturaConverter horarioAsignaturaConverter;

    public List<DtAsignatura> getAsignaturas() {
        return asignaturaRepo.findAll()
                .stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<List<DtAsignatura>> getAsignaturasDeCarrera(Integer idCarrera) {
        Carrera carrera = carreraRepo.findById(idCarrera)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));

        List<DtAsignatura> dtAsignaturas = asignaturaRepo.findByCarrera(carrera)
                .stream()
                .map(asignaturaConverter::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtAsignaturas);
    }

    public List<DtHorarioAsignatura> getHorarios(Integer id) {
        Asignatura asig = asignaturaRepo.findById(id).orElse(null);
        return asig == null ? null :
        horarioAsignaturaRepo.findByAsignatura(asig)
                .stream()
                .map(horarioAsignaturaConverter::convertToDto)
                .toList();
    }

    @Transactional
    public ResponseEntity<?> altaAsignatura(DtNuevaAsignatura dtNuevaAsignatura) {
        Carrera carrera = carreraRepo.findById(dtNuevaAsignatura.getIdCarrera()).orElse(null);

        if(carrera == null){
            return ResponseEntity.badRequest().body("Carrera no encontrada.");
        }

        List<Integer> idDocentes = dtNuevaAsignatura.getIdDocentes();
        if (idDocentes == null || idDocentes.isEmpty()) {
            return ResponseEntity.badRequest().body("Ingrese al menos un docente.");
        }

        List<Docente> docentes = dtNuevaAsignatura.getIdDocentes().stream()
                .map(docenteRepo::findById)
                .map(optionalDocente -> optionalDocente.orElse(null))
                .toList();

        if(asignaturaRepo.existsByNombreAndCarrera(dtNuevaAsignatura.getNombre(), carrera)){
            return ResponseEntity.badRequest().body("La asignatura ya existe.");
        }

        if(this.validarCircularidad(dtNuevaAsignatura.getPreviaturas())){
            return ResponseEntity.badRequest().body("Existen circularidades en las previaturas seleccionadas.");
        }

        DtAsignatura dtAsignatura = dtNuevaAsignatura.dtAsignaturaFromDtNuevaAsignatura(dtNuevaAsignatura);
        Asignatura asignatura = asignaturaConverter.convertToEntity(dtAsignatura);

        asignaturaRepo.save(asignatura);

        docentes.forEach(docente -> {
                    DocenteAsignatura da = new DocenteAsignatura();
                    da.setDocente(docente);
                    da.setAsignatura(asignatura);
                    docenteAsignaturaRepo.save(da);
                });

        List<Integer> idsPreviaturas = dtAsignatura.getPreviaturas();
        if (idsPreviaturas != null && !idsPreviaturas.isEmpty()) {
            for (Integer idPrevia : idsPreviaturas) {
                Asignatura previaAsignatura = asignaturaRepo.findById(idPrevia)
                        .orElseThrow(() -> new RuntimeException("Previa asignatura no encontrada"));
                Previaturas previatura = new Previaturas();
                previatura.setAsignatura(asignatura);
                previatura.setPrevia(previaAsignatura);
                previaturasRepo.save(previatura);
            }
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

        List<Previaturas> previaturas = previaturasRepo.findByAsignatura(asignaturaRepo.findById(idAsignatura));
        for (Previaturas previatura : previaturas) {
            if (esCiclico(previatura.getPrevia().getIdAsignatura(), visitado, stack)) {
                return true;
            }
        }

        stack.remove(idAsignatura);
        return false;
    }

    @Transactional
    public ResponseEntity<?> registroHorarios(Integer idAsignatura, DtNuevoHorarioAsignatura dtNuevoHorarioAsignatura) {
        try {
            Asignatura asignatura = asignaturaRepo.findById(idAsignatura)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid idAsignatura"));

            Docente docente = docenteRepo.findById(dtNuevoHorarioAsignatura.getIdDocente())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid idDocente"));

            // Obtener horarios para ese docente y ese anio
            List<HorarioDias> existingHorarioDias = getExistingHorarioDiasForDocenteAndAnio(docente, dtNuevoHorarioAsignatura.getAnio());

            // Validate for overlapping schedules
            validateNoOverlappingSchedules(existingHorarioDias, dtNuevoHorarioAsignatura.getDtHorarioDias());

            // Create and save HorarioAsignatura
            HorarioAsignatura horarioAsignatura = createAndSaveHorarioAsignatura(asignatura, dtNuevoHorarioAsignatura.getAnio());

            // Create and save HorarioDias
            createAndSaveHorarioDias(horarioAsignatura, dtNuevoHorarioAsignatura.getDtHorarioDias());

            // Create and save DocenteHorarioAsignatura
            createAndSaveDocenteHorarioAsignatura(docente, horarioAsignatura);

            return ResponseEntity.ok("Horarios registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private List<HorarioDias> getExistingHorarioDiasForDocenteAndAnio(Docente docente, Integer anio) {
        return docenteHorarioAsignaturaRepo.findHorarioDiasByDocenteIdAndAnio(docente.getIdDocente(), anio);
    }


    private void validateNoOverlappingSchedules(List<HorarioDias> existingHorarioDias, List<DtHorarioDias> newHorarioDias) {
        for (DtHorarioDias newHorarioDia : newHorarioDias) {
            DiaSemana diaSemana = newHorarioDia.getDiaSemana();
            Integer horaInicio = newHorarioDia.getHoraInicio();
            Integer horaFin = newHorarioDia.getHoraFin();

            if (horaInicio < 0 || horaInicio > 23 || horaFin < 0 || horaFin > 23 || horaInicio >= horaFin) {
                throw new IllegalArgumentException("Invalid hours: horaInicio should be less than horaFin and between 0 and 23");
            }

            for (HorarioDias existingHorarioDia : existingHorarioDias) {
                if (existingHorarioDia.getDiaSemana().equals(diaSemana)) {
                    if (horaInicio < existingHorarioDia.getHoraFin() && horaFin > existingHorarioDia.getHoraInicio()) {
                        throw new IllegalArgumentException("Overlapping schedule detected for " + diaSemana);
                    }
                }
            }
        }
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
