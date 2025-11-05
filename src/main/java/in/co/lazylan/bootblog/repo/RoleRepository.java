package in.co.lazylan.bootblog.repo;

import in.co.lazylan.bootblog.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
