package tech.iiht.tracker.workout.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import tech.iiht.tracker.workout.bean.Category;
import tech.iiht.tracker.workout.bean.User;
import tech.iiht.tracker.workout.dataobject.CategoryDao;

@Service
public class CategoryService extends AbstractService<Category, Integer> {

	private @Resource CategoryDao categoryDao;

	public List<Category> getAllCreatedBy(final User user) {
		return categoryDao.findAllByUserid(user.getId());
	}

	@Override
	protected JpaRepository<Category, Integer> dao() {
		return categoryDao;
	}

}
