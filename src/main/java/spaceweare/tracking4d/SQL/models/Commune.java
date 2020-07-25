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
public class Commune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relations
    @ManyToOne
    @JoinColumn
    private Region region;

    @OneToMany(mappedBy = "commune", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @JsonIgnore
    private List<Address> addresses = new ArrayList<>();

    //Other attributes
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
}