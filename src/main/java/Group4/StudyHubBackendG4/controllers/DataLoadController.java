package Group4.StudyHubBackendG4.controllers;

import Group4.StudyHubBackendG4.datatypes.*;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.services.*;
import com.github.javafaker.Faker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Tag(name = "Datos", description = "Endpoint para la carga de Datos")
public class DataLoadController {

    @Autowired
    private DataLoadService dataLoadService;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/loadData")
    @Operation(summary = "Impacta datos de prueba en la base")
    public ResponseEntity<?> loadData() throws MessagingException, IOException {
        List<DtUsuario> usuarioList = usuarioService.getUsuarios();
        if(!usuarioList.isEmpty()) {
            return ResponseEntity.badRequest().body("Ya hay datos en la base de datos");
        }

        //Registro de usuarios.
        dataLoadService.registerUsers();

        //Crear docentes.
        dataLoadService.registerDocentes();

        //Crear carreras
        dataLoadService.registerCarreras();

        //Crear asignaturas
        dataLoadService.registerAsignaturas();

        //Crear periodos de examen
        dataLoadService.registerPeriodos();

        //Crear horarios
        dataLoadService.registerHorarios();

        //Crear examenes
        dataLoadService.registerExamenes();

        //Inscribir estudiantes a carreras
        dataLoadService.inscribirEstudiantes();

        return ResponseEntity.ok().body("Datos cargados exitosamente");
    }

}

