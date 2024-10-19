package fr.bgsoft.incredy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import fr.bgsoft.incredy.dto.topic.CreateTopicDto;
import fr.bgsoft.incredy.dto.topic.TopicDto;
import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.entity.Topic;

@Mapper(componentModel = "spring")
public interface TopicMapper {
	TopicDto entityToDto(Topic topic);

	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "createdDate", ignore = true),
		@Mapping(target = "lastModifiedDate", ignore = true),
		@Mapping(target = "creatorUser", ignore = true),
	})
	Topic createDtoToEntity(CreateTopicDto createTopicDto);

	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "createdDate", ignore = true),
		@Mapping(target = "lastModifiedDate", ignore = true),
		@Mapping(target = "creatorUser", ignore = true),
	})
	Topic updateDtoToEntity(@MappingTarget Topic topic, UpdateTopicDto updateTopicDto);
}
