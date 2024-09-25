package fr.bgsoft.incredy.controller;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.bgsoft.incredy.config.SecurityConfiguration;
import fr.bgsoft.incredy.dto.topic.CreateTopicDto;
import fr.bgsoft.incredy.dto.topic.TopicDto;
import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.entity.Topic;
import fr.bgsoft.incredy.repository.TopicRepository;
import fr.bgsoft.incredy.service.TopicService;

@Import({ SecurityConfiguration.class })
@WebMvcTest(TopicController.class)
public class TopicControllerTest {
	private static final String TOPIC_CONTROLLER_URL = "/api/v1/topic";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TopicService service;

	@MockBean
	private TopicRepository topicRepository;

	private String convertJson(final Object object) throws JsonProcessingException {
		final ObjectMapper ow = new ObjectMapper();
		return ow.writeValueAsString(object);
	}

	@Test
	void shouldBePossibleToReadAllTopics() throws Exception {
		final List<TopicDto> allTopicDtos = List.of(
				TopicDto.builder().id(1L).title("title1").text("text1").build(),
				TopicDto.builder().id(2L).title("title2").text("text2").build());

		final ResponseObjectDto responseObjectDto = ResponseObjectDto.builder().data(allTopicDtos).build();

		when(service.getAll()).thenReturn(allTopicDtos);
		mockMvc.perform(MockMvcRequestBuilders.get(TOPIC_CONTROLLER_URL))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.content().json(convertJson(responseObjectDto)));
	}

	@Test
	void shouldBePossibleToCreateTopicWhenConnected() throws Exception {
		final String userId = "MY_SUB";

		final CreateTopicDto topicToCreate = CreateTopicDto.builder().title("title1").text("text1").build();

		mockMvc.perform(MockMvcRequestBuilders.post(TOPIC_CONTROLLER_URL)
				.with(SecurityMockMvcRequestPostProcessors.jwt()
						.jwt(jwt -> jwt.claim(StandardClaimNames.SUB, userId)))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(convertJson(topicToCreate)))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isCreated());

		verify(service, times(1)).create(topicToCreate);
	}

	@Test
	void shouldNotBePossibleToCreateTopicWhenNotConnected() throws Exception {
		final CreateTopicDto topicToCreate = CreateTopicDto.builder().title("title1").text("text1").build();

		mockMvc.perform(MockMvcRequestBuilders.post(TOPIC_CONTROLLER_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(convertJson(topicToCreate)))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isForbidden());

		verify(service, never()).create(topicToCreate);
	}

	@Test
	void shouldBePossibleToUpdateTopicWhenConnected() throws Exception {
		final long topicId = 1L;
		final String userId = "MY_SUB";
		final UpdateTopicDto topicToUpdate = UpdateTopicDto.builder().id(topicId).title("title1").text("text1").build();

		final Topic oldTopic = Topic.builder().id(topicId).creatorUser(userId).build();
		when(topicRepository.findById(topicId)).thenReturn(Optional.of(oldTopic));

		mockMvc.perform(MockMvcRequestBuilders.put(TOPIC_CONTROLLER_URL + "/" + topicId)
				.with(SecurityMockMvcRequestPostProcessors.jwt()
						.jwt(jwt -> jwt.claim(StandardClaimNames.SUB, userId)))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(convertJson(topicToUpdate)))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk());

		verify(service, times(1)).update(topicId, topicToUpdate);
	}

	@Test
	void shouldNotBePossibleToUpdateNonExistingTopic() throws Exception {
		final long topicId = 1L;
		final String userId = "MY_SUB";
		final UpdateTopicDto topicToUpdate = UpdateTopicDto.builder().id(topicId).title("title1").text("text1").build();

		when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

		final ResponseObjectDto responseObjectDto = ResponseObjectDto.builder()
				.messages(List.of(
						ResponseMessageDto.builder().code("UpdateTopicDtoOldTopicExistValidatorAnnotation")
						.message("Old topic should exist").build()))
				.build();

		mockMvc.perform(MockMvcRequestBuilders.put(TOPIC_CONTROLLER_URL + "/" + topicId)
				.with(SecurityMockMvcRequestPostProcessors.jwt()
						.jwt(jwt -> jwt.claim(StandardClaimNames.SUB, userId)))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(convertJson(topicToUpdate)))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().json(convertJson(responseObjectDto)));

		verify(service, never()).update(topicId, topicToUpdate);
	}

	@Test
	void shouldNotBePossibleToUpdateTopicOfOtherUser() throws Exception {
		final long topicId = 1L;
		final String userId = "MY_SUB";
		final UpdateTopicDto topicToUpdate = UpdateTopicDto.builder().id(topicId).title("title1").text("text1").build();

		final Topic oldTopic = Topic.builder().id(topicId).title("title0").text("text0").creatorUser("CREATOR").build();
		when(topicRepository.findById(topicId)).thenReturn(Optional.of(oldTopic));

		final ResponseObjectDto responseObjectDto = ResponseObjectDto.builder()
				.messages(List.of(
						ResponseMessageDto.builder().code("UpdateTopicDtoUserAuthorizedValidatorAnnotation")
						.message("User not allowed").build()))
				.build();

		mockMvc.perform(MockMvcRequestBuilders.put(TOPIC_CONTROLLER_URL + "/" + topicId)
				.with(SecurityMockMvcRequestPostProcessors.jwt()
						.jwt(jwt -> jwt.claim(StandardClaimNames.SUB, userId)))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(convertJson(topicToUpdate)))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().json(convertJson(responseObjectDto)));

		verify(service, never()).update(topicId, topicToUpdate);
	}

	@Test
	void shouldNotBePossibleToUpdateTopicWhenNotConnected() throws Exception {
		final long topicId = 1L;
		final String userId = "MY_SUB";
		final UpdateTopicDto topicToUpdate = UpdateTopicDto.builder().id(topicId).title("title1").text("text1").build();

		final Topic oldTopic = Topic.builder().id(topicId).creatorUser(userId).build();
		when(topicRepository.findById(topicId)).thenReturn(Optional.of(oldTopic));

		mockMvc.perform(MockMvcRequestBuilders.put(TOPIC_CONTROLLER_URL + "/" + topicId)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(convertJson(topicToUpdate)))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isForbidden());

		verify(service, never()).update(topicId, topicToUpdate);
	}

	@Test
	void shouldBePossibleToDeleteTopicWhenConnected() throws Exception {
		final long topicId = 1L;
		final String userId = "MY_SUB";

		mockMvc.perform(MockMvcRequestBuilders.delete(TOPIC_CONTROLLER_URL + "/" + topicId)
				.with(SecurityMockMvcRequestPostProcessors.jwt()
						.jwt(jwt -> jwt.claim(StandardClaimNames.SUB, userId))))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk());

		verify(service, times(1)).delete(topicId);
	}

	@Test
	void shouldNotBePossibleToDeleteTopicWhenNotConnected() throws Exception {
		final long topicId = 1L;

		mockMvc.perform(MockMvcRequestBuilders.delete(TOPIC_CONTROLLER_URL + "/" + topicId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isForbidden());

		verify(service, never()).delete(topicId);
	}
}
