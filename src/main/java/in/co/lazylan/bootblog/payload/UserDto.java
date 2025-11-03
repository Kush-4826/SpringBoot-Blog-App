package in.co.lazylan.bootblog.payload;

import in.co.lazylan.bootblog.service.impl.UserServiceImpl;
import in.co.lazylan.bootblog.validator.annotation.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String id;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, message = "Username must be at least 3 characters long")
    @Unique(service = UserServiceImpl.class, fieldName = "username", message = "Username already exists")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Field must be a valid Email")
    @Unique(service = UserServiceImpl.class, fieldName = "email", message = "Email already exists")
    private String email;

    @NotEmpty(message = "Gender cannot be empty")
    private String gender;

    private String about;
}
