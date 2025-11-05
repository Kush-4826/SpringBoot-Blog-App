package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.request.BlogRequestDTO;
import in.co.lazylan.bootblog.payload.response.BlogResponseDTO;
import in.co.lazylan.bootblog.payload.response.PaginatedBlogResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlogService {
    BlogResponseDTO createBlog(BlogRequestDTO blogDto, int authorId, int categoryId) throws ResourceNotFoundException;

    BlogResponseDTO updateBlog(BlogRequestDTO blogDto, int id) throws ResourceNotFoundException;

    void deleteBlogById(int id) throws ResourceNotFoundException;

    BlogResponseDTO getBlogById(int id) throws ResourceNotFoundException;

    BlogResponseDTO getBlogBySlug(String slug) throws ResourceNotFoundException;

    PaginatedBlogResponseDTO getAllBlogs(int pageNumber, String sortBy, String order);

    List<BlogResponseDTO> getBlogsByCategory(int id) throws ResourceNotFoundException;

    List<BlogResponseDTO> getBlogByAuthor(int id) throws ResourceNotFoundException;

    List<BlogResponseDTO> searchBlog(String keyword);
}
