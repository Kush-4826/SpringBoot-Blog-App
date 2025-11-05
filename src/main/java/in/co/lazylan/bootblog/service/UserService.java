package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.request.UserRequestDTO;
import in.co.lazylan.bootblog.payload.response.UserResponseDTO;
import in.co.lazylan.bootblog.util.FieldValueExists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends FieldValueExists {
    UserResponseDTO createUser(UserRequestDTO userDto);

    UserResponseDTO updateUser(UserRequestDTO userDto, int id) throws ResourceNotFoundException;

    UserResponseDTO getUserById(int id) throws ResourceNotFoundException;

    UserResponseDTO getUserByEmail(String email) throws ResourceNotFoundException;

    List<UserResponseDTO> getAllUsers();

    void deleteUserById(int id) throws ResourceNotFoundException;
}
