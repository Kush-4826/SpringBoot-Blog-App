package in.co.lazylan.bootblog.service;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.request.CommentRequestDTO;
import in.co.lazylan.bootblog.payload.response.CommentResponseDTO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    CommentResponseDTO createComment(CommentRequestDTO comment, User user) throws ResourceNotFoundException;

    void deleteComment(int commentId, User user) throws ResourceNotFoundException, AccessDeniedException;
}
