package tech.iiht.tracker.workout.controller;

import java.net.URI;
import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import tech.iiht.tracker.workout.bean.Category;
import tech.iiht.tracker.workout.bean.User;
import tech.iiht.tracker.workout.exception.WorkoutTrackerException;
import tech.iiht.tracker.workout.service.CategoryService;

@RestController
public class CategoryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

	private @Resource CategoryService categoryService;

	@PostMapping(path = "/category", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> createCategory(@RequestBody(required = true) Category category,
			final @RequestAttribute("user") User user) {

		category.setUserid(user.getId());
		LOGGER.info("Create Category Request by User ID {} :: {}", user.getId(), category);

		Category output = categoryService.create(category);
		if (output == null || output.getId() == null || output.getId() <= 0) {
			throw new WorkoutTrackerException("Failed to create Category");
		}

		LOGGER.info("Category [{}] created successfully", output.getId());
		LOGGER.debug(output.toString());

		String location = MessageFormat.format("/category/{0}", output.getId());
		location = ServletUriComponentsBuilder.fromCurrentContextPath().path(location).toUriString();
		LOGGER.info("Created Category Location :: {}", location);

		return ResponseEntity.created(URI.create(location))
				.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.GET.name())
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name()).build();
	}

	@DeleteMapping(path = "/category/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> deleteCategory(@PathVariable("id") final Integer id) {

		LOGGER.info("Deleting Category {}", id);
		categoryService.delete(id);

		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Category>> retrieveCategories(final @RequestAttribute("user") User user) {

		LOGGER.debug("Retrieving categories created by User ID :: {}", user.getId());
		List<Category> output = categoryService.getAllCreatedBy(user);

		LOGGER.info("Categories created by userid {} :: {}", user.getId(), output.size());
		LOGGER.trace("Categories :: {}", output);

		return CollectionUtils.isEmpty(output) ? ResponseEntity.noContent().build() : ResponseEntity.ok(output);
	}

	@GetMapping(path = "/category/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Category> retrieveCategory(@PathVariable("id") final Integer id) {

		LOGGER.info("Retrieving Category by id :: {}", id);
		Category output = categoryService.get(id);
		LOGGER.debug("Output :: {}", String.valueOf(output));

		return ResponseEntity.ok(output);
	}

	@PutMapping(path = "/category/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> updateCategory(@PathVariable("id") final Integer id,
			@RequestBody(required = true) Category category, final @RequestAttribute("user") User user) {

		LOGGER.info("Update Category Request by User ID {} :: {}", user.getId(), category);

		Category output = categoryService.get(id).putCategory(category.getCategory());
		output = categoryService.update(output);
		if (output == null || output.getId() == null || output.getId() <= 0) {
			throw new WorkoutTrackerException("Failed to update Category");
		}

		LOGGER.debug("Updated Category :: {}", output.toString());
		return ResponseEntity.ok(output);
	}

}
