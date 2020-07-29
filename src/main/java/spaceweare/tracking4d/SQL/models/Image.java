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
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relations
    @ManyToOne
    @JoinColumn
    private Customer customer;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Detection> detections = new ArrayList<>();
    //Other attributes
    private Boolean principal;
    private String name;
    private String extension;
    private String path;
}