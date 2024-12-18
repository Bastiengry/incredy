package fr.bgsoft.incredy.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import fr.bgsoft.incredy.dto.topic.ResponseMessageDto;
import fr.bgsoft.incredy.dto.topic.ResponseObjectDto;
import fr.bgsoft.incredy.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	// @ExceptionHandler(value = { IllegalArgumentException.class,
	// IllegalStateException.class })
	// protected ResponseEntity<Object> handleConflict(
	// final RuntimeException ex, final WebRequest request) {
	// final String bodyOfResponse = "A non expected exception was thrown";
	// return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(),
	// HttpStatus.CONFLICT, request);
	// }

	@ExceptionHandler(value = { EntityNotFoundException.class })
	protected ResponseEntity<Object> handleEntityNotFoundException(
			final Exception ex, final WebRequest request) {
		final ResponseMessageDto responseMessageDto = ResponseMessageDto.builder()
				.type("ERROR")
				.code("ENTITY_NOT_FOUND")
				.message(ex.getMessage())
				.build();
		final ResponseObjectDto responseObjectDto = ResponseObjectDto.builder().messages(List.of(responseMessageDto)).build();
		return new ResponseEntity<>(responseObjectDto, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
		final List<ResponseMessageDto> messages = ex.getBindingResult().getAllErrors().stream()
				.map(error -> ResponseMessageDto.builder()
						.type("ERROR")
						.code(error.getCode())
						.message(error.getDefaultMessage())
						.build())
				.collect(Collectors.toList());

		final ResponseObjectDto responseObjectDto = ResponseObjectDto.builder().messages(messages).build();
		return new ResponseEntity<>(responseObjectDto, HttpStatus.BAD_REQUEST);
	}
}
