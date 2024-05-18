package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.datatypes.DtNuevaCarrera;
import Group4.StudyHubBackendG4.persistence.Carrera;
import Group4.StudyHubBackendG4.repositories.CarreraRepo;
import Group4.StudyHubBackendG4.utils.converters.CarreraConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarreraService {

    @Autowired
    private CarreraRepo carreraRepo;

    @Autowired
    private CarreraConverter carreraConverter;


    public List<DtCarrera> getAllCarreras() {
        return carreraRepo.findAll().stream()
                .map(carreraConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> nuevaCarrera(DtNuevaCarrera dtNuevaCarrera) {

        Optional<Carrera> existingCarrera = Optional.ofNullable(carreraRepo.findByNombre(dtNuevaCarrera.getNombre()));
        if (existingCarrera.isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe una carrera con ese nombre.");
        }

        Carrera carrera = existingCarrera.orElseGet(Carrera::new)
                .CarreraFromDtNuevaCarrera(dtNuevaCarrera);

        carreraRepo.save(carrera);

        return ResponseEntity.ok().body("Carrera registrada con éxito.");
    }

    public ResponseEntity<?> bajaCarrera(Integer id) {
        Optional<Carrera> carreraOpt = carreraRepo.findById(id);

        if (carreraOpt.isPresent()) {
            Carrera carrera = carreraOpt.get();
            carrera.setActiva(false);
            carreraRepo.save(carrera);

            return ResponseEntity.ok().body("Carrera desactivada exitosamente.");
        }
        return ResponseEntity.badRequest().body("Id no existe.");
    }

    public ResponseEntity<?> modificarCarrera(Integer id, DtCarrera dtCarrera) {
        String message = "No se encontró carrera.";
        Optional<Carrera> carreraOptional = carreraRepo.findById(id);

        if (carreraOptional.isPresent()) {
            Carrera aux = carreraOptional.get();

            if (Objects.equals(dtCarrera.getNombre(), aux.getNombre()) || (!Objects.equals(dtCarrera.getNombre(), aux.getNombre()) && !carreraRepo.existsByNombre(dtCarrera.getNombre()))) {
                aux.setNombre(dtCarrera.getNombre() == null || dtCarrera.getNombre().isEmpty() ? aux.getNombre() : dtCarrera.getNombre());
                aux.setDescripcion(dtCarrera.getDescripcion() == null || dtCarrera.getDescripcion().isEmpty() ? aux.getDescripcion() : dtCarrera.getDescripcion());
                aux.setActiva(dtCarrera.getActiva() == null ? aux.getActiva() : dtCarrera.getActiva());

                carreraRepo.save(aux);
                return ResponseEntity.ok().body("Carrera actualizada exitosamente");
            } else {
                message = "Ya existe una carrera con ese nombre.";
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
