package tech.iiht.tracker.workout;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import tech.iiht.tracker.workout.bean.Category;
import tech.iiht.tracker.workout.bean.LoginCredential;
import tech.iiht.tracker.workout.bean.User;
import tech.iiht.tracker.workout.bean.Workout;
import tech.iiht.tracker.workout.security.AuthFilter;

@Configuration
@EnableAutoConfiguration
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = { "tech.iiht.tracker.workout.*" })
public class WorkoutTrackerApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkoutTrackerApplication.class);

	public static void main(String[] args) throws IOException {
		System.setProperty("app.secret", "tr@cke1");
		SpringApplication.run(WorkoutTrackerApplication.class, args);
	}

	@Bean("authDetailsStore")
	public WebAuthenticationDetailsSource authDetailsStore() {
		return new WebAuthenticationDetailsSource();
	}

	@Bean("category")
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Category category() {
		return new Category();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public CorsConfigurationSource corsConfigurationSource() {

		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.applyPermitDefaultValues();
		configuration.setAllowCredentials(true);

		configuration.addAllowedMethod(HttpMethod.PUT);
		configuration.addAllowedMethod(HttpMethod.DELETE);
		configuration.addAllowedMethod(HttpMethod.OPTIONS);

		List<String> headers = new ArrayList<>();
		headers.add(HttpHeaders.ACCEPT);
		headers.add(HttpHeaders.ACCEPT);
		headers.add(HttpHeaders.ACCEPT_CHARSET);
		headers.add(HttpHeaders.ACCEPT_ENCODING);
		headers.add(HttpHeaders.ACCEPT_LANGUAGE);
		headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS);
		headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS);
		headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS);
		headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS);
		headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE);
		headers.add(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
		headers.add(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD);
		headers.add(HttpHeaders.ALLOW);
		headers.add(HttpHeaders.AUTHORIZATION);
		headers.add(HttpHeaders.CONTENT_DISPOSITION);
		headers.add(HttpHeaders.CONTENT_ENCODING);
		headers.add(HttpHeaders.CONTENT_LENGTH);
		headers.add(HttpHeaders.CONTENT_LOCATION);
		headers.add(HttpHeaders.CONTENT_TYPE);
		headers.add(HttpHeaders.ETAG);
		headers.add(HttpHeaders.LOCATION);
		headers.add(HttpHeaders.ORIGIN);
		headers.add(HttpHeaders.TRANSFER_ENCODING);
		headers.add(HttpHeaders.USER_AGENT);

		configuration.setAllowedHeaders(headers);
		configuration.setExposedHeaders(headers);

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthFilter jwtAuthFilter() {
		return new AuthFilter();
	}

	@Bean("login")
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public LoginCredential loginCredential() {
		return new LoginCredential();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		try {
			SecureRandom random = SecureRandom.getInstanceStrong();
			encoder = new BCryptPasswordEncoder(Character.MAX_RADIX, random);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex.getCause());
		}
		return encoder;
	}

	@Bean("user")
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public User user() {
		return new User();
	}

	@Bean("workout")
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Workout workout() {
		return new Workout();
	}

}
