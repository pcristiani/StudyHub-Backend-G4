package Group4.StudyHubBackendG4.utils.enums;

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

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public static DiaSemana fromId(int id) {
        for (DiaSemana dia : values()) {
            if (dia.id == id) {
                return dia;
            }
        }
        throw new IllegalArgumentException("Invalid id: " + id);
    }

    public static DiaSemana fromNombre(String nombre) {
        for (DiaSemana dia : values()) {
            if (dia.nombre.equalsIgnoreCase(nombre)) {
                return dia;
            }
        }
        throw new IllegalArgumentException("Invalid nombre: " + nombre);
    }
}