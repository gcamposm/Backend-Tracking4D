package spaceweare.tracking4d.SQL.dto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import spaceweare.tracking4d.SQL.models.Match;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime hour;
    private LocalDateTime oneHourLater;
    private String formattedDate;
    private Integer unknown;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Match> matches;
}