package fr.bgsoft.incredy.dto.topic;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TopicDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String title;

	private String text;

	private Instant createdDate;

	private Instant lastModifiedDate;

	private String creatorUser;
}
