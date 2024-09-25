package fr.bgsoft.incredy.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.micrometer.core.instrument.Tag;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExtraTagsPropagation {
	private static final ThreadLocal<Set<Tag>> TAGS = ThreadLocal.withInitial(HashSet::new);

	public static void add(final String key, final Object value) {
		add(key, String.valueOf(value));
	}

	public static void add(final String key, final String value) {
		TAGS.get().add(Tag.of(key, value));
	}

	static Iterable<Tag> getTagsAndReset() {
		final Set<Tag> tags = Collections.unmodifiableSet(TAGS.get());
		TAGS.remove();

		return tags;
	}
}