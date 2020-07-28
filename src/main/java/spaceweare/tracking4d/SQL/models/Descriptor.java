package spaceweare.tracking4d.SQL.models;

import lombok.Data;
import javax.persistence.*;

@Data
public class Descriptor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String path;
    private Float[] descriptor;
    @ManyToOne
    @JoinColumn
    private Match match;
}