package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtAsignatura;
import Group4.StudyHubBackendG4.datatypes.DtHorarioAsignatura;
import Group4.StudyHubBackendG4.datatypes.DtNuevaAsignatura;
import Group4.StudyHubBackendG4.persistence.*;
import Group4.StudyHubBackendG4.repositories.*;
import Group4.StudyHubBackendG4.utils.converters.AsignaturaConverter;
import Group4.StudyHubBackendG4.utils.converters.HorarioAsignaturaConverter;
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
    private DocenteAsignaturaRepo docenteAsignaturaRepo;

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

    /*No funca
    public List<HorarioAsignatura> findHorarioAsignaturasByDocenteAndAnioAndDias(Integer idAsignatura, String docenteNombre,
                                                                                 Integer anio,
                                                                                 List<Integer> dias) {
        return horarioAsignaturaRepo.findHorarioAsignaturasByDocenteAndAnioAndDiasAndAsignaturaId(idAsignatura, docenteNombre, anio, dias);
    }

     */
}
