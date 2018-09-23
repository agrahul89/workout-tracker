package tech.iiht.tracker.workout.bean;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import tech.iiht.tracker.workout.constant.Constants;

@Entity
@Table(schema = "iiht", name = "user")
@JsonIgnoreProperties({ "midname", "categories", "workouts", "created", "updated" })
@JsonPropertyOrder({ "id", "firstname", "lastname", "midname", "email", "password", "categories", "workouts", "created",
		"updated" })
public class User extends BaseEntity implements UserDetails {

	public enum ROLE {
		USER;
	}

	@JsonIgnore
	private static final long serialVersionUID = -6088061480545479360L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = { CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE })
	private Set<Category> categories;

	@Column
	@Email
	@NotEmpty
	private String email;

	@Column
	@Pattern(regexp = Constants.REGEX_ALPHA)
	@Size(max = 20)
	private String firstname;

	@Column
	@Pattern(regexp = Constants.REGEX_ALPHA)
	@Size(max = 20)
	private String lastname;

	@Column
	@Pattern(regexp = Constants.REGEX_ALPHA)
	@Size(max = 20)
	private String midname;

	@Column
	@NotEmpty
	private String password;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
	private Set<Workout> workouts;

	@JsonIgnore
	public User addCategories(Set<Category> categories) {
		setCategories(categories);
		return this;
	}

	@JsonIgnore
	public void addCategory(Category category) {
		getCategories().add(category);
		category.setUser(this);
	}

	@JsonIgnore
	public User addCreated(Timestamp created) {
		setCreated(created);
		return this;
	}

	@JsonIgnore
	public User addEmail(String email) {
		setEmail(email);
		return this;
	}

	@JsonIgnore
	public User addFirstname(String firstname) {
		setFirstname(firstname);
		return this;
	}

	@JsonIgnore
	@Override
	public User putId(Integer id) {
		setId(id);
		return this;
	}

	@JsonIgnore
	public User addLastname(String lastname) {
		setLastname(lastname);
		return this;
	}

	@JsonIgnore
	public User addMidname(String midname) {
		setMidname(midname);
		return this;
	}

	@JsonIgnore
	public User addPassword(String password) {
		setPassword(password);
		return this;
	}

	@JsonIgnore
	public void addWorkout(Workout workout) {
		getWorkouts().add(workout);
		workout.setUser(this);
	}

	@JsonIgnore
	public User addWorkouts(Set<Workout> workouts) {
		setWorkouts(workouts);
		return this;
	}

	@JsonIgnore
	public final User clearPassword() {
		setPassword(null);
		return this;
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this == null || !StringUtils.hasText(Integer.toString(super.getId())) ? Collections.emptyList()
				: Arrays.asList(new SimpleGrantedAuthority(ROLE.USER.name()));
	}

	public Set<Category> getCategories() {
		if (CollectionUtils.isEmpty(categories)) {
			categories = new TreeSet<>();
		}
		return categories;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getMidname() {
		return midname;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return this == null ? null : this.getEmail();
	}

	public Set<Workout> getWorkouts() {
		return workouts;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this != null;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this != null;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this != null;
	}

	@Override
	public boolean isEnabled() {
		return this != null;
	}

	public void removeCategory(Category category) {
		getCategories().remove(category);
		category.setUser(null);
	}

	public void removeWorkout(Workout workout) {
		getWorkouts().remove(workout);
		workout.setUser(null);
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setMidname(String midname) {
		this.midname = midname;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setWorkouts(Set<Workout> workouts) {
		this.workouts = workouts;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append("{");
		builder.append("id : ").append(super.getId());
		builder.append(", firstname : ").append(firstname);
		builder.append(", lastname : ").append(lastname);
		builder.append(", midname : ").append(midname);
		builder.append(", email : ").append(email);
		builder.append(", password : ").append("********");
		builder.append(", created : ").append(created);
		builder.append(", updated : ").append(updated);
		return builder.append("}").toString();
	}

}
