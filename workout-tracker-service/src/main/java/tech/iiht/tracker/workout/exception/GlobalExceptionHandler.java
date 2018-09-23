package tech.iiht.tracker.workout.exception;

import java.net.URI;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tech.iiht.tracker.workout.bean.ApiResponse;
import tech.iiht.tracker.workout.bean.User;
import tech.iiht.tracker.workout.constant.Constants;

@Component
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Lookup
	public ApiResponse getApiResponse() {
		return null; // Dynamic prototype bean
	}

	@ExceptionHandler({ WorkoutTrackerException.class, Exception.class })
	public final ResponseEntity<ApiResponse> handleApplicationException(final Exception ex) {
		LOGGER.warn(ex.getMessage(), ex.getCause());
		ApiResponse output = getApiResponse().addMessage(ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(output);
	}

	@ExceptionHandler({ BadCredentialsException.class, CredentialsExpiredException.class })
	public final ResponseEntity<?> handleAuthenticationException(final AuthenticationException ex) {

		LOGGER.error("Authentiication Failed {}", ex.getMessage());

		String location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/login").toUriString();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).location(URI.create(location))
				.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.POST.name())
				.header(HttpHeaders.WWW_AUTHENTICATE, "Bearer").build();
	}

	@ExceptionHandler({ IllegalArgumentException.class })
	public final ResponseEntity<ApiResponse> handleBadRequestException(final RuntimeException ex) {
		LOGGER.warn(ex.getMessage(), ex.getCause());
		ApiResponse output = getApiResponse().addMessage(ex.getMessage());
		return ResponseEntity.badRequest().body(output);
	}

	@ExceptionHandler({ EmptyResultDataAccessException.class, NoSuchElementException.class })
	public final ResponseEntity<ApiResponse> handleNoContentException(final RuntimeException ex) {
		LOGGER.warn(ex.getMessage(), ex.getCause());
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<ApiResponse> handleValidationException(final ConstraintViolationException ex) {

		ApiResponse output = getApiResponse();
		LOGGER.error("Data validation Failure :: {}", ex.getMessage());

		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {

			LOGGER.error(violation.toString());
			Iterator<Node> iterator = violation.getPropertyPath().iterator();
			while (iterator.hasNext()) {

				String fieldname = iterator.next().getName();

				if (violation.getRootBeanClass().equals(User.class) && "password".equals(fieldname)) {
					output.addMessage(Constants.PASSWORD_FORMAT_ERROR);
				} else {
					output.addMessage(fieldname + Constants.SPACE + violation.getMessage());
				}
			}
		}
		return ResponseEntity.badRequest().body(output);
	}

}
