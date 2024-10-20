package fr.bgsoft.incredy.dto.topic;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateTopicDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "{CreateTopicDto.title.NotBlank}")
	@Size(max = 256, message = "{CreateTopicDto.title.Size}")
	private String title;

	@NotBlank(message = "{CreateTopicDto.text.NotBlank}")
	private String text;
}
