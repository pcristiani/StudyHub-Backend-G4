package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.DtAsignatura;
import Group4.StudyHubBackendG4.datatypes.DtCarrera;
import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.services.AsignaturaService;
import Group4.StudyHubBackendG4.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    @GetMapping("/getAllCarreras")
    public List<DtAsignatura> getAllAsignaturas() {
        return asignaturaService.getAllAsignaturas();
    }

}
