package spaceweare.tracking4d.SQL.dto.responses;

import lombok.Data;

@Data
public class ImageResponse {
    private Integer id;
    private String name;
    private String extension;
    private Boolean principal;
    private String url;
}