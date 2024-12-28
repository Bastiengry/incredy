package fr.bgsoft.incredy.valid;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.bgsoft.incredy.databuilder.TopicBuilder;
import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.entity.Topic;
import fr.bgsoft.incredy.repository.TopicRepository;

@ExtendWith(MockitoExtension.class)
public class UpdateTopicDtoOldTopicExistValidatorTest {
	@Mock
	private TopicRepository topicRepository;

	@InjectMocks
	private UpdateTopicDtoOldTopicExistValidator validator;

	@Test
	public void shouldSucceedIfOldTopicExistsInDB() {
		final Topic existingTopic = TopicBuilder.buildTopic1();
		existingTopic.setText("MY EXISTING TEXT");

		final UpdateTopicDto updatedTopicDto = TopicBuilder.getUpdateTopicDtoForTopic(existingTopic);
		updatedTopicDto.setText("MY UPDATED TEXT");

		when(topicRepository.findById(updatedTopicDto.getId())).thenReturn(Optional.of(existingTopic));

		assertTrue(validator.isValid(updatedTopicDto, null));
	}

	@Test
	public void shouldFailIfOldTopicDoesNOTExistsInDB() {
		final UpdateTopicDto updatedTopicDto = TopicBuilder.buildUpdateTopicDto1();

		when(topicRepository.findById(updatedTopicDto.getId())).thenReturn(Optional.empty());

		assertFalse(validator.isValid(updatedTopicDto, null));
	}
}
