package spaceweare.tracking4d.SQL.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //Relations
    @ManyToOne
    @JoinColumn
    private Person person;
    //Attributes
    private String temperature;
    private LocalDateTime date;
    private Boolean active;
}