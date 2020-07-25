package spaceweare.tracking4d.SQL.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn
    private Company company;
    //Other attributes
    private String name;
    private String extension;
}