package tech.iiht.tracker.workout.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Component("apiResponse")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@JsonPropertyOrder({ "status", "message" })
public class ApiResponse implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -5522934163309378051L;

	protected List<String> messages = new ArrayList<>();
	protected boolean status = false;

	@JsonIgnore
	public ApiResponse addMessage(String message) {
		this.messages.add(message);
		return this;
	}

	@JsonIgnore
	public ApiResponse addStatus(boolean status) {
		setStatus(status);
		return this;
	}

	public List<String> getMessages() {
		return messages;
	}

	public boolean isStatus() {
		return status;
	}

	public void setMessages(String... messages) {
		this.messages = Stream.of(messages).collect(Collectors.toList());
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append("{");
		builder.append("status : ").append(status);
		builder.append(", messages : ").append(messages);
		return builder.append("}").toString();
	}
}
