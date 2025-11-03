package in.co.lazylan.bootblog.payload;

import in.co.lazylan.bootblog.service.impl.CategoryServiceImpl;
import in.co.lazylan.bootblog.validator.annotation.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDto {
    private String id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    @Unique(service = CategoryServiceImpl.class, fieldName = "name", message = "Category already exists")
    private String name;

    @Size(min = 3, message = "Description must be at least 3 characters long")
    private String description;
}
