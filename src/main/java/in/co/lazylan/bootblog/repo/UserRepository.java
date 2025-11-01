package in.co.lazylan.bootblog.repo;

import in.co.lazylan.bootblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
