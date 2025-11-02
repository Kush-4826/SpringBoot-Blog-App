package in.co.lazylan.bootblog.service.impl;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.UserDto;
import in.co.lazylan.bootblog.repo.UserRepository;
import in.co.lazylan.bootblog.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        User savedUser = this.userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String id) throws ResourceNotFoundException {
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User ID", id));
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        User updatedUser = this.userRepository.save(user);
        return this.modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(String id) throws ResourceNotFoundException {
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User ID", id));
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) throws ResourceNotFoundException {
        User user = this.userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map((user) -> this.modelMapper.map(user, UserDto.class))
                .toList();
        return userDtos;
    }

    @Override
    public void deleteUserById(String id) throws ResourceNotFoundException {
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User ID", id));
        this.userRepository.delete(user);
    }
}
