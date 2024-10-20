package fr.bgsoft.incredy.service;

import java.util.List;

import fr.bgsoft.incredy.dto.topic.CreateTopicDto;
import fr.bgsoft.incredy.dto.topic.TopicDto;
import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.exception.EntityNotFoundException;

public interface TopicService {
	List<TopicDto> getAll();

	TopicDto getById(Long id) throws EntityNotFoundException;

	void create(CreateTopicDto createTopicDto);

	void update(Long id, UpdateTopicDto updateTopicDto) throws EntityNotFoundException;

	void delete(Long id);
}
