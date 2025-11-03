package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.UserDto;
import in.co.lazylan.bootblog.util.FieldValueExists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends FieldValueExists {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, String id) throws ResourceNotFoundException;

    UserDto getUserById(String id) throws ResourceNotFoundException;

    UserDto getUserByEmail(String email) throws ResourceNotFoundException;

    List<UserDto> getAllUsers();

    void deleteUserById(String id) throws ResourceNotFoundException;
}
