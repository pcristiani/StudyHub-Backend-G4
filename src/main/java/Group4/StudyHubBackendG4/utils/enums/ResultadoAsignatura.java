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

}
