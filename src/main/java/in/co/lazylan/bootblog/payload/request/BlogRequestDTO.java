package in.co.lazylan.bootblog.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlogRequestDTO {
    private String title;
    private String content;
    private String imageName;
}
