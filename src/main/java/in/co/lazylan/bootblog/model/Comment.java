package in.co.lazylan.bootblog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Blog blog;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    // TODO: Adding Comment Reply Feature
    // TODO: Adding Comment Publish Date
}
