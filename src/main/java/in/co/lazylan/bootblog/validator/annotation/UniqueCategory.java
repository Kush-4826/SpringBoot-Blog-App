package in.co.lazylan.bootblog.validator.annotation;

import in.co.lazylan.bootblog.validator.UniqueCategoryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueCategoryValidator.class)
@Documented
public @interface UniqueCategory {
    String message() default "Category already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
