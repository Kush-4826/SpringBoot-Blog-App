package in.co.lazylan.bootblog.controller;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.request.CommentRequestDTO;
import in.co.lazylan.bootblog.payload.response.CommentResponseDTO;
import in.co.lazylan.bootblog.response.SuccessResponse;
import in.co.lazylan.bootblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestBody CommentRequestDTO comment
    ) throws ResourceNotFoundException {
        CommentResponseDTO commentResponseDTO = this.commentService.createComment(comment);
        return new ResponseEntity<>(commentResponseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> delete(
            @PathVariable int commentId
    ) throws ResourceNotFoundException {
        this.commentService.deleteComment(commentId);
        return new ResponseEntity<>(new SuccessResponse("Comment Deleted Successfully"), HttpStatus.OK);
    }
}
