package tech.iiht.tracker.workout.bean;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "username", "password", "authToken" })
public class LoginCredential {

	private String authToken;

	@Column
	@NotEmpty
	private String password;

	@Column
	@Email
	@NotEmpty
	private String username;

	public String getAuthToken() {
		return authToken;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append("{");
		builder.append("username : ").append(username);
		builder.append(", password : ").append("********");
		builder.append(", token : ").append("********");
		return builder.append("}").toString();
	}

}
