package spaceweare.tracking4d.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Descriptor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String path;

    @ManyToOne
    @JoinColumn
    private Customer customer;

    @OneToMany(mappedBy = "descriptor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Detection> detections = new ArrayList<>();
}