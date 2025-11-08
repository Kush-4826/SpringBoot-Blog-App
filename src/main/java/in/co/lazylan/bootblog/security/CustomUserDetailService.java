package in.co.lazylan.bootblog.security;

import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * We'll be loading a user from the database in this method.
     * Whenever we need to get the user details from some custom source (Database in this case),
     * we need to create our own implementation of the UserDetailsService and then
     * implement this method. In this method, now we'll fetch the user from the database
     * based on the username.
     *
     * @param email the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        return user;
    }
}
