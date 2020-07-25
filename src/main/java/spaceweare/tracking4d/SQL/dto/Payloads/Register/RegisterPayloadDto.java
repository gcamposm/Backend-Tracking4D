package spaceweare.tracking4d.SQL.dto.Payloads.Register;

import lombok.Data;

@Data
public class RegisterPayloadDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String rut;
    private String region;
    private String country;
    private String commune;
    private String streetName;
    private String directionNumber;
    private String complement;
    private String password;
}