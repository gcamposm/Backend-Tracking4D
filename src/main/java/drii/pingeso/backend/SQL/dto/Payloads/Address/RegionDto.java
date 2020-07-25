package drii.pingeso.backend.SQL.dto.Payloads.Address;

import lombok.Data;
import java.util.List;

@Data
public class RegionDto {
    private String regionName;
    private String regionNumber;
    private List<String> communeListString;
}