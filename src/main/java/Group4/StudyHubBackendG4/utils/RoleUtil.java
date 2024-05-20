package Group4.StudyHubBackendG4.utils;

import Group4.StudyHubBackendG4.datatypes.DtNuevoUsuario;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class RoleUtil {

    private static final Map<String, String> roleMap = new HashMap<>();
    private static final Map<String, String> reverseRoleMap = new HashMap<>();

    static {
        roleMap.put("F", "Funcionario");
        roleMap.put("E", "Estudiante");
        roleMap.put("A", "Administrador");
        roleMap.put("C", "Coordinador");

        reverseRoleMap.put("Funcionario", "F");
        reverseRoleMap.put("Estudiante", "E");
        reverseRoleMap.put("Administrador", "A");
        reverseRoleMap.put("Coordinador", "C");
    }

    public static String getRoleName(String role) {
        return roleMap.getOrDefault(role, "Unknown");
    }

    public static String getRoleAbbreviation(String roleFullName) {
        return reverseRoleMap.getOrDefault(roleFullName, null);
    }

    public Boolean isEstudiante(DtNuevoUsuario dtNuevoUsuario){
        return dtNuevoUsuario.getRol().equals("E");
    }

    public Boolean isFuncionario(DtNuevoUsuario dtNuevoUsuario){
        return dtNuevoUsuario.getRol().equals("F");
    }

    public Boolean isCoordinador(DtNuevoUsuario dtNuevoUsuario){
        return dtNuevoUsuario.getRol().equals("C");
    }

    public Boolean isAdmin(DtNuevoUsuario dtNuevoUsuario){
        return dtNuevoUsuario.getRol().equals("A");
    }
}