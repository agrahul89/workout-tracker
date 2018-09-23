package tech.iiht.tracker.workout.bean;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(schema = "iiht", name = "category")
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties({ "userid", "user", "workouts", "created", "updated" })
@JsonPropertyOrder({ "id", "category", "userid", "user", "workouts", "created", "updated" })
public class Category extends BaseEntity {

	@JsonIgnore
	private static final long serialVersionUID = 936378602471695215L;

	@Column
	@Size(max = 50)
	private String category;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	private User user;

	@Column
	@NotNull
	private Integer userid;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category", orphanRemoval = true, cascade = { CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE })
	private Set<Workout> workouts;

	public String getCategory() {
		return category;
	}

	public User getUser() {
		return user;
	}

	public Integer getUserid() {
		return userid;
	}

	public Set<Workout> getWorkouts() {
		return workouts;
	}

	@JsonIgnore
	public Category putCategory(String category) {
		setCategory(category);
		return this;
	}

	@JsonIgnore
	@Override
	public Category putId(Integer id) {
		setId(id);
		return this;
	}

	@JsonIgnore
	public Category putUserid(Integer userid) {
		setUserid(userid);
		return this;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public void setWorkouts(Set<Workout> workouts) {
		this.workouts = workouts;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append("{");
		builder.append("id : ").append(getId());
		builder.append(", userid : ").append(userid);
		builder.append(", category : ").append(category);
		builder.append(", created : ").append(created);
		builder.append(", updated : ").append(updated);
		return builder.append("}").toString();
	}

}
