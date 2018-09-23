package tech.iiht.tracker.workout.security;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import tech.iiht.tracker.workout.bean.User;
import tech.iiht.tracker.workout.bean.User.ROLE;
import tech.iiht.tracker.workout.constant.Constants;
import tech.iiht.tracker.workout.service.UserService;

@Component
public class AuthProvider implements AuthenticationProvider {
	private static final long expireMillis = Constants.AUTH_TOKEN_EXPIRE_MILLIS;
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthProvider.class);

	private byte[] secret = Constants.APP_SECRET.getBytes();
	private @Resource UserService userService;

	@Override
	public UsernamePasswordAuthenticationToken authenticate(Authentication authentication)
			throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		User userDetails = userService.loadUserByUsername(name);
		if (StringUtils.hasText(password) && password.equals(userDetails.getPassword())) {
			return new UsernamePasswordAuthenticationToken(userDetails.getEmail(), userDetails.getPassword(),
					userDetails.getAuthorities());
		}
		return null;
	}

	public String generateToken(Authentication auth) {
		User user = (User) auth.getPrincipal();

		Date issuedAt = new Date(System.currentTimeMillis());
		Date expireOn = new Date(issuedAt.getTime() + expireMillis);

		return Jwts.builder().setSubject(String.valueOf(user.getId())).setAudience(ROLE.USER.name())
				.setIssuedAt(issuedAt).setExpiration(expireOn).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Integer parseToken(final String authToken) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
		return ROLE.USER.name().equals(claims.get(Claims.AUDIENCE, String.class))
				? Integer.parseInt(claims.getSubject()) : null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	public boolean validateToken(String authToken) {
		boolean valid = false;
		try {
			valid = ROLE.USER.name()
					.equals(Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody().getAudience());
		} catch (SignatureException ex) {
			LOGGER.error("Invalid JWT signature");
			LOGGER.debug(ex.getMessage(), ex);
			// throw new BadCredentialsException(
			// "Tampered Token. Please Login to validate authenticity", ex);
		} catch (MalformedJwtException ex) {
			LOGGER.error("Invalid JWT token");
			LOGGER.debug(ex.getMessage(), ex);
			// throw new BadCredentialsException(
			// "Invalid Token. Please Login to re-generate token", ex);
		} catch (ExpiredJwtException ex) {
			LOGGER.error("Expired JWT token");
			LOGGER.debug(ex.getMessage(), ex);
			// throw new CredentialsExpiredException(
			// "Token Expired. Please Login to issue new token", ex);
		} catch (UnsupportedJwtException ex) {
			LOGGER.error("Unsupported JWT token");
			LOGGER.debug(ex.getMessage(), ex);
			// throw new BadCredentialsException(
			// "Invalid Token. Please Login to issue new token", ex);
		} catch (IllegalArgumentException ex) {
			LOGGER.error("JWT Claims is empty");
			LOGGER.debug(ex.getMessage(), ex);
			// throw new BadCredentialsException(
			// "Invalid Token. Please Login to issue new token", ex);
		}
		return valid;
	}
}
