package Group4.StudyHubBackendG4.utils.enums;

import lombok.Getter;

@Getter
public enum ResultadoAsignatura {
    EXONERADO("EXONERADO"),
    EXAMEN("EXAMEN"),
    RECURSA("RECURSA"),
    PENDIENTE("PENDIENTE");

    private final String nombre;

    ResultadoAsignatura(String nombre) {
        this.nombre = nombre;
    }

    public static ResultadoAsignatura doyResultadoPorCalificacion(int calificacion) {
        if (calificacion >= 1 && calificacion <= 3) {
            return ResultadoAsignatura.RECURSA;
        } else if (calificacion >= 4 && calificacion <= 5) {
            return ResultadoAsignatura.EXAMEN;
        } else if (calificacion >= 6 && calificacion <= 12) {
            return ResultadoAsignatura.EXONERADO;
        } else {
            return ResultadoAsignatura.PENDIENTE;
        }
    }
}
