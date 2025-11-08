package in.co.lazylan.bootblog.repo;

import in.co.lazylan.bootblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByName(String username);

    Optional<User> findByName(String username);
}
