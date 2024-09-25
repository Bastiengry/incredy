package fr.bgsoft.incredy.valid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.bgsoft.incredy.valid.UpdateTopicDtoOldTopicExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UpdateTopicDtoOldTopicExistValidator.class)
public @interface UpdateTopicDtoOldTopicExistValidatorAnnotation {
	String message() default "Old topic should exist";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
