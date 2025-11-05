package in.co.lazylan.bootblog.repo;

import in.co.lazylan.bootblog.model.Blog;
import in.co.lazylan.bootblog.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByBlog(Blog blog, Pageable pageable);
}
