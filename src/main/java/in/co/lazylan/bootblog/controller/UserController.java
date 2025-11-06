package in.co.lazylan.bootblog.controller;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.request.UserRequestDTO;
import in.co.lazylan.bootblog.payload.response.UserResponseDTO;
import in.co.lazylan.bootblog.response.SuccessResponse;
import in.co.lazylan.bootblog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController extends ApiController {

    private final UserService userServiceImpl;

    @Autowired
    public UserController(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @Valid @RequestBody UserRequestDTO userDto,
            @PathVariable int id,
            @AuthenticationPrincipal User user
    ) throws ResourceNotFoundException, AccessDeniedException {
        UserResponseDTO updatedUser = this.userServiceImpl.updateUser(userDto, id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable int id,
            @AuthenticationPrincipal User authUser
    ) throws ResourceNotFoundException {
        this.userServiceImpl.deleteUserById(id, authUser);
        return new ResponseEntity<>(
                new SuccessResponse("User with id " + id + " Deleted Successfully"),
                HttpStatus.OK
        );
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponseDTO>> index(
            @AuthenticationPrincipal User user
    ) {
        List<UserResponseDTO> users = this.userServiceImpl.getAllUsers(user);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> show(
            @PathVariable int id,
            @AuthenticationPrincipal User user
    ) throws ResourceNotFoundException, AccessDeniedException {
        UserResponseDTO userResponseDTO = this.userServiceImpl.getUserById(id, user);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/{userId}/promote")
    public ResponseEntity<SuccessResponse> promote(
            @PathVariable int userId,
            @AuthenticationPrincipal User authUser
    ) throws ResourceNotFoundException, AccessDeniedException {
        this.userServiceImpl.promoteAuthorToAdmin(userId, authUser);
        return new ResponseEntity<>(new SuccessResponse("Author with id " + userId + " Promoted to ADMIN"), HttpStatus.OK);
    }

    @PutMapping("/{userId}/demote")
    public ResponseEntity<SuccessResponse> demote(
            @PathVariable int userId,
            @AuthenticationPrincipal User authUser
    ) throws ResourceNotFoundException, AccessDeniedException {
        this.userServiceImpl.demoteAdminToAuthor(userId, authUser);
        return new ResponseEntity<>(new SuccessResponse("Admin with id " + userId + " Demoted to AUTHOR"), HttpStatus.OK);
    }
}
