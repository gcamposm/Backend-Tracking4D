package spaceweare.tracking4d.SQL.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //Relations
    @ManyToOne
    @JoinColumn
    private Person person;
    //Attributes
    private Float longitude;
    private Float latitude;
}