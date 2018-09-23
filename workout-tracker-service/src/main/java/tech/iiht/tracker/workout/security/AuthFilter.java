package tech.iiht.tracker.workout.security;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import tech.iiht.tracker.workout.bean.User;
import tech.iiht.tracker.workout.service.UserService;

public class AuthFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

	private @Resource WebAuthenticationDetailsSource authDetailsStore;
	private @Resource AuthProvider authProvider;
	private @Resource UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authToken = extractToken(request);

		if (StringUtils.hasText(authToken) && authProvider.validateToken(authToken)) {
			Integer userId = authProvider.parseToken(authToken);
			LOGGER.debug("User ID extracted from token :: " + userId);

			User userDetails = userService.getUserById(userId);
			LOGGER.debug("User Details extracted :: " + userDetails.getUsername());
			request.setAttribute("user", userDetails);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					userDetails.getPassword(), userDetails.getAuthorities());
			authentication.setDetails(authDetailsStore.buildDetails(request));

			// XXX Use this for usename/password based authentication
			// authentication =
			// authProvider.authenticate(SecurityContextHolder.getContext().getAuthentication());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.contains("Bearer")) {
			bearerToken = StringUtils.delete(bearerToken, "Bearer").trim();
		}
		return bearerToken;
	}
}
