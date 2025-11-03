package in.co.lazylan.bootblog.validator;

import in.co.lazylan.bootblog.repo.CategoryRepository;
import in.co.lazylan.bootblog.validator.annotation.UniqueCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueCategoryValidator implements ConstraintValidator<UniqueCategory, String> {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !categoryRepository.existsByName(value);
    }

    @Override
    public void initialize(UniqueCategory constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
