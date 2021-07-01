package com.example.mywebshop.config.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMatchConstraint {
    String message() default "Values doesn't match";
    String field();
    String fieldMatch();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}