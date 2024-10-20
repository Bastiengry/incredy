package fr.bgsoft.incredy.dto.topic;

import java.io.Serializable;

import fr.bgsoft.incredy.valid.annotation.UpdateTopicDtoOldTopicExistValidatorAnnotation;
import fr.bgsoft.incredy.valid.annotation.UpdateTopicDtoUserAuthorizedValidatorAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@UpdateTopicDtoOldTopicExistValidatorAnnotation
@UpdateTopicDtoUserAuthorizedValidatorAnnotation
public class UpdateTopicDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	private Long id;

	@NotBlank(message = "{UpdateTopicDto.title.NotBlank}")
	@Size(max = 256, message = "{UpdateTopicDto.title.Size}")
	private String title;

	@NotBlank(message = "{UpdateTopicDto.text.NotBlank}")
	private String text;
}
