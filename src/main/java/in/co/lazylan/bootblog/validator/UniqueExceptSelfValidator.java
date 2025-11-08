package in.co.lazylan.bootblog.validator;

import in.co.lazylan.bootblog.util.FieldValueExists;
import in.co.lazylan.bootblog.validator.annotation.UniqueExceptSelf;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class UniqueExceptSelfValidator implements ConstraintValidator<UniqueExceptSelf, String> {
    @Autowired
    ApplicationContext applicationContext;

    FieldValueExists service;
    String fieldName;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !service.fieldValueExistsExceptSelf(value, fieldName);
    }

    @Override
    public void initialize(UniqueExceptSelf constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.service = applicationContext.getBean(constraintAnnotation.service());
        this.fieldName = constraintAnnotation.fieldName();
    }
}
