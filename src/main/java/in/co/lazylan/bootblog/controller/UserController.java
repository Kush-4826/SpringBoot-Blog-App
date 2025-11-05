package in.co.lazylan.bootblog.controller;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.request.UserRequestDTO;
import in.co.lazylan.bootblog.payload.response.UserResponseDTO;
import in.co.lazylan.bootblog.response.SuccessResponse;
import in.co.lazylan.bootblog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userServiceImpl;

    @Autowired
    public UserController(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

//    @PostMapping("")
//    public ResponseEntity<UserResponseDTO> store(@Valid @RequestBody UserRequestDTO userDto) {
//        UserResponseDTO user = this.userServiceImpl.createUser(userDto);
//        return new ResponseEntity<>(user, HttpStatus.CREATED);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@Valid @RequestBody UserRequestDTO userDto, @PathVariable int id) throws ResourceNotFoundException {
        UserResponseDTO updatedUser = this.userServiceImpl.updateUser(userDto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) throws ResourceNotFoundException {
        this.userServiceImpl.deleteUserById(id);
        return new ResponseEntity<>(
                new SuccessResponse("User with id " + id + " Deleted Successfully"),
                HttpStatus.OK
        );
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponseDTO>> index() {
        List<UserResponseDTO> users = this.userServiceImpl.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> show(@PathVariable int id) throws ResourceNotFoundException {
        UserResponseDTO user = this.userServiceImpl.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
