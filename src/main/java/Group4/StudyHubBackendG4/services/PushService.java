package Group4.StudyHubBackendG4.services;

import Group4.StudyHubBackendG4.persistence.Usuario;
import Group4.StudyHubBackendG4.persistence.UsuarioTR;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioTrRepo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushService {
    @Autowired
    UsuarioRepo usuarioRepo;
    @Autowired
    UsuarioTrRepo usuarioTrRepo;
    public void sendPushNotification(Integer idUsuario, String mensaje, String titulo) {
        //Usuario user = usuarioRepo.findById(idUsuario)
                //.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        //UsuarioTR userTokens = usuarioTrRepo.findByUsuario(user);
        //String token = userTokens.getMobileToken();
        String token = "euDrRKuFRj-zgMBrTjr7Xj:APA91bFz6oYI3RoCwXq5T_6LScQ87FSYJQo9Y9TZZnbcS14jSCjoLGuyi_AyrkZ0-AyGD2EQt_qd8ToTC7HcqCkGp-sglgprJzP7lXIEuX5Bfec7AAd6TCpbrN6z_S359ERT-Qz7dH6Z";
        if (!token.isEmpty()){
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(titulo)
                            .setBody(mensaje)
                            .build())
                    .setToken(token)
                    .build();
            try {
                String response = FirebaseMessaging.getInstance().send(message);
                System.out.println("Mensaje enviado correctamente: " + response);
            } catch (Exception e) {
                System.err.println("Error enviando el mensaje: " + e.getMessage());
            }
        }
    }
}
