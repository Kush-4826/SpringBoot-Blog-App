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
import in.co.lazylan.bootblog.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final BlogService blogService;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(UserService userService, ModelMapper modelMapper, BlogService blogService, CommentRepository commentRepository) {
        this.userService = userService;
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
    public CommentResponseDTO createComment(CommentRequestDTO comment) throws ResourceNotFoundException {
        User currentUser = this.modelMapper.map(this.userService.getUserById(comment.getUserId()), User.class);
        Blog currentBlog = this.modelMapper.map(this.blogService.getBlogById(comment.getBlogId()), Blog.class);

        Comment currentComment = this.modelMapper.map(comment, Comment.class);
        currentComment.setUser(currentUser);
        currentComment.setBlog(currentBlog);

        Comment saved = this.commentRepository.save(currentComment);
        return this.modelMapper.map(saved, CommentResponseDTO.class);
    }

    @Override
    public void deleteComment(String commentId) throws ResourceNotFoundException {
        Comment comment = this.commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", commentId));

        this.commentRepository.delete(comment);
    }
}
