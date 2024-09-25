package fr.bgsoft.incredy.databuilder;

import java.time.Instant;

import fr.bgsoft.incredy.dto.topic.CreateTopicDto;
import fr.bgsoft.incredy.dto.topic.TopicDto;
import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.entity.Topic;

public class TopicBuilder {
	public static Topic buildTopic1() {
		return Topic.builder().id(1L).title("title1").text("text1")
				.createdDate(Instant.parse("2024-09-03T10:15:30.00Z")).creatorUser("user1").build();
	}

	public static Topic buildTopic2() {
		return Topic.builder().id(2L).title("title2").text("text2")
				.createdDate(Instant.parse("2024-09-11T09:15:28.00Z")).creatorUser("user2")
				.lastModifiedDate(Instant.parse("2024-09-15T09:15:28.00Z")).build();
	}

	public static Topic buildTopicFromCreateTopicDto(final CreateTopicDto createTopicDto) {
		return Topic.builder().id(1L).title(createTopicDto.getTitle()).text(createTopicDto.getText()).build();
	}

	public static TopicDto getTopicDtoForTopic(final Topic topic) {
		return TopicDto.builder().id(topic.getId()).title(topic.getTitle()).text(topic.getText())
				.createdDate(topic.getCreatedDate()).creatorUser(topic.getCreatorUser())
				.lastModifiedDate(topic.getLastModifiedDate()).build();
	}

	public static CreateTopicDto getCreateTopicDtoForTopic(final Topic topic) {
		return CreateTopicDto.builder().title(topic.getTitle()).text(topic.getText()).build();
	}

	public static UpdateTopicDto getUpdateTopicDtoForTopic(final Topic topic) {
		return UpdateTopicDto.builder().id(topic.getId()).title(topic.getTitle()).text(topic.getText()).build();
	}

	public static Topic buildTopicFromUpdateTopicDto(final UpdateTopicDto updateTopicDto1) {
		return Topic.builder().id(1L).title(updateTopicDto1.getTitle()).text(updateTopicDto1.getText())
				.build();
	}
}
