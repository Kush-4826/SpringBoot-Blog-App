package in.co.lazylan.bootblog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentRequestDTO {
    @Size(min = 3, message = "Comment must be at least 3 characters")
    @NotBlank(message = "Comment cannot be blank")
    private String content;

    @NotBlank(message = "User ID is required")
    private int userId;

    @NotBlank(message = "Blog ID is required")
    private int blogId;
}
