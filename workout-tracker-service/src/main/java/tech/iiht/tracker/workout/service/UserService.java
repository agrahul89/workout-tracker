package tech.iiht.tracker.workout.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tech.iiht.tracker.workout.bean.User;
import tech.iiht.tracker.workout.dataobject.UserDao;

@Service
public class UserService implements UserDetailsService {

	private @Resource UserDao userdao;

	@Transactional
	public boolean exists(Integer userid) {
		return userdao.existsById(userid);
	}

	@Transactional
	public boolean exists(User user) {
		return userdao.existsByEmail(user.getEmail());
	}

	@Transactional
	public User getUserById(Integer userid) throws UsernameNotFoundException {
		UsernameNotFoundException ex = new UsernameNotFoundException("Unregistered User ID :: " + userid);
		return userdao.findById(userid).orElseThrow(() -> ex);
	}

	@Override
	@Transactional
	public User loadUserByUsername(String email) throws UsernameNotFoundException {
		UsernameNotFoundException ex = new UsernameNotFoundException("Unregistered Email ID :: " + email);
		return userdao.findByEmail(email).orElseThrow(() -> ex);
	}

	@Transactional
	public User register(User user) {
		return userdao.save(user);
	}
}
