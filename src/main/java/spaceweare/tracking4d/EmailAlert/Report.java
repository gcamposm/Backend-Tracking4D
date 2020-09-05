package spaceweare.tracking4d.EmailAlert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Report {
    private String name;
    private String companyName;
    private String text;
    private String email;
}
