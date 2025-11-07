package in.co.lazylan.bootblog.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class JwtResponseDTO {
    private String token;
    private UserResponseDTO user;
}
