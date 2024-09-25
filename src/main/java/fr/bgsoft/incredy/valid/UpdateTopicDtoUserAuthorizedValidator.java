package fr.bgsoft.incredy.valid;

import java.util.Optional;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.entity.Topic;
import fr.bgsoft.incredy.repository.TopicRepository;
import fr.bgsoft.incredy.valid.annotation.UpdateTopicDtoUserAuthorizedValidatorAnnotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateTopicDtoUserAuthorizedValidator
implements ConstraintValidator<UpdateTopicDtoUserAuthorizedValidatorAnnotation, UpdateTopicDto> {

	private final TopicRepository topicRepository;

	public UpdateTopicDtoUserAuthorizedValidator(final TopicRepository topicRepository) {
		this.topicRepository = topicRepository;
	}

	@Override
	public boolean isValid(final UpdateTopicDto updateTopicDto, final ConstraintValidatorContext context) {
		boolean valid = true;

		final Optional<Topic> oldTopicOpt = topicRepository.findById(updateTopicDto.getId().longValue());

		if (oldTopicOpt.isPresent()) {
			valid = false;

			final Optional<String> currentUserIdOpt = Optional.of(SecurityContextHolder.getContext())
					.map(SecurityContext::getAuthentication)
					.map(Authentication::getPrincipal)
					.map(object -> {
						if (object instanceof Jwt) {
							return ((Jwt) object).getClaim("sub");
						}
						return null;
					})
					.map(String::valueOf);

			if (currentUserIdOpt.isPresent()) {
				final String currentUserId = currentUserIdOpt.get();

				if (updateTopicDto.getId() != null) {

					final Topic oldTopic = oldTopicOpt.get();
					if (StringUtils.equals(currentUserId, oldTopic.getCreatorUser())) {
						valid = true;
					}
				}
			}
		}

		return valid;
	}
}