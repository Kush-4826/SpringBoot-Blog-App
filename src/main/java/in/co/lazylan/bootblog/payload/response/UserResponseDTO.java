package in.co.lazylan.bootblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDTO {
    private int id;
    private String username;
    private String email;
    private String gender;
    private String about;
}
