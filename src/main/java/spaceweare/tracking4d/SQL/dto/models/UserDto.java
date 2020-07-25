package drii.pingeso.backend.SQL.dto.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Date;

@Data
@JsonIgnoreProperties({"saleList", "entryList", "password"})
public class UserDto {
  private Integer id;
  private String firstName;
  private String lastName;
  private String userName;
  private String password;
  private Date birthDate;
  private Integer points;
  private Boolean active;
  //private List<Sale> saleList;
}