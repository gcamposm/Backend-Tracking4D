package spaceweare.tracking4d.SQL.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Detection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float value;
    private LocalDateTime clock;

    @ManyToOne
    @JoinColumn
    private Image image;
}
