package in.co.lazylan.bootblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlogResponseDTO {
    private String id;

    private String title;

    private String slug;

    private String content;

    private String imageName;

    private LocalDate createdDate;

    private CategoryResponseDTO category;

    private UserResponseDTO author;
}
