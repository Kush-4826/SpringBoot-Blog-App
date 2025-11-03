package in.co.lazylan.bootblog.controller;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.BlogDto;
import in.co.lazylan.bootblog.service.BlogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/users/{userId}/categories/{categoryId}/blogs")
    public ResponseEntity<BlogDto> createBlog(
            @Valid @RequestBody BlogDto blogDto,
            @PathVariable String userId,
            @PathVariable String categoryId
    ) throws ResourceNotFoundException {
        BlogDto dto = this.blogService.createBlog(blogDto, userId, categoryId);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
