package fr.bgsoft.incredy.valid;

import java.util.Optional;

import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.repository.TopicRepository;
import fr.bgsoft.incredy.valid.annotation.UpdateTopicDtoOldTopicExistValidatorAnnotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateTopicDtoOldTopicExistValidator
implements ConstraintValidator<UpdateTopicDtoOldTopicExistValidatorAnnotation, UpdateTopicDto> {

	private final TopicRepository topicRepository;

	public UpdateTopicDtoOldTopicExistValidator(final TopicRepository topicRepository) {
		this.topicRepository = topicRepository;
	}

	@Override
	public boolean isValid(final UpdateTopicDto updateTopicDto, final ConstraintValidatorContext context) {
		return Optional.ofNullable(updateTopicDto)
				.map(UpdateTopicDto::getId)
				.flatMap(topicRepository::findById)
				.isPresent();
	}
}