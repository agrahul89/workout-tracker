package tech.iiht.tracker.workout.service;

import java.sql.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import tech.iiht.tracker.workout.bean.User;
import tech.iiht.tracker.workout.bean.Workout;
import tech.iiht.tracker.workout.dataobject.WorkoutDao;

@Service
public class WorkoutService extends AbstractService<Workout, Integer> {

	private @Resource WorkoutDao workoutDao;

	@Override
	protected JpaRepository<Workout, Integer> dao() {
		return workoutDao;
	}

	public List<Workout> getAllCreatedBetween(final User user, long start, long end) {
		return workoutDao.findAllByUseridBetween(user.getId(), new Date(start), new Date(end),
				workoutDao.sortAsc("start"));
	}

	@Override
	public List<Workout> getAllCreatedBy(final User user) {
		return workoutDao.findAllByUserid(user.getId());
	}

}
