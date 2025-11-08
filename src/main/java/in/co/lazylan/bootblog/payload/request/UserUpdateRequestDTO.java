package in.co.lazylan.bootblog.payload.request;

import in.co.lazylan.bootblog.service.impl.UserServiceImpl;
import in.co.lazylan.bootblog.validator.annotation.UniqueExceptSelf;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserUpdateRequestDTO {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Field must be a valid Email")
    @UniqueExceptSelf(service = UserServiceImpl.class, fieldName = "email", message = "Email already exists")
    private String email;

    @NotEmpty(message = "Gender cannot be empty")
    private String gender;

    private String about;
}
