package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.datatypes.DtInscripcionCarrera;
import Group4.StudyHubBackendG4.datatypes.DtNuevaCarrera;
import Group4.StudyHubBackendG4.persistence.Carrera;
import Group4.StudyHubBackendG4.persistence.InscripcionCarrera;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.repositories.CarreraRepo;
import Group4.StudyHubBackendG4.repositories.InscripcionCarreraRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
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
    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private InscripcionCarreraRepo inscripcionCarreraRepo;


    public List<DtCarrera> getCarreras() {
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

    public ResponseEntity<?> inscripcionCarrera(DtInscripcionCarrera dtInscripcionCarrera) {
        Optional<Carrera> carreraOptional = carreraRepo.findById(dtInscripcionCarrera.getIdCarrera());
        Optional<Usuario> estudianteOptional = usuarioRepo.findById(dtInscripcionCarrera.getIdEstudiante());

        if (estudianteOptional.isPresent()) {
            Usuario estudiante = estudianteOptional.get();
            if(!estudiante.getRol().equals("E")){
                return ResponseEntity.badRequest().body("El usuario no es un estudiante.");
            }

            if(carreraOptional.isEmpty()){
                return ResponseEntity.badRequest().body("La carrera no fue encontrada.");
            }

            // Verificar si el estudiante ya tiene una inscripción activa y no validada a esta carrera
            Optional<InscripcionCarrera> existingInscripcion = inscripcionCarreraRepo
                    .findByUsuarioAndCarreraAndActivaAndValidada(estudiante, carreraOptional.get(), true, false);

            if (existingInscripcion.isPresent()) {
                return ResponseEntity.badRequest().body("El estudiante ya tiene una inscripción activa.");
            }

        }else{
            return ResponseEntity.badRequest().body("El estudiante no fue encontrado.");
        }

        InscripcionCarrera inscripcionCarrera = new InscripcionCarrera();
        inscripcionCarrera.setCarrera(carreraOptional.get());
        inscripcionCarrera.setUsuario(estudianteOptional.get());
        inscripcionCarrera.setActiva(true);
        inscripcionCarrera.setValidada(false);
        inscripcionCarreraRepo.save(inscripcionCarrera);

        return ResponseEntity.ok().body("Inscripcion solicitada exitosamente.");
    }
}
