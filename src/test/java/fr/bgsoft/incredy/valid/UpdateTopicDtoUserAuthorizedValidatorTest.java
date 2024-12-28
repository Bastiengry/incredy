package fr.bgsoft.incredy.valid;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.bgsoft.incredy.databuilder.TopicBuilder;
import fr.bgsoft.incredy.dto.topic.UpdateTopicDto;
import fr.bgsoft.incredy.entity.Topic;
import fr.bgsoft.incredy.repository.TopicRepository;

//@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class UpdateTopicDtoUserAuthorizedValidatorTest {
	@Mock
	private TopicRepository topicRepository;

	@Mock
	SecurityContext securityContext;

	@Mock
	Authentication authentication;

	@Mock
	Jwt jwt;

	@InjectMocks
	private UpdateTopicDtoUserAuthorizedValidator validator;

	@Test
	public void shouldSucceedIfUserIsAuthorized() {
		final Topic existingTopic = TopicBuilder.buildTopic1();
		existingTopic.setText("MY EXISTING TEXT");

		final UpdateTopicDto updatedTopicDto = TopicBuilder.getUpdateTopicDtoForTopic(existingTopic);
		updatedTopicDto.setText("MY UPDATED TEXT");

		// Mocks the repository.
		when(topicRepository.findById(updatedTopicDto.getId())).thenReturn(Optional.of(existingTopic));

		// Mocks the authentication.
		when(jwt.getClaim("sub")).thenReturn(existingTopic.getCreatorUser());
		when(authentication.getPrincipal()).thenReturn(jwt);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		// Launches the test.
		assertTrue(validator.isValid(updatedTopicDto, null));
	}

	@Test
	public void shouldFailIfNoSubInUserAuthentication() {
		final Topic existingTopic = TopicBuilder.buildTopic1();
		existingTopic.setText("MY EXISTING TEXT");

		final UpdateTopicDto updatedTopicDto = TopicBuilder.getUpdateTopicDtoForTopic(existingTopic);
		updatedTopicDto.setText("MY UPDATED TEXT");

		// Mocks the repository.
		when(topicRepository.findById(updatedTopicDto.getId())).thenReturn(Optional.of(existingTopic));

		// Mocks the authentication.
		when(authentication.getPrincipal()).thenReturn(jwt);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		// Launches the test.
		assertFalse(validator.isValid(updatedTopicDto, null));
	}

	@Test
	public void shouldFailIfNoJwtAuthentication() {
		final Topic existingTopic = TopicBuilder.buildTopic1();
		existingTopic.setText("MY EXISTING TEXT");

		final UpdateTopicDto updatedTopicDto = TopicBuilder.getUpdateTopicDtoForTopic(existingTopic);
		updatedTopicDto.setText("MY UPDATED TEXT");

		// Mocks the repository.
		when(topicRepository.findById(updatedTopicDto.getId())).thenReturn(Optional.of(existingTopic));

		// Mocks the authentication.
		when(authentication.getPrincipal()).thenReturn(new Object());
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		// Launches the test.
		assertFalse(validator.isValid(updatedTopicDto, null));
	}

	@Test
	public void shouldFailIfUserIsNOTAuthorized() {
		final Topic existingTopic = TopicBuilder.buildTopic1();
		existingTopic.setText("MY EXISTING TEXT");

		final UpdateTopicDto updatedTopicDto = TopicBuilder.getUpdateTopicDtoForTopic(existingTopic);
		updatedTopicDto.setText("MY UPDATED TEXT");

		// Mocks the repository.
		when(topicRepository.findById(updatedTopicDto.getId())).thenReturn(Optional.of(existingTopic));

		// Mocks the authentication.
		when(jwt.getClaim("sub")).thenReturn("UNAUTHORIZED_USER");
		when(authentication.getPrincipal()).thenReturn(jwt);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		// Launches the test.
		assertFalse(validator.isValid(updatedTopicDto, null));
	}

	@Test
	public void shouldFailIfUserIsNOTConnected() {
		final Topic existingTopic = TopicBuilder.buildTopic1();
		existingTopic.setText("MY EXISTING TEXT");

		final UpdateTopicDto updatedTopicDto = TopicBuilder.getUpdateTopicDtoForTopic(existingTopic);
		updatedTopicDto.setText("MY UPDATED TEXT");

		// Mocks the repository.
		when(topicRepository.findById(updatedTopicDto.getId())).thenReturn(Optional.of(existingTopic));

		// Launches the test.
		assertFalse(validator.isValid(updatedTopicDto, null));
	}

	@Test
	public void shouldSucceedIfTopicDoesNOTExistBecauseManagedByOtherAnnotation() {
		final UpdateTopicDto updatedTopicDto = TopicBuilder.buildUpdateTopicDto1();

		// Mocks the repository.
		when(topicRepository.findById(updatedTopicDto.getId())).thenReturn(Optional.empty());

		// Launches the test.
		// The scenario of missing topic is managed by an other annotation, so no
		// failure here.
		assertTrue(validator.isValid(updatedTopicDto, null));
	}
}
