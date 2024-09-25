package fr.bgsoft.incredy.valid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.bgsoft.incredy.valid.UpdateTopicDtoUserAuthorizedValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UpdateTopicDtoUserAuthorizedValidator.class)
public @interface UpdateTopicDtoUserAuthorizedValidatorAnnotation {
	String message() default "User not allowed";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
