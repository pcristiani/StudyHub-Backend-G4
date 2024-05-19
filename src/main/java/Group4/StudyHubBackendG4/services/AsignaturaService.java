package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtAsignatura;
import Group4.StudyHubBackendG4.persistence.Asignatura;
import Group4.StudyHubBackendG4.persistence.Previaturas;
import Group4.StudyHubBackendG4.repositories.AsignaturaRepo;
import Group4.StudyHubBackendG4.repositories.PreviaturasRepo;
import Group4.StudyHubBackendG4.utils.converters.AsignaturaConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepo asignaturaRepo;

    @Autowired
    private PreviaturasRepo previaturasRepo;

    @Autowired
    private AsignaturaConverter asignaturaConverter;

    public ResponseEntity<?> altaAsignatura(DtAsignatura dtAsignatura) {

        if(asignaturaRepo.existsByNombre(dtAsignatura.getNombre())){
            return ResponseEntity.badRequest().body("La asignatura ya existe.");
        }

        if(this.validarCircularidad(dtAsignatura)){
            return ResponseEntity.badRequest().body("Existen circularidades en las previaturas seleccionadas.");
        }

        Asignatura asignatura = asignaturaConverter.convertToEntity(dtAsignatura);
        asignaturaRepo.save(asignatura);

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

    private Boolean validarCircularidad(DtAsignatura dtAsignatura) {
        List<Integer> idsPreviaturas = dtAsignatura.getPreviaturas();
        if (idsPreviaturas == null || idsPreviaturas.isEmpty()) {
            return false;
        }

        Set<Integer> visited = new HashSet<>();
        Set<Integer> stack = new HashSet<>();

        for (Integer idPrevia : idsPreviaturas) {
            if (isCyclic(idPrevia, visited, stack)) {
                return true;
            }
        }

        return false;
    }

    private boolean isCyclic(Integer idAsignatura, Set<Integer> visited, Set<Integer> stack) {
        if (stack.contains(idAsignatura)) {
            return true; // Cycle detected
        }

        if (visited.contains(idAsignatura)) {
            return false;
        }

        visited.add(idAsignatura);
        stack.add(idAsignatura);


        List<Previaturas> previaturas = previaturasRepo.findByAsignatura(asignaturaRepo.findById(idAsignatura));
        for (Previaturas previatura : previaturas) {
            if (isCyclic(previatura.getPrevia().getIdAsignatura(), visited, stack)) {
                return true;
            }
        }

        stack.remove(idAsignatura);
        return false;
    }
}
