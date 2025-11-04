package in.co.lazylan.bootblog.controller;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.request.BlogRequestDTO;
import in.co.lazylan.bootblog.payload.response.BlogResponseDTO;
import in.co.lazylan.bootblog.payload.response.PaginatedBlogResponseDTO;
import in.co.lazylan.bootblog.response.SuccessResponse;
import in.co.lazylan.bootblog.service.BlogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/users/{userId}/categories/{categoryId}/blogs")
    public ResponseEntity<BlogResponseDTO> createBlog(
            @Valid @RequestBody BlogRequestDTO blogDto,
            @PathVariable String userId,
            @PathVariable String categoryId
    ) throws ResourceNotFoundException {
        BlogResponseDTO dto = this.blogService.createBlog(blogDto, userId, categoryId);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/blogs/{blogId}")
    public ResponseEntity<BlogResponseDTO> updateBlog(
            @Valid @RequestBody BlogRequestDTO blogDto,
            @PathVariable String blogId
    ) throws ResourceNotFoundException {
        BlogResponseDTO dto = this.blogService.updateBlog(blogDto, blogId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}/blogs")
    public ResponseEntity<List<BlogResponseDTO>> getBlogsByCategory(
            @PathVariable String categoryId
    ) throws ResourceNotFoundException {
        List<BlogResponseDTO> blogsByCategory = this.blogService.getBlogsByCategory(categoryId);
        return new ResponseEntity<>(blogsByCategory, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/blogs")
    public ResponseEntity<List<BlogResponseDTO>> getBlogsByAuthor(
            @PathVariable String userId
    ) throws ResourceNotFoundException {
        List<BlogResponseDTO> blogsByAuthor = this.blogService.getBlogByAuthor(userId);
        return new ResponseEntity<>(blogsByAuthor, HttpStatus.OK);
    }

    @DeleteMapping("/blogs/{blogId}")
    public ResponseEntity<SuccessResponse> deleteBlog(
            @PathVariable String blogId
    ) throws ResourceNotFoundException {
        this.blogService.deleteBlogById(blogId);
        return new ResponseEntity<>(new SuccessResponse("Blog deleted successfully"), HttpStatus.OK);
    }

    @GetMapping("/blogs/{slug}")
    public ResponseEntity<BlogResponseDTO> getBlogBySlug(
            @PathVariable String slug
    ) throws ResourceNotFoundException {
        BlogResponseDTO dto = this.blogService.getBlogBySlug(slug);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/blogs")
    public ResponseEntity<PaginatedBlogResponseDTO> getAllBlogs(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "sortBy", defaultValue = "createdDate", required = false) String sortBy,
            @RequestParam(name = "order", defaultValue = "asc", required = false) String order
    ) {
        PaginatedBlogResponseDTO blogResponseDTOS = this.blogService.getAllBlogs(page, sortBy, order);
        return new ResponseEntity<>(blogResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/blogs/search")
    public ResponseEntity<List<BlogResponseDTO>> search(
            @RequestParam(name = "q", required = true) String keyword
    ) {
        List<BlogResponseDTO> blogResponseDTOS = this.blogService.searchBlog(keyword);
        return new ResponseEntity<>(blogResponseDTOS, HttpStatus.OK);
    }

//    @GetMapping("/blogs/{blogId}")
//    public ResponseEntity<BlogResponseDTO> getBlogById(
//            @PathVariable String blogId
//    ) throws ResourceNotFoundException {
//        BlogResponseDTO dto = this.blogService.getBlogById(blogId);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
//    }
}
