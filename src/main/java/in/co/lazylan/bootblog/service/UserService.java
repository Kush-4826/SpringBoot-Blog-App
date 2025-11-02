package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.payload.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, String id);

    UserDto getUserById(String id);

    UserDto getUserByEmail(String email);

    List<UserDto> getAllUsers();

    void deleteUserById(String id);
}
