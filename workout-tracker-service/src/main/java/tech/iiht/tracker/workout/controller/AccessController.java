package tech.iiht.tracker.workout.controller;

import java.net.URI;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tech.iiht.tracker.workout.bean.ApiResponseBody;
import tech.iiht.tracker.workout.bean.LoginCredential;
import tech.iiht.tracker.workout.bean.User;
import tech.iiht.tracker.workout.exception.WorkoutTrackerException;
import tech.iiht.tracker.workout.security.AuthProvider;
import tech.iiht.tracker.workout.service.UserService;

@RestController
public class AccessController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccessController.class);

	private @Resource(name = BeanIds.AUTHENTICATION_MANAGER) AuthenticationManager authenticationManager;
	private @Resource PasswordEncoder passwordEncoder;
	private @Resource UserService userService;

	@GetMapping(path = "/registration/email/{email}")
	public ResponseEntity<?> checkEmailAvailability(final @PathVariable("email") String email, @Autowired User user) {

		LOGGER.info("Checking availability for email :: " + email);
		boolean exists = userService.exists(user.addEmail(email));
		LOGGER.info("Email [" + email + "] " + (exists ? "already " : "does not ") + "exists");
		return exists ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

	@GetMapping(path = "/")
	public ResponseEntity<?> home() {
		String location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/login").toUriString();
		return ResponseEntity.noContent().location(URI.create(location))
				.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.POST.name()).build();
	}

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<LoginCredential> login(@Autowired AuthProvider authProvider,
			@RequestBody(required = true) LoginCredential credential, @Autowired LoginCredential authorized) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		authorized.setUsername(credential.getUsername());
		authorized.setPassword(passwordEncoder.encode(credential.getPassword()));
		authorized.setAuthToken(authProvider.generateToken(authentication));

		return ResponseEntity.ok(authorized);
	}

	@PostMapping(path = "/registration", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ApiResponseBody<User>> register(@RequestBody(required = true) User user,
			@Autowired ApiResponseBody<User> output) {

		if (!StringUtils.hasText(user.getFirstname()) || !StringUtils.hasText(user.getLastname())
				|| !StringUtils.hasText(user.getEmail()) || !StringUtils.hasText(user.getPassword())) {
			throw new IllegalArgumentException(
					"One or more mandatory fields(First Name, Last Name, Email, Password) are missing");
		}

		// TODO Fix this Regex
		// if (!Constants.REGEX_PASSWORD.matches(user.getPassword())) {
		// throw new IllegalArgumentException(Constants.PASSWORD_FORMAT_ERROR);
		// }

		if (userService.exists(user)) {
			output.addMessage("User already registered with provided email").addData(user.clearPassword());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(output);
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		LOGGER.info("Registering User :: " + user);
		User registered = userService.register(user);

		if (registered.getId() == null || registered.getId() <= 0) {

			LOGGER.error("Registration Failed for user :: " + user);
			throw new WorkoutTrackerException("User Registration Failed");

		} else {

			LOGGER.info("User Registered :: " + registered);
			output.addMessage("User registered successfully").addData(registered.clearPassword());

			String location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/login").toUriString();
			return ResponseEntity.created(URI.create(location))
					.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.POST.name())
					.body(output.addStatus(true));
		}
	}

}
