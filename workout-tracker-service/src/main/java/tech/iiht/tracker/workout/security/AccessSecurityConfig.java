package tech.iiht.tracker.workout.security;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tech.iiht.tracker.workout.service.UserService;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class AccessSecurityConfig extends WebSecurityConfigurerAdapter implements AuthenticationEntryPoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccessSecurityConfig.class);

	private @Resource AuthFilter authFilter;
	private @Resource AuthProvider authProvider;
	private @Resource PasswordEncoder passwordEncoder;
	private @Resource UserService userService;

	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
			throws IOException, ServletException {
		LOGGER.error("Unauthorized :: {}", ex.getMessage());
		response.setHeader(HttpHeaders.ALLOW, HttpMethod.POST.name());
		response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// XXX Use this for usename/password based authentication
		// auth.authenticationProvider(authProvider);
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder).and().eraseCredentials(true);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(this);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().mvcMatchers("/", "/login", "/registration", "/registration/**").permitAll();
		http.authorizeRequests().anyRequest().authenticated();
		http.logout().logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true)
				.logoutSuccessUrl("/login").permitAll();
		http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
