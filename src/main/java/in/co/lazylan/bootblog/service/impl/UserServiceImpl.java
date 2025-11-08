package in.co.lazylan.bootblog.service.impl;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.request.UserRequestDTO;
import in.co.lazylan.bootblog.payload.request.UserUpdateRequestDTO;
import in.co.lazylan.bootblog.payload.response.UserResponseDTO;
import in.co.lazylan.bootblog.repo.UserRepository;
import in.co.lazylan.bootblog.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static in.co.lazylan.bootblog.types.RoleType.ADMIN;
import static in.co.lazylan.bootblog.types.RoleType.AUTHOR;

@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(AUTHOR));
        User savedUser = this.userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO updateUser(UserUpdateRequestDTO userDto, int id, User authUser) throws ResourceNotFoundException, AccessDeniedException {
        if (!authUser.isAdmin() && id != authUser.getId()) throw new AccessDeniedException("Access denied");
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User ID", id));
        user.setEmail(userDto.getEmail());
//        user.setPassword(userDto.getPassword());
//        user.setUsername(userDto.getUsername());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        User updatedUser = this.userRepository.save(user);
        return this.modelMapper.map(updatedUser, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserById(int id, User authUser) throws ResourceNotFoundException, AccessDeniedException {
        if (id == authUser.getId()) return this.modelMapper.map(authUser, UserResponseDTO.class);
        if (!authUser.isAdmin()) throw new AccessDeniedException("Access denied");
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User ID", id));
        return this.modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) throws ResourceNotFoundException {
        User user = this.userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        return this.modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAllUsers(User authUser) throws AccessDeniedException {
        if (!authUser.isAdmin()) throw new AccessDeniedException("Access denied");
        List<User> users = this.userRepository.findAll();
        List<UserResponseDTO> userDtos = users.stream()
                .map((user) -> this.modelMapper.map(user, UserResponseDTO.class))
                .toList();
        return userDtos;
    }

    @Override
    public void deleteUserById(int id, User authUser) throws ResourceNotFoundException, AccessDeniedException {
        if (!authUser.isAdmin() && id != authUser.getId()) throw new AccessDeniedException("Access denied");
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User ID", id));
        this.userRepository.delete(user);
    }

    @Override
    public void promoteAuthorToAdmin(int userId, User authUser) throws ResourceNotFoundException, AccessDeniedException {
        if (!authUser.isAdmin()) throw new AccessDeniedException("Access denied");
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User ID", userId));

        user.getRoles().add(ADMIN);
        user.getRoles().remove(AUTHOR);
        this.userRepository.save(user);
    }

    @Override
    public void demoteAdminToAuthor(int userId, User authUser) throws ResourceNotFoundException, AccessDeniedException {
        if (!authUser.isAdmin() || authUser.getId() == userId) throw new AccessDeniedException("Access denied");
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User ID", userId));

        user.getRoles().add(AUTHOR);
        user.getRoles().remove(ADMIN);
        this.userRepository.save(user);
    }

    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        switch (fieldName) {
            case "email" -> {
                return this.userRepository.existsByEmail(value.toString());
            }

            case "username" -> {
                return this.userRepository.existsByUsername(value.toString());
            }

            default -> throw new UnsupportedOperationException(fieldName + " field does not exist");
        }
    }

    @Override
    public boolean fieldValueExistsExceptSelf(Object value, String fieldName) throws UnsupportedOperationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (fieldName.equals("email")) {
            if (user.getEmail().equalsIgnoreCase(value.toString())) return false;
            return this.userRepository.existsByEmail(value.toString());
        }
        throw new UnsupportedOperationException(fieldName + " field does not exist");
    }
}
