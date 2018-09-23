package tech.iiht.tracker.workout.controller;

import java.net.URI;
import java.text.MessageFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tech.iiht.tracker.workout.bean.User;
import tech.iiht.tracker.workout.bean.Workout;
import tech.iiht.tracker.workout.constant.Constants;
import tech.iiht.tracker.workout.exception.WorkoutTrackerException;
import tech.iiht.tracker.workout.service.CategoryService;
import tech.iiht.tracker.workout.service.WorkoutService;

@RestController
public class WorkoutController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkoutController.class);

	private @Resource CategoryService categoryService;
	private @Resource WorkoutService workoutService;

	@PostMapping(path = "/workout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> createWorkout(@RequestBody(required = true) Workout workout,
			final @RequestAttribute("user") User user) {

		workout.putUserid(user.getId())
				.putCategoryid(workout.getCategory() == null ? null : workout.getCategory().getId());
		LOGGER.info("Create Workout Request by User ID {} :: {}", user.getId(), workout);

		Workout output = workoutService.create(workout);
		if (output == null || output.getId() == null || output.getId() <= 0) {
			throw new WorkoutTrackerException("Failed to create Workout");
		}

		LOGGER.info("Workout [{}] created successfully", output.getId());
		LOGGER.debug(output.toString());

		String location = MessageFormat.format("/workout/{0}", output.getId());
		location = ServletUriComponentsBuilder.fromCurrentContextPath().path(location).toUriString();
		LOGGER.info("Created Workout Location :: {}", location);

		return ResponseEntity.created(URI.create(location))
				.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.GET.name())
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name()).build();
	}

	@DeleteMapping(path = "/workout/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> deleteWorkout(@PathVariable("id") final Integer id) {

		LOGGER.info("Marking Workout {} as deleted", id);
		ResponseEntity<Workout> response = this.retrieveWorkout(id);

		if (!response.hasBody()) {
			return ResponseEntity.notFound().build();
		}

		workoutService.update(response.getBody().putDeleted(new Date()));
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/workout/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Workout> retrieveWorkout(@PathVariable("id") final Integer id) {

		LOGGER.info("Retrieving Workout by id :: {}", id);
		Workout output = workoutService.get(id);
		LOGGER.debug("Output :: {}", String.valueOf(output));

		return ResponseEntity.ok(output);
	}

	@GetMapping(path = "/workout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Workout>> retrieveWorkouts(final @RequestAttribute("user") User user) {

		LOGGER.debug("Retrieving workouts created by User ID :: {}", user.getId());
		List<Workout> output = workoutService.getAllCreatedBy(user);

		LOGGER.info("Workouts created by userid {} :: {}", user.getId(), output.size());
		LOGGER.trace("Workouts :: {}", output);

		return CollectionUtils.isEmpty(output) ? ResponseEntity.noContent().build() : ResponseEntity.ok(output);
	}

	@GetMapping(path = "/workout/start/{start}/end/{end}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Workout>> retrieveWorkoutsForDateRange(final @RequestAttribute("user") User user,
			final @PathVariable @DateTimeFormat(pattern = Constants.DT_FMT_ISO_LOCAL_DT) Date start,
			final @PathVariable @DateTimeFormat(pattern = Constants.DT_FMT_ISO_LOCAL_DT) Date end) {

		if ((start == null || start.getTime() < 0) && (end == null || end.getTime() < 0)) {
			throw new IllegalArgumentException("start and end cannot be null or empty");
		}

		LOGGER.debug("Retrieving workouts created by User ID :: {} for range {} - {}", user.getId(), start, end);
		List<Workout> output = workoutService.getAllCreatedBetween(user, start.getTime(),
				end.toInstant().plus(1, ChronoUnit.DAYS).toEpochMilli());

		LOGGER.info("Workouts created by userid {} :: {}", user.getId(), output.size());
		LOGGER.trace("Workouts :: {}", output);

		return CollectionUtils.isEmpty(output) ? ResponseEntity.noContent().build() : ResponseEntity.ok(output);
	}

	@PutMapping(path = "/workout/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> updateWorkout(@PathVariable("id") final Integer id,
			@RequestBody(required = true) Workout workout, final @RequestAttribute("user") User user) {

		LOGGER.info("Update Workout Request by User ID {} :: {}", user.getId(), workout);

		Workout output = workoutService.get(id).putBurnrate(workout.getBurnrate());
		output.putStart(workout.getStart()).setEnd(workout.getEnd());
		output.putTitle(workout.getTitle()).setNotes(workout.getNotes());

		if (workout.getCategory() != null && output.getCategory().getId() != workout.getCategory().getId()) {
			output.setCategory(categoryService.get(workout.getCategory().getId()));
			output.setCategoryid(output.getCategory().getId());
		}

		output = workoutService.update(output);
		if (output == null || output.getId() == null || output.getId() <= 0) {
			throw new WorkoutTrackerException("Failed to Update Workout");
		}

		LOGGER.debug("Updated Workout :: {}", output.toString());
		return ResponseEntity.ok(output);
	}

}
