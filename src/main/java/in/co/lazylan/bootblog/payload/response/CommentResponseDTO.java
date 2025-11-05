package in.co.lazylan.bootblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentResponseDTO {
    private int id;
    private String content;
    UserResponseDTO user;
    private LocalDateTime publishedAt;
}
