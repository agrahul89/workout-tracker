package tech.iiht.tracker.workout.dataobject;

import java.util.Optional;

import javax.validation.constraints.Email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.iiht.tracker.workout.bean.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

	boolean existsByEmail(final @Email String email);

	Optional<User> findByEmail(final @Email String email);

}
