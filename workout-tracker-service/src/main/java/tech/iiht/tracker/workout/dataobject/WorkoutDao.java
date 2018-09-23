package tech.iiht.tracker.workout.dataobject;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tech.iiht.tracker.workout.bean.Workout;

@Repository
public interface WorkoutDao extends JpaRepository<Workout, Integer>, BaseDao {

	@Query("select workout from #{#entityName} workout where workout.userid = ?1 and workout.deleted is null")
	List<Workout> findAllByUserid(final Integer userid);

	@Query("select workout from #{#entityName} workout where workout.userid = ?1"
			+ " and workout.start > ?2 and workout.end < ?3")
	List<Workout> findAllByUseridBetween(final Integer userid, final Date start, final Date end, Sort sortingCriteria);

}
