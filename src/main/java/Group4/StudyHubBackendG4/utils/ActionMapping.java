package Group4.StudyHubBackendG4.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ActionMapping {
    private static final Map<Pattern, String> actionMap = new HashMap<>();

    static {
        // Usuario Controller
        actionMap.put(Pattern.compile("PUT /api/usuario/modificarUsuario/\\d+"), "Modifica un Usuario");
        actionMap.put(Pattern.compile("PUT /api/usuario/modificarPerfil/\\d+"), "Modificar Perfil de Usuario");
        actionMap.put(Pattern.compile("PUT /api/usuario/modificarPassword/\\d+"), "Modificar Contraseña de Usuario");
        actionMap.put(Pattern.compile("PUT /api/usuario/acceptEstudiante/\\d+"), "Aceptar Estudiante");
        actionMap.put(Pattern.compile("PUT /api/docente/modificarDocente/\\d+"), "Modificar Docente");
        actionMap.put(Pattern.compile("POST /registerUsuario"), "Registrar Usuario");
        actionMap.put(Pattern.compile("POST /recuperarPassword"), "Recuperar Contraseña");
        actionMap.put(Pattern.compile("POST /forgotPassword"), "Olvido Contraseña");
        actionMap.put(Pattern.compile("POST /api/docente/altaDocente"), "Alta de Docente");
        actionMap.put(Pattern.compile("GET /api/usuario/getUsuarios"), "");
        actionMap.put(Pattern.compile("GET /api/usuario/getUsuario/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/usuario/getResumenActividad/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/usuario/getEstudiantesPendientes"), "");
        actionMap.put(Pattern.compile("GET /api/usuario/getDocentes"), "");
        actionMap.put(Pattern.compile("GET /api/estudiante/getCalificacionesExamenes/\\d+"), "Consultar Calificaciones de Exámenes del Estudiante");
        actionMap.put(Pattern.compile("GET /api/estudiante/getCalificacionesAsignaturas/\\d+"), "Consultar Calificaciones de Asignaturas del Estudiante");
        actionMap.put(Pattern.compile("GET /api/docente/getDocentesByAsignaturaId/\\d+"), "");
        actionMap.put(Pattern.compile("DELETE /api/usuario/bajaUsuario/\\d+"), "Baja de Usuario");
        actionMap.put(Pattern.compile("DELETE /api/docente/bajaDocente/\\d+"), "Baja de Docente");
        actionMap.put(Pattern.compile("POST /api/usuario/registerMobileToken/\\d+"), "");


        // Carrera Controller
        actionMap.put(Pattern.compile("PUT /api/carrera/modificarCarrera/\\d+"), "Modificar Carrera");
        actionMap.put(Pattern.compile("PUT /api/carrera/asignarCoordinadorCarrera/\\d+"), "Asignar Coordinador de Carrera");
        actionMap.put(Pattern.compile("PUT /api/carrera/acceptEstudianteCarrera"), "Aceptar Estudiante en Carrera");
        actionMap.put(Pattern.compile("POST /api/carrera/inscripcionCarrera"), "Inscripción en Carrera");
        actionMap.put(Pattern.compile("POST /api/carrera/altaPeriodoDeExamen/\\d+"), "Alta de Período de Examen");
        actionMap.put(Pattern.compile("POST /api/carrera/altaCarrera"), "Alta de Carrera");
        actionMap.put(Pattern.compile("GET /api/carrera/getPeriodosDeCarrera/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/carrera/getInscriptosPendientes/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/carrera/getCarreras"), "");
        actionMap.put(Pattern.compile("GET /api/carrera/getCarreraById/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/carrera/getCarrerasInscripto/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/carrera/getCarrerasInscripcionesPendientes"), "");
        actionMap.put(Pattern.compile("GET /api/carrera/getCarrerasConPeriodo"), "");
        actionMap.put(Pattern.compile("GET /api/carrera/getPreviaturasGrafo"), "");
        actionMap.put(Pattern.compile("GET /api/carrera/getCarrerasCoordinador/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/carrera/getPreviaturasGrafo/\\d+"), "");


        // Autenticación Controller
        actionMap.put(Pattern.compile("POST /iniciarSesion"), "Iniciar Sesión");
        actionMap.put(Pattern.compile("POST /cerrarSesion"), "Cerrar Sesión");

        // Examen Controller
        actionMap.put(Pattern.compile("POST /api/examen/registroAsignaturaAPeriodo"), "Registro de Asignatura a Período de Examen");
        actionMap.put(Pattern.compile("POST /api/examen/inscripcionExamen"), "Inscripción a Examen");
        actionMap.put(Pattern.compile("POST /api/examen/cambiarResultadoExamen/\\d+"), "Cambiar Resultado de Cursada");
        actionMap.put(Pattern.compile("GET /api/examen/getExamenesAsignatura/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/examen/getExamenes/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/examen/getActa/\\d+"), "Obtener acta de Examen");
        actionMap.put(Pattern.compile("GET /api/examen/getExamenesPeriodo/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/examen/getExamenesAsignaturaPorAnio/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/examen/getCursadasExamen/\\d+"), "");

        // Asignatura Controller
        actionMap.put(Pattern.compile("POST /api/asignatura/registroHorarios/\\d+"), "Registro de Horarios de Asignatura");
        actionMap.put(Pattern.compile("POST /api/asignatura/registrarPreviaturas/\\d+"), "Registrar Previaturas de Asignatura");
        actionMap.put(Pattern.compile("POST /api/asignatura/inscripcionAsignatura"), "Inscripción en Asignatura");
        actionMap.put(Pattern.compile("POST /api/asignatura/cambiarResultadoCursada/\\d+"), "Cambiar Resultado de Cursada");
        actionMap.put(Pattern.compile("POST /api/asignatura/altaAsignatura"), "Alta de Asignatura");
        actionMap.put(Pattern.compile("GET /api/asignatura/getPreviasAsignatura/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/getHorarios/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturas"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturaById/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasNoAprobadas/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasDeEstudiante/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasDeCarreraConExamen/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasDeCarrera/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasAprobadas/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/cursadasPendientes"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/getActa/\\d+"), "Obtener acta de Asignatura");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasConExamenPendiente/\\d+"), "");
        actionMap.put(Pattern.compile("GET /api/asignatura/getNoPreviasAsignatura/\\d+"), "");

        // Simple Courses Controller
        actionMap.put(Pattern.compile("GET /course-relations"), "");
    }

    public static String getActionDescription(String method, String path) {
        return actionMap.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(method + " " + path).matches())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(method + " " + path);
    }
}
