package fr.bgsoft.incredy.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

@Configuration
public class MetricsConfig {
	@Bean
	public CountedAspect countedAspect(final MeterRegistry meterRegistry) {
		return new CountedAspect(meterRegistry, this::tagFactory);
	}

	private Iterable<Tag> tagFactory(final ProceedingJoinPoint pjp) {
		return Tags.of(
				"class", pjp.getStaticPart().getSignature().getDeclaringTypeName(),
				"method", pjp.getStaticPart().getSignature().getName())
				.and(getParameterTags(pjp))
				.and(ExtraTagsPropagation.getTagsAndReset());
	}

	private Iterable<Tag> getParameterTags(final ProceedingJoinPoint pjp) {
		final Set<Tag> tags = new HashSet<>();

		final Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		final Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			for (final Annotation annotation : parameters[i].getAnnotations()) {
				if (annotation instanceof ExtraTag) {
					final ExtraTag extraTag = (ExtraTag) annotation;
					tags.add(Tag.of(extraTag.value(), String.valueOf(pjp.getArgs()[i])));
				}
			}
		}

		return tags;
	}
}