package fr.bgsoft.incredy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.bgsoft.incredy.config.ExtraTag;
import fr.bgsoft.incredy.dto.topic.CreateTopicDto;
import fr.bgsoft.incredy.dto.topic.TopicDto;
import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.exception.EntityNotFoundException;
import fr.bgsoft.incredy.service.TopicService;
import io.micrometer.core.annotation.Counted;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/topic")
public class TopicController {
	private final TopicService topicService;

	@Autowired
	public TopicController(final TopicService topicService) {
		this.topicService = topicService;
	}

	@GetMapping("")
	public ResponseEntity<ResponseObjectDto> getAll() throws EntityNotFoundException {
		final List<TopicDto> topicDtos = topicService.getAll();
		final ResponseObjectDto responseObjectDto = ResponseObjectDto.builder().data(topicDtos).build();
		return ResponseEntity.ofNullable(responseObjectDto);
	}

	@GetMapping("/{id}")
	@Counted(value = "counted.topic.getById")
	public ResponseEntity<ResponseObjectDto> getById(@ExtraTag("id") @PathVariable("id") final long id)
			throws EntityNotFoundException {
		final TopicDto topicDto = topicService.getById(id);
		final ResponseObjectDto responseObjectDto = ResponseObjectDto.builder().data(topicDto).build();
		return ResponseEntity.ofNullable(responseObjectDto);
	}

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody @Valid final CreateTopicDto createTopicDto) {
		topicService.create(createTopicDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable("id") final Long id,
			@RequestBody @Valid final UpdateTopicDto updateTopicDto)
					throws EntityNotFoundException {
		topicService.update(id, updateTopicDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
		topicService.delete(id);
		return ResponseEntity.ok().build();
	}
}
