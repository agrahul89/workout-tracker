package tech.iiht.tracker.workout.dataobject;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;

public interface BaseDao {

	default Sort sortAsc(String... properties) {
		return JpaSort.by(Direction.ASC, properties);
	}

	default Sort sortDesc(String... properties) {
		return JpaSort.by(Direction.DESC, properties);
	}
}
