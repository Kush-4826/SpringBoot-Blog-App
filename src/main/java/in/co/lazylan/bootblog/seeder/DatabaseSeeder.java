package in.co.lazylan.bootblog.seeder;

import com.github.javafaker.Faker;
import in.co.lazylan.bootblog.model.*;
import in.co.lazylan.bootblog.repo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final BlogRepository blogRepo;
    private final CategoryRepository categoryRepo;
    private final CommentRepository commentRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:false}")
    private boolean seederEnabled;

    public DatabaseSeeder(UserRepository userRepo, BlogRepository blogRepo,
                          CategoryRepository categoryRepo,
                          CommentRepository commentRepo,
                          RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.blogRepo = blogRepo;
        this.categoryRepo = categoryRepo;
        this.commentRepo = commentRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seederEnabled) return;
        if (userRepo.count() > 0
                || blogRepo.count() > 0
                || categoryRepo.count() > 0
                || commentRepo.count() > 0
                || roleRepo.count() > 0) return;

        Faker faker = new Faker();

        // 1. Roles
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole.setUsers(new ArrayList<>());
        Role userRole = new Role();
        userRole.setName("USER");
        userRole.setUsers(new ArrayList<>());
        roleRepo.saveAll(List.of(adminRole, userRole));

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
            user.setUsername(faker.name().username());
            user.setPassword(password);
            user.setEmail(faker.internet().emailAddress());
            user.setGender(faker.options().option("Male", "Female", "Other"));
            user.setAbout(faker.lorem().paragraph());
            user.setRoles(List.of(userRole));
            users.add(user);
        }
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(password);
        admin.setEmail("admin@example.com");
        admin.setGender("Other");
        admin.setAbout("The admin user for bootblog.");
        admin.setRoles(List.of(adminRole, userRole));
        users.add(admin);

        userRepo.saveAll(users);

//        // Add users to roles for bi-directional relationship
//        adminRole.setUsers(List.of(admin));
//        userRole.setUsers(users);
//        roleRepo.saveAll(List.of(adminRole, userRole));

        // 4. Blogs - 1000
        List<Blog> blogs = new ArrayList<>();
        for (int i = 0; i < 1000; ++i) {
            Blog blog = new Blog();
            blog.setTitle(faker.book().title());
            blog.setSlug(faker.lorem().characters(8, 16, true));
            blog.setContent(faker.lorem().paragraph(5));
            blog.setImageName("default.jpg");
            blog.setCreatedDate(LocalDate.now().minusDays(faker.random().nextInt(0, 365)));
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
                comments.add(comm);
            }
        }
        // Ensure at least 10,000 comments (possible overspill)
        while (comments.size() < 10000) {
            Blog blog = faker.options().nextElement(blogs);
            Comment comm = new Comment();
            comm.setContent(faker.lorem().sentence());
            comm.setBlog(blog);
            comm.setUser(faker.options().nextElement(users));
            comments.add(comm);
        }
        commentRepo.saveAll(comments);

        System.out.println("[Seeder] Database seeded with dummy data.");
    }
}
