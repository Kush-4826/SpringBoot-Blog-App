package in.co.lazylan.bootblog.service.impl;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.Blog;
import in.co.lazylan.bootblog.model.Comment;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.request.CommentRequestDTO;
import in.co.lazylan.bootblog.payload.response.CommentResponseDTO;
import in.co.lazylan.bootblog.repo.CommentRepository;
import in.co.lazylan.bootblog.service.BlogService;
import in.co.lazylan.bootblog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    private final ModelMapper modelMapper;
    private final BlogService blogService;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(ModelMapper modelMapper, BlogService blogService, CommentRepository commentRepository) {
        this.modelMapper = modelMapper;
        this.blogService = blogService;
        this.commentRepository = commentRepository;

        PropertyMap<CommentRequestDTO, Comment> map = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        };
        this.modelMapper.addMappings(map);
    }

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO comment, User user) throws ResourceNotFoundException {
        Blog currentBlog = this.modelMapper.map(this.blogService.getBlogById(comment.getBlogId()), Blog.class);

        Comment currentComment = this.modelMapper.map(comment, Comment.class);
        currentComment.setUser(user);
        currentComment.setBlog(currentBlog);
        currentComment.setPublishedAt(LocalDateTime.now());

        Comment saved = this.commentRepository.save(currentComment);
        return this.modelMapper.map(saved, CommentResponseDTO.class);
    }

    @Override
    public void deleteComment(int commentId, User user) throws ResourceNotFoundException, AccessDeniedException {
        Comment comment = this.commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", commentId));


        // Normal User can delete only his own comments
        // Admin can delete anyone's comments
        if (!user.isAdmin() && comment.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("You cannot delete the comment as you do not own it!");
        }

        this.commentRepository.delete(comment);
    }
}
