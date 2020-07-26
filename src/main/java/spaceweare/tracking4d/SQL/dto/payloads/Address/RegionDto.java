package spaceweare.tracking4d.SQL.dto.payloads.Address;

import lombok.Data;
import java.util.List;

@Data
public class RegionDto {
    private String regionName;
    private String regionNumber;
    private List<String> communeListString;
}