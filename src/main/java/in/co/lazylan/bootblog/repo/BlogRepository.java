package in.co.lazylan.bootblog.repo;

import in.co.lazylan.bootblog.model.Blog;
import in.co.lazylan.bootblog.model.Category;
import in.co.lazylan.bootblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, String> {
    List<Blog> findByAuthor(User author);

    List<Blog> findByCategory(Category category);

    List<Blog> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
}
