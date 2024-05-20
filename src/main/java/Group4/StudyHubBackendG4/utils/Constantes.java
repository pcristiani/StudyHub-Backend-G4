package Group4.StudyHubBackendG4.utils;

import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass

public final class Constantes {

    public static final Map<Integer, String> DIAS_DE_LA_SEMANA;
    public static final Map<String, Integer> DIAS_DE_LA_SEMANA_INVERTIDO;


    static {
        Map<Integer, String> dias = new HashMap<>();
        dias.put(1, "Lunes");
        dias.put(2, "Martes");
        dias.put(3, "Miércoles");
        dias.put(4, "Jueves");
        dias.put(5, "Viernes");
        dias.put(6, "Sábado");
        dias.put(7, "Domingo");
        DIAS_DE_LA_SEMANA = Collections.unmodifiableMap(dias);

        Map<String, Integer> diasInvertido = new HashMap<>();
        dias.forEach((key, value) -> diasInvertido.put(value, key));
        DIAS_DE_LA_SEMANA_INVERTIDO = Collections.unmodifiableMap(diasInvertido);
    }

    // Other constants
    public static final String ASIGNATURA_EXISTE = "La asignatura ya existe en la misma carrera.";
    public static final String CARRERA_NO_ENCONTRADA = "Carrera no encontrada.";
    public static final String ASIGNATURA_CREADA = "Asignatura creada exitosamente.";
    public static final String PREVIA_NO_ENCONTRADA = "Previa asignatura no encontrada";
    public static final String CICLO_DETECTADO = "Existen circularidades en las previaturas seleccionadas.";

    // Add more constants as needed
}

