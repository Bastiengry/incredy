package fr.bgsoft.incredy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "entity not found")
public class EntityNotFoundException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(final String message) {
		super(message);
	}

	public EntityNotFoundException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
}