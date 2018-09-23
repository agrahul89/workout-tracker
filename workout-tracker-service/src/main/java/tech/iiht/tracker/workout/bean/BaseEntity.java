package tech.iiht.tracker.workout.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -5704160323617509823L;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	protected Date created;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "idGenrator")
	@GenericGenerator(name = "idGenrator", strategy = "native")
	protected Integer id;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	protected Date updated;

	protected abstract BaseEntity putId(Integer id);

	public Date getCreated() {
		return created;
	}

	public Integer getId() {
		return id;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}
