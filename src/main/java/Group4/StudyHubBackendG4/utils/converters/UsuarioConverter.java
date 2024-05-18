package Group4.StudyHubBackendG4.utils.converters;

import Group4.StudyHubBackendG4.datatypes.DtUsuario;
import Group4.StudyHubBackendG4.persistence.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioConverter extends AbstractGenericConverter<Usuario, DtUsuario> {
    public UsuarioConverter() {
        super(Usuario.class, DtUsuario.class);
    }
}


