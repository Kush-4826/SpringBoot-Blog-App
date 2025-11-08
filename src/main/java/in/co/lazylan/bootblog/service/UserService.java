package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.request.UserRequestDTO;
import in.co.lazylan.bootblog.payload.request.UserUpdateRequestDTO;
import in.co.lazylan.bootblog.payload.response.UserResponseDTO;
import in.co.lazylan.bootblog.util.FieldValueExists;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends FieldValueExists {
    UserResponseDTO createUser(UserRequestDTO userDto);

    UserResponseDTO updateUser(UserUpdateRequestDTO userDto, int id, User authUser) throws ResourceNotFoundException, AccessDeniedException;

    UserResponseDTO getUserById(int id, User authUser) throws ResourceNotFoundException, AccessDeniedException;

    UserResponseDTO getUserByEmail(String email) throws ResourceNotFoundException;

    List<UserResponseDTO> getAllUsers(User user) throws AccessDeniedException;

    void deleteUserById(int id, User user) throws ResourceNotFoundException, AccessDeniedException;

    void promoteAuthorToAdmin(int userId, User authUser) throws ResourceNotFoundException, AccessDeniedException;

    void demoteAdminToAuthor(int userId, User authUser) throws ResourceNotFoundException, AccessDeniedException;
}
