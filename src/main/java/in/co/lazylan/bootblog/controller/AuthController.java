package in.co.lazylan.bootblog.controller;

import in.co.lazylan.bootblog.payload.request.JwtRequestDTO;
import in.co.lazylan.bootblog.payload.request.UserRequestDTO;
import in.co.lazylan.bootblog.payload.response.JwtResponseDTO;
import in.co.lazylan.bootblog.payload.response.UserResponseDTO;
import in.co.lazylan.bootblog.response.ErrorResponse;
import in.co.lazylan.bootblog.security.JwtHelper;
import in.co.lazylan.bootblog.service.UserService;
import in.co.lazylan.bootblog.util.FieldValueExists;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends ApiController {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final UserService userService;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtHelper jwtHelper, UserService userService, FieldValueExists userServiceImpl) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody JwtRequestDTO jwtRequestDTO) {
        this.doAuthenticate(jwtRequestDTO.getUsername(), jwtRequestDTO.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequestDTO.getUsername());
        String token = this.jwtHelper.generateToken(userDetails);

        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder().token(token).username(jwtRequestDTO.getUsername()).build();
        return ResponseEntity.ok(jwtResponseDTO);
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCreds(BadCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> store(@Valid @RequestBody UserRequestDTO userDto) {
        UserResponseDTO user = this.userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // TODO: rewrite the Auth System with Role Based Access Control
    // TODO: Add Full Refresh Token functionality
}
