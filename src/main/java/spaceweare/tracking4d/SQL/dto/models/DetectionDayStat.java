package spaceweare.tracking4d.SQL.dto.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DetectionDayStat {
    private LocalDateTime day;
    private String formattedDate;
    private Integer unknown;
    private Integer matches;
    private Integer total;
}