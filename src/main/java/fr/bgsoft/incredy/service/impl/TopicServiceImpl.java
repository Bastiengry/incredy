package fr.bgsoft.incredy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.bgsoft.incredy.dto.topic.CreateTopicDto;
import fr.bgsoft.incredy.dto.topic.TopicDto;
import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.entity.Topic;
import fr.bgsoft.incredy.exception.EntityNotFoundException;
import fr.bgsoft.incredy.mapper.TopicMapper;
import fr.bgsoft.incredy.repository.TopicRepository;
import fr.bgsoft.incredy.service.TopicService;

@Service
public class TopicServiceImpl implements TopicService {
	private final TopicRepository topicRepository;
	private final TopicMapper topicMapper;

	@Autowired
	public TopicServiceImpl(final TopicRepository topicRepository, final TopicMapper topicMapper) {
		this.topicRepository = topicRepository;
		this.topicMapper = topicMapper;
	}

	@Override
	public List<TopicDto> getAll() {
		final List<TopicDto> topicDtos = new ArrayList<>();

		topicDtos.addAll(topicRepository.findAll()
				.stream()
				.map(topicMapper::entityToDto)
				.collect(Collectors.toList()));

		return topicDtos;
	}

	@Override
	public TopicDto getById(final Long id) throws EntityNotFoundException {
		return Optional.ofNullable(id)
				.flatMap(topicRepository::findById)
				.map(topicMapper::entityToDto)
				.orElseThrow(() -> new EntityNotFoundException("Topic " + id + " not found"));
	}

	@Override
	public void create(final CreateTopicDto createTopicDto) {
		Optional.ofNullable(createTopicDto)
		.map(topicMapper::createDtoToEntity)
		.map(topicRepository::save);
	}

	@Override
	public void update(final Long id, final UpdateTopicDto updateTopicDto) throws EntityNotFoundException {
		if ((id != null) && (updateTopicDto != null)) {
			final Optional<Topic> entityOpt = Optional.ofNullable(id)
					.flatMap(topicRepository::findById);

			if (entityOpt.isPresent()) {
				final Topic topic = entityOpt.get();
				final Topic updatedTopic = topicMapper.updateDtoToEntity(topic, updateTopicDto);
				topicRepository.save(updatedTopic);
			} else {
				throw new EntityNotFoundException("Topic " + id + " not found");
			}
		}
	}

	@Override
	public void delete(final Long id) {
		Optional.ofNullable(id)
		.ifPresent(topicRepository::deleteById);
	}
}
