package Group4.StudyHubBackendG4.utils.enums;

import lombok.Getter;

@Getter
public enum ResultadoExamen {
    APROBADO("APROBADO"),
    REPROBADO("REPROBADO"),
    PENDIENTE("PENDIENTE");

    private final String nombre;

    ResultadoExamen(String nombre) {
        this.nombre = nombre;
    }

    public static ResultadoExamen doyResultadoPorCalificacion(int calificacion) {
        if (calificacion >= 5) {
            return ResultadoExamen.APROBADO;
        } else if (calificacion >= 1) {
            return ResultadoExamen.REPROBADO;
        } else {
            return ResultadoExamen.PENDIENTE;
        }
    }
}
