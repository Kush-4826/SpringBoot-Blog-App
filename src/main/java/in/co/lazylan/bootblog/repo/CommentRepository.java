package in.co.lazylan.bootblog.repo;

import in.co.lazylan.bootblog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
