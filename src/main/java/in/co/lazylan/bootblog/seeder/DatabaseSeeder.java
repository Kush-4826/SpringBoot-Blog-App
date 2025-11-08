package in.co.lazylan.bootblog.seeder;

import com.github.javafaker.Faker;
import in.co.lazylan.bootblog.model.Blog;
import in.co.lazylan.bootblog.model.Category;
import in.co.lazylan.bootblog.model.Comment;
import in.co.lazylan.bootblog.model.User;
import in.co.lazylan.bootblog.repo.BlogRepository;
import in.co.lazylan.bootblog.repo.CategoryRepository;
import in.co.lazylan.bootblog.repo.CommentRepository;
import in.co.lazylan.bootblog.repo.UserRepository;
import in.co.lazylan.bootblog.types.RoleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final BlogRepository blogRepo;
    private final CategoryRepository categoryRepo;
    private final CommentRepository commentRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:false}")
    private boolean seederEnabled;

    public DatabaseSeeder(UserRepository userRepo, BlogRepository blogRepo,
                          CategoryRepository categoryRepo,
                          CommentRepository commentRepo,
                          PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.blogRepo = blogRepo;
        this.categoryRepo = categoryRepo;
        this.commentRepo = commentRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seederEnabled) return;
        if (userRepo.count() > 0
                || blogRepo.count() > 0
                || categoryRepo.count() > 0
                || commentRepo.count() > 0) return;

        Faker faker = new Faker();

        // 2. Categories - 10
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            Category category = new Category();
            category.setName("Category-" + faker.book().genre() + "_" + i);
            category.setDescription(faker.lorem().sentence());
            categories.add(category);
        }
        categoryRepo.saveAll(categories);

        // 3. Users - 100 user + 1 admin = 101
        List<User> users = new ArrayList<>();
        String password = passwordEncoder.encode("abcd1234");
        for (int i = 0; i < 100; ++i) {
            User user = new User();
            user.setName(faker.name().username());
            user.setPassword(password);
            user.setEmail(faker.internet().emailAddress());
            user.setGender(faker.options().option("Male", "Female", "Other"));
            user.setAbout(faker.lorem().paragraph());
            user.setRoles(Set.of(RoleType.AUTHOR));
            users.add(user);
        }
        User admin = new User();
        admin.setName("admin");
        admin.setPassword(password);
        admin.setEmail("admin@example.com");
        admin.setGender("Other");
        admin.setAbout("The admin user for bootblog.");
        admin.setRoles(Set.of(RoleType.ADMIN));
        users.add(admin);

        userRepo.saveAll(users);

        // 4. Blogs - 1000
        List<Blog> blogs = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            Blog blog = new Blog();
            String title = faker.lorem().sentence(faker.random().nextInt(2, 8));
            String slug = title.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
            blog.setTitle(title);
            blog.setSlug(slug);
            blog.setContent(faker.lorem().paragraph(5));
            blog.setImageName("default.jpg");
            blog.setCreatedDate(LocalDateTime.now().minusDays(faker.random().nextInt(0, 365)));
            blog.setCategory(faker.options().nextElement(categories));
            blog.setAuthor(faker.options().nextElement(users));
            blogs.add(blog);
        }
        blogRepo.saveAll(blogs);

        // 5. Comments - at least 10,000 (spread across blogs)
        List<Comment> comments = new ArrayList<>();
        for (Blog blog : blogs) {
            int numComments = faker.random().nextInt(10, 20); // randomly 10 to 20 comments per blog
            for (int j = 0; j < numComments; ++j) {
                Comment comm = new Comment();
                comm.setContent(faker.lorem().sentence());
                comm.setBlog(blog);
                comm.setUser(faker.options().nextElement(users));
                comm.setPublishedAt(LocalDateTime.now().minusDays(faker.random().nextInt(0, 365)));
                comments.add(comm);
            }
        }
        commentRepo.saveAll(comments);

        System.out.println("[Seeder] Database seeded with dummy data.");
    }
}
