package tech.iiht.tracker.workout.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "status", "messages", "data" })
public class ApiResponseBody<T extends BaseEntity> extends ApiResponse implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -5522934163309378051L;

	private T data;

	@JsonIgnore
	public ApiResponseBody<T> addData(T data) {
		setData(data);
		return this;
	}

	@Override
	public ApiResponseBody<T> addMessage(String message) {
		super.addMessage(message);
		return this;
	}

	@Override
	public ApiResponseBody<T> addStatus(boolean status) {
		super.addStatus(status);
		return this;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append("{");
		builder.append("status :: ").append(status);
		builder.append(", messages :: ").append(messages);
		builder.append(", data :: ").append(data);
		return builder.append("}").toString();
	}
}
