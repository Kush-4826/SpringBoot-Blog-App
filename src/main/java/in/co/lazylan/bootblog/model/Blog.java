package in.co.lazylan.bootblog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "blogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String imageName;

    // TODO: Convert to LocalDateTime
    @Column(nullable = false)
    private LocalDate createdDate;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User author;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "blog", fetch = FetchType.LAZY)
    private List<Comment> comments;

    // TODO: Add Soft Deletes
    // TODO: Add published At Date
    // TODO: Create Tags Entity
    // TODO: Add Tags Relationship to the Blogs
    // TODO: Add image upload feature
}
