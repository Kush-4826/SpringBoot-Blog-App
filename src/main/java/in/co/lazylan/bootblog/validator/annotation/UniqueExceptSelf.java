package in.co.lazylan.bootblog.validator.annotation;

import in.co.lazylan.bootblog.util.FieldValueExists;
import in.co.lazylan.bootblog.validator.UniqueExceptSelfValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueExceptSelfValidator.class)
@Documented
public @interface UniqueExceptSelf {
    String message() default "already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends FieldValueExists> service();

    String fieldName();
}
