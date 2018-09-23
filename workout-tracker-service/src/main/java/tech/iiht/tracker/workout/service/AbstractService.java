package tech.iiht.tracker.workout.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.iiht.tracker.workout.bean.BaseEntity;
import tech.iiht.tracker.workout.bean.User;

public abstract class AbstractService<Entity extends BaseEntity, Identifier extends Number> {

	public Entity create(final Entity entity) {
		return save(entity);
	}

	protected abstract JpaRepository<Entity, Identifier> dao();

	public void delete(final Identifier workoutId) {
		dao().deleteById(workoutId);
	}

	public Entity get(final Identifier id) {
		return dao().findById(id).get();
	}

	protected abstract List<Entity> getAllCreatedBy(final User user);

	private Entity save(final Entity entity) {
		return dao().saveAndFlush(entity);
	}

	public Entity update(final Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Input details cannot be empty");
		}
		if (entity.getId() == null || entity.getId() <= 0) {
			throw new IllegalArgumentException("Invalid " + entity.getClass().getSimpleName() + " Identifier");
		}
		return save(entity);
	}

}
