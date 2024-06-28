package Group4.StudyHubBackendG4.datatypes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DtFecha {
    private Integer anio;
    private Integer mes;
    private Integer dia;

    public LocalDate convertToLocalDate() {
        return LocalDate.of(anio, mes, dia);
    }

}
