package in.co.lazylan.bootblog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto {
    private String id;

    private String title;

    private String slug;

    private String content;

    private String imageName;

    private LocalDate createdDate;

    private CategoryDto category;

    private UserDto author;
}
