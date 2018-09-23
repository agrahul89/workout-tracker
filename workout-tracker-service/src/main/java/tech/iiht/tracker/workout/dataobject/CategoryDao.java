package tech.iiht.tracker.workout.dataobject;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.iiht.tracker.workout.bean.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

	List<Category> findAllByUserid(final Integer userid);

}
