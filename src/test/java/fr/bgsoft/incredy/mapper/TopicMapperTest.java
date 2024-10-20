package fr.bgsoft.incredy.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.bgsoft.incredy.databuilder.TopicBuilder;
import fr.bgsoft.incredy.dto.topic.CreateTopicDto;
import fr.bgsoft.incredy.dto.topic.TopicDto;
import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.entity.Topic;

@SpringBootTest(classes = { TopicMapperImpl.class })
public class TopicMapperTest {

	@Autowired
	private TopicMapper topicMapper;

	@Test
	public void shouldBeAbleToGetDtoFromEntity() {
		final Topic topic = TopicBuilder.buildTopic1();
		final TopicDto topicDto = TopicBuilder.getTopicDtoForTopic(topic);
		final TopicDto mapperTopicDto = topicMapper.entityToDto(topic);
		assertEquals(topicDto, mapperTopicDto);
	}

	@Test
	public void shouldReturnNullDtoIfNullEntity() {
		assertNull(topicMapper.entityToDto(null));
	}

	@Test
	public void shouldBeAbleToGetEntityFromCreateDto() {
		final CreateTopicDto createTopicDto = TopicBuilder.buildCreateTopicDto1();
		final Topic topic = TopicBuilder.buildTopicFromCreateTopicDto(createTopicDto);

		final Topic mapperTopic = topicMapper.createDtoToEntity(createTopicDto);

		// Does an equals without implementing to equals/hash code (lombok
		// EqualsAndHashCode is discouraged on JPA entities)
		assertThat(mapperTopic).usingRecursiveComparison().isEqualTo(topic);
	}

	@Test
	public void shouldReturnNullEntityIfNullCreateDto() {
		assertNull(topicMapper.createDtoToEntity(null));
	}

	@Test
	public void shouldBeAbleToGetEntityFromUpdateDto() {
		final Topic oldTopic = TopicBuilder.buildTopic1();

		final UpdateTopicDto updateTopicDto = TopicBuilder.getUpdateTopicDtoForTopic(oldTopic);
		updateTopicDto.setText(updateTopicDto.getText() + " - complementary text");
		updateTopicDto.setTitle(updateTopicDto.getTitle() + " - complementary title");

		// Creates a similar topic than the old, then overrides what is needed to
		// override
		// (be careful, update DTO does not contain all the fields so the first line
		// allows us to get the missing fields).
		final Topic updatedTopic = TopicBuilder.buildTopic1();
		updatedTopic.setText(updateTopicDto.getText());
		updatedTopic.setTitle(updateTopicDto.getTitle());

		final Topic mapperTopic = topicMapper.updateDtoToEntity(oldTopic, updateTopicDto);

		// Does an equals without implementing to equals/hash code (lombok
		// EqualsAndHashCode is discouraged on JPA entities)
		assertThat(mapperTopic).usingRecursiveComparison().isEqualTo(updatedTopic);
	}

	@Test
	public void shouldReturnExistingEntityIfNullUpdateDto() {
		final Topic oldTopic = TopicBuilder.buildTopic1();
		final Topic mapperTopic = topicMapper.updateDtoToEntity(oldTopic, null);

		// Does an equals without implementing to equals/hash code (lombok
		// EqualsAndHashCode is discouraged on JPA entities)
		assertThat(mapperTopic).usingRecursiveComparison().isEqualTo(oldTopic);
	}
}
