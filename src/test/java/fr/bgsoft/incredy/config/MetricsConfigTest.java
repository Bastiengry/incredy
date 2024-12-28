package fr.bgsoft.incredy.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

import fr.bgsoft.incredy.metrics.ExtraTag;
import fr.bgsoft.incredy.metrics.MetricsConfig;
import io.micrometer.core.instrument.Tag;

public class MetricsConfigTest {
	@Test
	public void shouldSucceedToCreateTagWithoutArgument() {
		final ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
		final MethodSignature signaturePjp = mock(MethodSignature.class);
		when(pjp.getSignature()).thenReturn(signaturePjp);
		final Method method = mock(Method.class);
		when(signaturePjp.getMethod()).thenReturn(method);
		final Parameter parameter = mock(Parameter.class);
		when(method.getParameters()).thenReturn(new Parameter[] { parameter });
		when(parameter.getAnnotations()).thenReturn(new Annotation[] {});

		final StaticPart staticPart = mock(StaticPart.class);
		when(pjp.getStaticPart()).thenReturn(staticPart);
		final Signature signatureStaticPart = mock(Signature.class);
		when(staticPart.getSignature()).thenReturn(signatureStaticPart);
		when(signatureStaticPart.getDeclaringTypeName()).thenReturn("MyClass");
		when(signatureStaticPart.getName()).thenReturn("myMethod");

		final MetricsConfig metricsConfig = new MetricsConfig();
		final Iterable<Tag> itTags = metricsConfig.tagFactory(pjp);

		final List<Tag> listTags = Lists.newArrayList(itTags);
		assertEquals(2, listTags.size());
		assertEquals("class", listTags.get(0).getKey());
		assertEquals("MyClass", listTags.get(0).getValue());
		assertEquals("method", listTags.get(1).getKey());
		assertEquals("myMethod", listTags.get(1).getValue());
	}

	@Test
	public void shouldSucceedToCreateTagWithArguments() {
		final ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
		final MethodSignature signaturePjp = mock(MethodSignature.class);
		when(pjp.getSignature()).thenReturn(signaturePjp);
		when(pjp.getArgs()).thenReturn(new Object[] { "arg1" });
		final Method method = mock(Method.class);
		when(signaturePjp.getMethod()).thenReturn(method);
		final Parameter parameter = mock(Parameter.class);
		when(method.getParameters()).thenReturn(new Parameter[] { parameter });
		final ExtraTag extraTagAnnotation = new ExtraTag() {
			@Override
			public String value() {
				return "value";
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}
		};

		when(parameter.getAnnotations()).thenReturn(new Annotation[] { extraTagAnnotation });

		final StaticPart staticPart = mock(StaticPart.class);
		when(pjp.getStaticPart()).thenReturn(staticPart);
		final Signature signatureStaticPart = mock(Signature.class);
		when(staticPart.getSignature()).thenReturn(signatureStaticPart);
		when(signatureStaticPart.getDeclaringTypeName()).thenReturn("MyClass");
		when(signatureStaticPart.getName()).thenReturn("myMethod");

		final MetricsConfig metricsConfig = new MetricsConfig();
		final Iterable<Tag> itTags = metricsConfig.tagFactory(pjp);

		final List<Tag> listTags = Lists.newArrayList(itTags);
		assertEquals(3, listTags.size());
		assertEquals("class", listTags.get(0).getKey());
		assertEquals("MyClass", listTags.get(0).getValue());
		assertEquals("method", listTags.get(1).getKey());
		assertEquals("myMethod", listTags.get(1).getValue());
		assertEquals("value", listTags.get(2).getKey());
		assertEquals("arg1", listTags.get(2).getValue());
	}
}
