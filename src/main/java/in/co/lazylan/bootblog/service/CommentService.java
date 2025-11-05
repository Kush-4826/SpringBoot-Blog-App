package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.payload.request.CommentRequestDTO;
import in.co.lazylan.bootblog.payload.response.CommentResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    CommentResponseDTO createComment(CommentRequestDTO comment) throws ResourceNotFoundException;

    void deleteComment(int commentId) throws ResourceNotFoundException;
}
