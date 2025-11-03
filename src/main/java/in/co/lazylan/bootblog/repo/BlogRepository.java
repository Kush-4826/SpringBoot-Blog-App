package in.co.lazylan.bootblog.repo;

import in.co.lazylan.bootblog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, String> {
    List<Blog> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);

    Optional<Blog> findBySlug(String slug);
}
