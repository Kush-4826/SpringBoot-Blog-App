package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.request.BlogRequestDTO;
import in.co.lazylan.bootblog.payload.response.BlogResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlogService {
    BlogResponseDTO createBlog(BlogRequestDTO blogDto, String authorId, String categoryId) throws ResourceNotFoundException;

    BlogResponseDTO updateBlog(BlogRequestDTO blogDto, String id) throws ResourceNotFoundException;

    void deleteBlogById(String id);

    BlogResponseDTO getBlogById(String id);

    List<BlogResponseDTO> getAllBlogs();

    List<BlogResponseDTO> getBlogByCategory(String id);

    List<BlogResponseDTO> getBlogByAuthor(String id);

    List<BlogResponseDTO> searchBlog(String keyword);
}
