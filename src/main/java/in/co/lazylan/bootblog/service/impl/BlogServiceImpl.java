package in.co.lazylan.bootblog.service.impl;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.Blog;
import in.co.lazylan.bootblog.model.Category;
import in.co.lazylan.bootblog.model.Comment;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.request.BlogRequestDTO;
import in.co.lazylan.bootblog.payload.response.BlogResponseDTO;
import in.co.lazylan.bootblog.payload.response.CommentResponseDTO;
import in.co.lazylan.bootblog.payload.response.PaginatedBlogResponseDTO;
import in.co.lazylan.bootblog.payload.response.PaginatedCommentsResponseDTO;
import in.co.lazylan.bootblog.repo.BlogRepository;
import in.co.lazylan.bootblog.repo.CategoryRepository;
import in.co.lazylan.bootblog.repo.CommentRepository;
import in.co.lazylan.bootblog.repo.UserRepository;
import in.co.lazylan.bootblog.service.BlogService;
import in.co.lazylan.bootblog.util.AppConstants;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public BlogServiceImpl(BlogRepository blogRepository, ModelMapper modelMapper, CategoryRepository categoryRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.blogRepository = blogRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;

        /*
        Creating a converter that will convert the title of the blog to the slug
         */
        Converter<String, String> titleToSlug = context -> {
            String title = context.getSource();
            return title == null ? null :
                    title.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
        };

        /*
        Now we create a PropertyMap to map the property. The type of the property
        map is <BlogRequestDTO, Blog> because we want this map to be executed when the
        modelmapper maps a dto object to the model object.
         */
        PropertyMap<BlogRequestDTO, Blog> blogMap = new PropertyMap<BlogRequestDTO, Blog>() {
            /*
            In the configure method, we use our titleToSlug converter to map the title
            of the source (BlogRequestDTO) to the Slug of the destination (Blog)
             */
            protected void configure() {
                using(titleToSlug).map(source.getTitle()).setSlug(null);
            }
        };

        PropertyMap<BlogRequestDTO, Blog> map = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        };
        this.modelMapper.addMappings(map);

        // Finally we add this property map to the modelmapper
        this.modelMapper.addMappings(blogMap);
        this.commentRepository = commentRepository;
    }

    @Override
    public BlogResponseDTO createBlog(BlogRequestDTO blogDto, User authUser) throws ResourceNotFoundException {
        // Fetching the supporting resources
        User user = this.userRepository.findById(authUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "ID", authUser.getId()));
        Category category = this.categoryRepository.findById(blogDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", "ID", blogDto.getCategoryId()));

        Blog blog = modelMapper.map(blogDto, Blog.class);
        System.out.println("Blog to be created id: " + blog.getId());
        // Manually setting the imagename for now
        // TODO: Implement the Image API
        blog.setImageName("sample.jpg");
        // Setting the date of the blog to current date
        blog.setCreatedDate(LocalDateTime.now());

        // Setting the author and the category
        blog.setCategory(category);
        blog.setAuthor(user);
        blogRepository.save(blog);
        return modelMapper.map(blog, BlogResponseDTO.class);
    }

    @Override
    public BlogResponseDTO updateBlog(BlogRequestDTO blogDto, int id) throws ResourceNotFoundException {
        Blog blog = this.blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blog", "ID", id));
        // Using modelmapper to map the updated title to a new slug
        Blog blogToBeUpdated = modelMapper.map(blogDto, Blog.class);

        // Setting the values from mapped model instead of the dto
        blog.setTitle(blogToBeUpdated.getTitle());
        blog.setContent(blogToBeUpdated.getContent());
        blog.setSlug(blogToBeUpdated.getSlug());
        blog.setImageName(blogToBeUpdated.getImageName());

        Blog saved = this.blogRepository.save(blog);
        return modelMapper.map(saved, BlogResponseDTO.class);
    }

    @Override
    public void deleteBlogById(int id) throws ResourceNotFoundException {
        Blog blog = this.blogRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "ID", id));
        this.blogRepository.delete(blog);
    }

    @Override
    public BlogResponseDTO getBlogById(int id) throws ResourceNotFoundException {
        Blog blog = this.blogRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "ID", id));
        return modelMapper.map(blog, BlogResponseDTO.class);
    }

    @Override
    public BlogResponseDTO getBlogBySlug(String slug) throws ResourceNotFoundException {
        Blog blog = this.blogRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "Slug", slug));
        return modelMapper.map(blog, BlogResponseDTO.class);
    }

    @Override
    public PaginatedBlogResponseDTO getAllBlogs(int pageNumber, String sortBy, String order) {
        Sort by = null;
        if ("asc".equalsIgnoreCase(order)) {
            by = Sort.by(sortBy).ascending();
        } else {
            by = Sort.by(sortBy).descending();
        }

        Pageable p = PageRequest.of(pageNumber, AppConstants.DEFAULT_PAGE_SIZE, by);
        Page<Blog> blogs = this.blogRepository.findAll(p);
        List<Blog> content = blogs.getContent();
        List<BlogResponseDTO> blogResponseDTOS = content.stream()
                .map(blog -> modelMapper.map(blog, BlogResponseDTO.class))
                .toList();

        // Creating the Paginated Response with all the pagination details
        PaginatedBlogResponseDTO responseDTO = new PaginatedBlogResponseDTO();
        responseDTO.setBlogs(blogResponseDTOS);
        responseDTO.setPageNumber(blogs.getNumber());
        responseDTO.setTotalPages(blogs.getTotalPages());
        responseDTO.setPageSize(blogs.getSize());
        responseDTO.setTotalElements(blogs.getTotalElements());
        responseDTO.setLastPage(blogs.isLast());

        return responseDTO;
    }

    @Override
    public List<BlogResponseDTO> getBlogsByCategory(int id) throws ResourceNotFoundException {
        Category category = this.categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", id));

        List<Blog> blogs = category.getBlogs();
        List<BlogResponseDTO> blogResponseDTOS = blogs.stream()
                .map(blog -> this.modelMapper.map(blog, BlogResponseDTO.class))
                .toList();

        return blogResponseDTOS;
    }

    @Override
    public List<BlogResponseDTO> getBlogByAuthor(int id) throws ResourceNotFoundException {
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "ID", id));

        List<Blog> blogs = user.getBlogs();
        List<BlogResponseDTO> blogResponseDTOS = blogs.stream()
                .map(blog -> this.modelMapper.map(blog, BlogResponseDTO.class))
                .toList();

        return blogResponseDTOS;
    }

    @Override
    public List<BlogResponseDTO> searchBlog(String keyword) {
        List<Blog> blogs = this.blogRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
        List<BlogResponseDTO> blogResponseDTOS = blogs.stream()
                .map(blog -> this.modelMapper.map(blog, BlogResponseDTO.class))
                .toList();
        return blogResponseDTOS;
    }

    @Override
    public PaginatedCommentsResponseDTO getCommentsForBlog(int blogId, int page) throws ResourceNotFoundException {
        Blog blog = this.blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException("Blog", "ID", blogId));
        Pageable p = PageRequest.of(page, AppConstants.DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Comment> commentPage = this.commentRepository.findByBlog(blog, p);
        List<Comment> comments = commentPage.getContent();
        List<CommentResponseDTO> commentResponseDTOS = comments.stream()
                .map(comment -> this.modelMapper.map(comment, CommentResponseDTO.class))
                .toList();

        PaginatedCommentsResponseDTO responseDTO = new PaginatedCommentsResponseDTO();
        responseDTO.setComments(commentResponseDTOS);
        responseDTO.setPageNumber(commentPage.getNumber());
        responseDTO.setTotalPages(commentPage.getTotalPages());
        responseDTO.setPageSize(commentPage.getSize());
        responseDTO.setTotalElements(commentPage.getTotalElements());
        responseDTO.setLastPage(commentPage.isLast());

        return responseDTO;
    }
}
