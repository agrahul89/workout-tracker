package tech.iiht.tracker.workout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class WorkoutTrackerException extends RuntimeException {

	private static final long serialVersionUID = 3050330395983498145L;

	public WorkoutTrackerException(String message) {
		super(message);
	}

	public WorkoutTrackerException(String message, Throwable cause) {
		super(message, cause);
	}
}
