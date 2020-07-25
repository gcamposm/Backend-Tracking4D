package spaceweare.tracking4d.SQL.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Relations
    @ManyToOne
    @JoinColumn
    private Country country;

    @ManyToOne
    @JoinColumn
    private Region region;

    @ManyToOne
    @JoinColumn
    private Commune commune;

    @ManyToOne
    @JoinColumn
    private City city;

    @ManyToOne
    @JoinColumn
    private Company company;

    @ManyToOne
    @JoinColumn
    private Person person;

    //Other attributes
    private String streetName;
    private String directionNumber;
    private String complement;
    //Times
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
}