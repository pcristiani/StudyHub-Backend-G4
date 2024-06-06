package Group4.StudyHubBackendG4.utils.enums;

import lombok.Getter;

@Getter
public enum DiaSemana {
    LUNES(1, "Lunes"),
    MARTES(2, "Martes"),
    MIERCOLES(3, "Miércoles"),
    JUEVES(4, "Jueves"),
    VIERNES(5, "Viernes"),
    SABADO(6, "Sábado"),
    DOMINGO(7, "Domingo");

    private final int id;
    private final String nombre;

    DiaSemana(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}