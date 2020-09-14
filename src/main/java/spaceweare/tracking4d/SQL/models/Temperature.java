package spaceweare.tracking4d.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //Relations

    @OneToMany(mappedBy = "temperature", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Pixel> pixels = new ArrayList<>();
    //Attributes
    private Float value;
    private LocalDateTime detectedHour;
}