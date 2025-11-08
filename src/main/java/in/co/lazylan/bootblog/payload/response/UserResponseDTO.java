package in.co.lazylan.bootblog.payload.response;

import in.co.lazylan.bootblog.types.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDTO {
    private int id;
    private String name;
    private String email;
    private String gender;
    private String about;
    private Set<RoleType> roles;
}
