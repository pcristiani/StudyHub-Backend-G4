package Group4.StudyHubBackendG4.utils;

import Group4.StudyHubBackendG4.persistence.Actividad;
import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.services.ActividadService;
import Group4.StudyHubBackendG4.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Component
public class ActivityInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ActividadService actividadService;

    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // No need to process anything here for logging purposes
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // No need to process anything here for logging purposes
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(!request.getRequestURI().startsWith("/api/")){
            return;
        }
        if (response.getStatus() == HttpServletResponse.SC_OK) {
            String token = request.getHeader("Authorization");

            if (token != null && token.startsWith(TOKEN_PREFIX)) {
                token = token.substring(7);
                String cedula = jwtUtil.getCedulaFromToken(token);

                if (cedula != null) {
                    Usuario usuario = usuarioService.getUsuarioByCedula(cedula);
                    if (usuario != null) {
                        String actionDescription = ActionMapping.getActionDescription(request.getMethod(), request.getRequestURI());
                        if(!actionDescription.isEmpty()){
                            Actividad actividad = new Actividad();
                            actividad.setUsuario(usuario);
                            actividad.setFechaHora(LocalDateTime.now());
                            actividad.setAccion(actionDescription);
                            actividadService.save(actividad);
                        }
                    }
                }
            }
        }
    }
}
