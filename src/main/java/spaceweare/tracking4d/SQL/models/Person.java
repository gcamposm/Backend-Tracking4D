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
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relations
    @ManyToOne
    @JoinColumn
    private Company company;

    @ManyToOne
    @JoinColumn
    private PersonType type;

    @ManyToOne
    @JoinColumn
    private PersonPosition position;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @JsonIgnore
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @JsonIgnore
    private List<Match> matches = new ArrayList<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @JsonIgnore
    private List<Address> addresses = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    private User user;

    //Other attributes
    private String firstName;
    private String lastName;
    private String rut;
    private String phoneNumber;
    private String mail;
    private String genre;
    private Boolean unknown;
    private Boolean deleted;
    private Boolean actual;
    private Boolean toTrain;
    private Boolean covid;
    private Boolean newAlert;
    private String activity;
}