package in.co.lazylan.bootblog.controller;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.request.CommentRequestDTO;
import in.co.lazylan.bootblog.payload.response.CommentResponseDTO;
import in.co.lazylan.bootblog.response.SuccessResponse;
import in.co.lazylan.bootblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController extends ApiController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public ResponseEntity<CommentResponseDTO> store(
            @RequestBody CommentRequestDTO comment,
            @AuthenticationPrincipal User user
    ) throws ResourceNotFoundException {
        CommentResponseDTO commentResponseDTO = this.commentService.createComment(comment, user);
        return new ResponseEntity<>(commentResponseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> delete(
            @PathVariable int commentId,
            @AuthenticationPrincipal User user
    ) throws ResourceNotFoundException, AccessDeniedException {
        this.commentService.deleteComment(commentId, user);
        return new ResponseEntity<>(new SuccessResponse("Comment Deleted Successfully"), HttpStatus.OK);
    }
}
