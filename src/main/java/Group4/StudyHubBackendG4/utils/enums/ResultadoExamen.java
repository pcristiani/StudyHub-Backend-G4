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

}
