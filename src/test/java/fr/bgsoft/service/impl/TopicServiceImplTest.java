package fr.bgsoft.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.bgsoft.incredy.databuilder.TopicBuilder;
import fr.bgsoft.incredy.dto.topic.CreateTopicDto;
import fr.bgsoft.incredy.dto.topic.TopicDto;
import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.entity.Topic;
import fr.bgsoft.incredy.exception.EntityNotFoundException;
import fr.bgsoft.incredy.mapper.TopicMapper;
import fr.bgsoft.incredy.repository.TopicRepository;
import fr.bgsoft.incredy.service.impl.TopicServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TopicServiceImplTest {
	@Mock
	private TopicRepository topicRepository;

	@Mock
	private TopicMapper topicMapper;

	@InjectMocks
	private TopicServiceImpl topicServiceImpl;

	@Test
	public void shouldBePossibleToGetAllTopics() {
		final Topic topic1 = TopicBuilder.buildTopic1();
		final Topic topic2 = TopicBuilder.buildTopic2();

		final TopicDto topicDto1 = TopicBuilder.getTopicDtoForTopic(topic1);
		final TopicDto topicDto2 = TopicBuilder.getTopicDtoForTopic(topic2);

		final List<Topic> allTopics = List.of(topic1, topic2);
		final List<TopicDto> allTopicDtos = List.of(topicDto1, topicDto2);

		when(topicRepository.findAll()).thenReturn(allTopics);
		when(topicMapper.entityToDto(topic1)).thenReturn(topicDto1);
		when(topicMapper.entityToDto(topic2)).thenReturn(topicDto2);

		final List<TopicDto> resultDtos = topicServiceImpl.getAll();
		assertThat(resultDtos).isEqualTo(allTopicDtos);
	}

	@Test
	public void shouldBePossibleToGetTopicById() throws EntityNotFoundException {
		final Topic topic1 = TopicBuilder.buildTopic1();

		final TopicDto topicDto1 = TopicBuilder.getTopicDtoForTopic(topic1);

		when(topicRepository.findById(topic1.getId())).thenReturn(Optional.of(topic1));
		when(topicMapper.entityToDto(topic1)).thenReturn(topicDto1);

		final TopicDto resultDto = topicServiceImpl.getById(topic1.getId());
		assertThat(resultDto).isEqualTo(topicDto1);
	}

	@Test
	public void shouldThrowAnExceptionIfNoTopicWithGivenId() {
		final long incorrectTopicId = 121515151L;

		when(topicRepository.findById(incorrectTopicId)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> {
			topicServiceImpl.getById(incorrectTopicId);
		});
	}

	@Test
	public void shouldBePossibleToCreateTopic() {
		final Topic topic1 = TopicBuilder.buildTopic1();
		final CreateTopicDto createTopicDto1 = TopicBuilder.getCreateTopicDtoForTopic(topic1);
		final Topic preCreatedTopic1 = TopicBuilder.buildTopicFromCreateTopicDto(createTopicDto1);

		when(topicMapper.createDtoToEntity(createTopicDto1)).thenReturn(preCreatedTopic1);
		when(topicRepository.save(preCreatedTopic1)).thenReturn(topic1);

		topicServiceImpl.create(createTopicDto1);

		verify(topicRepository, times(1)).save(preCreatedTopic1);
	}

	@Test
	public void shouldBePossibleToUpdateExistingTopic() throws EntityNotFoundException {
		final Topic topic1 = TopicBuilder.buildTopic1();
		final UpdateTopicDto updateTopicDto1 = TopicBuilder.getUpdateTopicDtoForTopic(topic1);
		updateTopicDto1.setText("new_" + updateTopicDto1.getText());
		updateTopicDto1.setTitle("new_" + updateTopicDto1.getTitle());
		final Topic preUpdatedTopic1 = TopicBuilder.buildTopicFromUpdateTopicDto(updateTopicDto1);

		when(topicRepository.findById(updateTopicDto1.getId())).thenReturn(Optional.of(topic1));

		when(topicMapper.updateDtoToEntity(topic1, updateTopicDto1)).thenReturn(preUpdatedTopic1);
		when(topicRepository.save(preUpdatedTopic1)).thenReturn(preUpdatedTopic1);

		topicServiceImpl.update(updateTopicDto1.getId(), updateTopicDto1);

		verify(topicRepository, times(1)).save(preUpdatedTopic1);
	}

	@Test
	public void shouldNOTBePossibleToUpdateANonExistingTopic() throws EntityNotFoundException {
		final Topic topic1 = TopicBuilder.buildTopic1();
		final UpdateTopicDto updateTopicDto1 = TopicBuilder.getUpdateTopicDtoForTopic(topic1);

		when(topicRepository.findById(updateTopicDto1.getId())).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> {
			topicServiceImpl.update(updateTopicDto1.getId(), updateTopicDto1);
		});
	}

	@Test
	public void shouldBePossibleToDeleteExistingTopic() {
		final long id = 1L;

		doNothing().when(topicRepository).deleteById(id);

		topicServiceImpl.delete(id);

		verify(topicRepository, times(1)).deleteById(id);
	}

	@Test
	public void deletingNonExistingTopicShouldNotCrashTheApp() {
		final long id = 1L;

		doNothing().when(topicRepository).deleteById(id);

		topicServiceImpl.delete(id);

		verify(topicRepository, times(1)).deleteById(id);
	}

	@Test
	public void deletingWithNullIdShouldNotCrashTheApp() {
		topicServiceImpl.delete(null);

		verify(topicRepository, never()).deleteById(null);
	}
}
