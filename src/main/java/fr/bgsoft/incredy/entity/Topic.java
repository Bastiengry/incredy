package fr.bgsoft.incredy.entity;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "topic")
@Builder
public class Topic implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topic_sequence")
	@SequenceGenerator(name = "topic_sequence", sequenceName = "topic_sequence", allocationSize = 1, initialValue = 1)
	private long id;

	@Column(name = "title", length = 256, nullable = false)
	private String title;

	@Column(name = "text", nullable = false)
	private String text;

	@CreatedDate
	@Column(name = "created_date", nullable = false, updatable = false)
	private Instant createdDate;

	@LastModifiedDate
	@Column(name = "last_modified_date", nullable = false)
	private Instant lastModifiedDate;

	@CreatedBy
	@Column(name = "creator_user", nullable = false, updatable = false)
	private String creatorUser;
}
