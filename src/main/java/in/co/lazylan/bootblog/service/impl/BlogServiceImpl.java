package in.co.lazylan.bootblog.service.impl;

import in.co.lazylan.bootblog.exception.ResourceNotFoundException;
import in.co.lazylan.bootblog.model.Blog;
import in.co.lazylan.bootblog.model.Category;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.payload.request.BlogRequestDTO;
import in.co.lazylan.bootblog.payload.response.BlogResponseDTO;
import in.co.lazylan.bootblog.repo.BlogRepository;
import in.co.lazylan.bootblog.repo.CategoryRepository;
import in.co.lazylan.bootblog.repo.UserRepository;
import in.co.lazylan.bootblog.service.BlogService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public BlogServiceImpl(BlogRepository blogRepository, ModelMapper modelMapper, CategoryRepository categoryRepository, UserRepository userRepository) {
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

        // Finally we add this property map to the modelmapper
        this.modelMapper.addMappings(blogMap);
    }

    @Override
    public BlogResponseDTO createBlog(BlogRequestDTO blogDto, String authorId, String categoryId) throws ResourceNotFoundException {
        // Fetching the supporting resources
        User user = this.userRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("User", "ID", authorId));
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));

        Blog blog = modelMapper.map(blogDto, Blog.class);
        // Manually setting the imagename for now
        // TODO: Implement the Image API
        blog.setImageName("sample.jpg");
        // Setting the date of the blog to current date
        blog.setCreatedDate(LocalDate.now());

        // Setting the author and the category
        blog.setCategory(category);
        blog.setAuthor(user);
        blogRepository.save(blog);
        return modelMapper.map(blog, BlogResponseDTO.class);
    }

    @Override
    public BlogResponseDTO updateBlog(BlogRequestDTO blogDto, String id) throws ResourceNotFoundException {
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
    public void deleteBlogById(String id) throws ResourceNotFoundException {
        Blog blog = this.blogRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "ID", id));
        this.blogRepository.delete(blog);
    }

    @Override
    public BlogResponseDTO getBlogById(String id) {
        return null;
    }

    @Override
    public List<BlogResponseDTO> getAllBlogs() {
        return List.of();
    }

    @Override
    public List<BlogResponseDTO> getBlogsByCategory(String id) throws ResourceNotFoundException {
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
    public List<BlogResponseDTO> getBlogByAuthor(String id) throws ResourceNotFoundException {
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
        return List.of();
    }
}
