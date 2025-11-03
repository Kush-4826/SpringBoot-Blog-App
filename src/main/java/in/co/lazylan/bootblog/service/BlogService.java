package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.BlogDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlogService {
    BlogDto createBlog(BlogDto blogDto, String authorId, String categoryId) throws ResourceNotFoundException;

    BlogDto updateBlog(BlogDto blogDto, String id) throws ResourceNotFoundException;

    void deleteBlogById(String id);

    BlogDto getBlogById(String id);

    List<BlogDto> getAllBlogs();

    List<BlogDto> getBlogByCategory(String id);

    List<BlogDto> getBlogByAuthor(String id);

    List<BlogDto> searchBlog(String keyword);
}
