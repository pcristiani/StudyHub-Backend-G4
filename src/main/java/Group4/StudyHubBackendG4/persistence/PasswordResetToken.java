package Group4.StudyHubBackendG4.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PasswordResetToken")
@Data
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    private LocalDateTime expiryDateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
