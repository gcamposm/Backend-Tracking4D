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
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relations
    @ManyToOne
    @JoinColumn
    private Person person;

    @ManyToOne
    @JoinColumn
    private Company company;

    @ManyToOne
    @JoinColumn
    private Camera camera;

    @ManyToOne
    @JoinColumn
    private Contact contact;

    @ManyToOne
    @JoinColumn
    private Temperature temperature;

    @ManyToOne
    @JoinColumn
    private Location location;

    //Other attributes
    private String name;
    private LocalDateTime hour;
    private String emotion;
}