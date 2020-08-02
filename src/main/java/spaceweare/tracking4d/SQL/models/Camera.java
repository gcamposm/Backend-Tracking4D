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
public class Camera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String value;

    @OneToMany(mappedBy = "camera", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @JsonIgnore
    private List<Match> matchList = new ArrayList<>();

    @OneToMany(mappedBy = "camera", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @JsonIgnore
    private List<Image> images = new ArrayList<>();
}