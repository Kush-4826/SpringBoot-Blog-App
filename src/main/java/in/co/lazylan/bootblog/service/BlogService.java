package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.request.BlogRequestDTO;
import in.co.lazylan.bootblog.payload.response.BlogResponseDTO;
import in.co.lazylan.bootblog.payload.response.PaginatedBlogResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlogService {
    BlogResponseDTO createBlog(BlogRequestDTO blogDto, String authorId, String categoryId) throws ResourceNotFoundException;

    BlogResponseDTO updateBlog(BlogRequestDTO blogDto, String id) throws ResourceNotFoundException;

    void deleteBlogById(String id) throws ResourceNotFoundException;

    BlogResponseDTO getBlogById(String id) throws ResourceNotFoundException;

    BlogResponseDTO getBlogBySlug(String slug) throws ResourceNotFoundException;

    PaginatedBlogResponseDTO getAllBlogs(int pageNumber);

    List<BlogResponseDTO> getBlogsByCategory(String id) throws ResourceNotFoundException;

    List<BlogResponseDTO> getBlogByAuthor(String id) throws ResourceNotFoundException;

    List<BlogResponseDTO> searchBlog(String keyword);
}
