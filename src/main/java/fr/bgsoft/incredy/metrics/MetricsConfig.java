package fr.bgsoft.incredy.metrics;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

public class MetricsConfig {
	public Iterable<Tag> tagFactory(final ProceedingJoinPoint pjp) {
		return Tags.of(
				"class", pjp.getStaticPart().getSignature().getDeclaringTypeName(),
				"method", pjp.getStaticPart().getSignature().getName())
				.and(getParameterTags(pjp));
	}

	private Iterable<Tag> getParameterTags(final ProceedingJoinPoint pjp) {
		final Set<Tag> tags = new HashSet<>();

		final Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		final Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			for (final Annotation annotation : parameters[i].getAnnotations()) {

				if (annotation instanceof ExtraTag) {
					final ExtraTag extraTag = (ExtraTag) annotation;
					if ((pjp.getArgs() != null) && (pjp.getArgs().length > i)) {
						tags.add(Tag.of(extraTag.value(), String.valueOf(pjp.getArgs()[i])));
					}
				}
			}
		}

		return tags;
	}
}