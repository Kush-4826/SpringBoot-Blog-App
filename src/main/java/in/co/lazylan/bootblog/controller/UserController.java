package in.co.lazylan.bootblog.controller;

import in.co.lazylan.bootblog.SuccessResponse;
import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.UserDto;
import in.co.lazylan.bootblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userServiceImpl;

    @Autowired
    public UserController(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("")
    public ResponseEntity<UserDto> store(@RequestBody UserDto userDto) {
        UserDto user = this.userServiceImpl.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto, @PathVariable String id) {
        try {
            UserDto updatedUser = this.userServiceImpl.updateUser(userDto, id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return new ResponseEntity<>(new SuccessResponse("User Deleted Successfully"), HttpStatus.OK);
    }
}
