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
        actionMap.put(Pattern.compile("GET /api/usuario/getUsuarios"), "Consulta Usuarios");
        actionMap.put(Pattern.compile("GET /api/usuario/getUsuario/\\d+"), "Consultar Usuario");
        actionMap.put(Pattern.compile("GET /api/usuario/getResumenActividad/\\d+"), "Consultar Resumen de Actividad");
        actionMap.put(Pattern.compile("GET /api/usuario/getEstudiantesPendientes"), "Consultar Estudiantes Pendientes");
        actionMap.put(Pattern.compile("GET /api/usuario/getDocentes"), "Consultar Docentes");
        actionMap.put(Pattern.compile("GET /api/estudiante/getCalificacionesExamenes/\\d+"), "Consultar Calificaciones de Exámenes del Estudiante");
        actionMap.put(Pattern.compile("GET /api/estudiante/getCalificacionesAsignaturas/\\d+"), "Consultar Calificaciones de Asignaturas del Estudiante");
        actionMap.put(Pattern.compile("GET /api/docente/getDocentesByAsignaturaId/\\d+"), "Consultar Docentes por Asignatura");
        actionMap.put(Pattern.compile("DELETE /api/usuario/bajaUsuario/\\d+"), "Baja de Usuario");
        actionMap.put(Pattern.compile("DELETE /api/docente/bajaDocente/\\d+"), "Baja de Docente");

        // Carrera Controller
        actionMap.put(Pattern.compile("PUT /api/carrera/modificarCarrera/\\d+"), "Modificar Carrera");
        actionMap.put(Pattern.compile("PUT /api/carrera/asignarCoordinadorCarrera/\\d+"), "Asignar Coordinador de Carrera");
        actionMap.put(Pattern.compile("PUT /api/carrera/acceptEstudianteCarrera"), "Aceptar Estudiante en Carrera");
        actionMap.put(Pattern.compile("POST /api/carrera/inscripcionCarrera"), "Inscripción en Carrera");
        actionMap.put(Pattern.compile("POST /api/carrera/altaPeriodoDeExamen/\\d+"), "Alta de Período de Examen");
        actionMap.put(Pattern.compile("POST /api/carrera/altaCarrera"), "Alta de Carrera");
        actionMap.put(Pattern.compile("GET /api/carrera/getPeriodosDeCarrera/\\d+"), "Consultar Períodos de Carrera");
        actionMap.put(Pattern.compile("GET /api/carrera/getInscriptosPendientes/\\d+"), "Consultar Inscritos Pendientes");
        actionMap.put(Pattern.compile("GET /api/carrera/getCarreras"), "Consultar Carreras");
        actionMap.put(Pattern.compile("GET /api/carrera/getCarrerasInscripto/\\d+"), "Consultar Carreras Inscrito por Usuario");
        actionMap.put(Pattern.compile("GET /api/carrera/getCarrerasInscripcionesPendientes"), "Consultar Carreras con Inscripciones Pendientes");
        actionMap.put(Pattern.compile("GET /api/carrera/getCarrerasConPeriodo"), "Consultar Carreras con Período");

        // Autenticación Controller
        actionMap.put(Pattern.compile("POST /iniciarSesion"), "Iniciar Sesión");
        actionMap.put(Pattern.compile("POST /cerrarSesion"), "Cerrar Sesión");

        // Examen Controller
        actionMap.put(Pattern.compile("POST /api/examen/registroAsignaturaAPeriodo"), "Registro de Asignatura a Período de Examen");
        actionMap.put(Pattern.compile("POST /api/examen/inscripcionExamen"), "Inscripción a Examen");
        actionMap.put(Pattern.compile("POST /api/examen/cambiarResultadoExamen/\\d+"), "Cambiar Resultado de Cursada");
        actionMap.put(Pattern.compile("GET /api/examen/getExamenesAsignatura/\\d+"), "Consultar Exámenes por Asignatura");
        actionMap.put(Pattern.compile("GET /api/examen/getExamenes/\\d+"), "Consultar Exámenes por Usuario");
        actionMap.put(Pattern.compile("GET /api/examen/getCursadasExamenPendientes"), "Consultar Cursadas de Examen Pendientes");
        actionMap.put(Pattern.compile("GET /api/examen/getActa"), "Consulta Acta de Examen");

        // Asignatura Controller
        actionMap.put(Pattern.compile("POST /api/asignatura/registroHorarios/\\d+"), "Registro de Horarios de Asignatura");
        actionMap.put(Pattern.compile("POST /api/asignatura/registrarPreviaturas/\\d+"), "Registrar Previaturas de Asignatura");
        actionMap.put(Pattern.compile("POST /api/asignatura/inscripcionAsignatura"), "Inscripción en Asignatura");
        actionMap.put(Pattern.compile("POST /api/asignatura/cambiarResultadoCursada/\\d+"), "Cambiar Resultado de Cursada");
        actionMap.put(Pattern.compile("POST /api/asignatura/altaAsignatura"), "Alta de Asignatura");
        actionMap.put(Pattern.compile("GET /api/asignatura/getPreviasAsignatura/\\d+"), "Consultar Previaturas de Asignatura");
        actionMap.put(Pattern.compile("GET /api/asignatura/getHorarios/\\d+"), "Consultar Horarios de Asignatura");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturas"), "Consultar Asignaturas");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasNoAprobadas/\\d+"), "Consultar Asignaturas No Aprobadas del Estudiante");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasDeEstudiante/\\d+"), "Consultar Asignaturas del Estudiante");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasDeCarreraConExamen/\\d+"), "Consultar Asignaturas de Carrera con Examen");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasDeCarrera/\\d+"), "Consultar Asignaturas de Carrera");
        actionMap.put(Pattern.compile("GET /api/asignatura/getAsignaturasAprobadas/\\d+"), "Consultar Asignaturas Aprobadas del Estudiante");
        actionMap.put(Pattern.compile("GET /api/asignatura/cursadasPendientes"), "Consultar Cursadas Pendientes");
        actionMap.put(Pattern.compile("GET /api/asignatura/getActa"), "Consulta Acta de Asignatura");

        // Simple Courses Controller
        actionMap.put(Pattern.compile("GET /course-relations"), "Consultar Relaciones de Cursos");
    }

    public static String getActionDescription(String method, String path) {
        return actionMap.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(method + " " + path).matches())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(method + " " + path);
    }
}
