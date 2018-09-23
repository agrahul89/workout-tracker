package tech.iiht.tracker.workout.bean;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(schema = "iiht", name = "workout")
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties({ "categoryid", "userid", "user", "created", "updated", "deleted" })
@JsonPropertyOrder({ "id", "title", "notes", "burnrate", "category", "start", "end", "userid", "user", "created",
		"updated", "deleted" })
public class Workout extends BaseEntity {

	@JsonIgnore
	private static final long serialVersionUID = 4386545488144245548L;

	@Column
	@DecimalMin(value = "0.0", inclusive = true)
	private Double burnrate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "categoryid", insertable = false, updatable = false)
	private Category category;

	@Column
	@NotNull
	private Integer categoryid;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date deleted;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date end;

	@Column
	@JsonProperty("note")
	private String notes;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date start;

	@Column
	@NotEmpty
	private String title;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	private User user;

	@Column
	@NotNull
	private Integer userid;

	public Double getBurnrate() {
		return burnrate;
	}

	public Category getCategory() {
		return category;
	}

	public Integer getCategoryid() {
		return categoryid;
	}

	public Date getDeleted() {
		return deleted;
	}

	public Date getEnd() {
		return end;
	}

	public String getNotes() {
		return notes;
	}

	public Date getStart() {
		return start;
	}

	public String getTitle() {
		return title;
	}

	public User getUser() {
		return user;
	}

	public Integer getUserid() {
		return userid;
	}

	@JsonIgnore
	public Workout putBurnrate(Double burnrate) {
		setBurnrate(burnrate);
		return this;
	}

	@JsonIgnore
	public Workout putCategoryid(Integer categoryid) {
		setCategoryid(categoryid);
		return this;
	}

	public Workout putDeleted(Date deleted) {
		setDeleted(deleted);
		return this;
	}

	@JsonIgnore
	public Workout putEnd(Date end) {
		setEnd(end);
		return this;
	}

	@JsonIgnore
	@Override
	public Workout putId(Integer id) {
		setId(id);
		return this;
	}

	@JsonIgnore
	public Workout putNotes(String notes) {
		setNotes(notes);
		return this;
	}

	@JsonIgnore
	public Workout putStart(Date start) {
		setStart(start);
		return this;
	}

	@JsonIgnore
	public Workout putTitle(String title) {
		setTitle(title);
		return this;
	}

	@JsonIgnore
	public Workout putUserid(Integer userid) {
		setUserid(userid);
		return this;
	}

	public void setBurnrate(Double burnrate) {
		this.burnrate = burnrate;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setCategoryid(Integer categoryid) {
		this.categoryid = categoryid;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append("{");
		builder.append("id : ").append(super.getId());
		builder.append(", title : ").append(title);
		builder.append(", notes : ").append(notes);
		builder.append(", burnrate : ").append(burnrate);
		if (start != null) {
			builder.append(", start : ").append(start);
		}
		if (end != null) {
			builder.append(", end : ").append(end);
		}
		builder.append(", userid : ").append(userid);
		if (user != null) {
			builder.append(", user : ").append(user);
		}
		builder.append(", categoryid : ").append(categoryid);
		if (category != null) {
			builder.append(", category : ").append(category);
		}
		builder.append(", created : ").append(created);
		builder.append(", updated : ").append(updated);
		return builder.append("}").toString();
	}

}
