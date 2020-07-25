package drii.pingeso.backend.Security.security.RestorePassword;

import drii.pingeso.backend.SQL.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "confirmation_token")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime createdAt;

    private String token;

    @ManyToOne
    @JoinColumn
    private User user;

    public ConfirmationToken(LocalDateTime createdAt, String token, User user) {
        this.createdAt = createdAt;
        this.token = token;
        this.user = user;
    }
}