package SQA.service;


import SQA.model.User;
import SQA.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	public User findUser() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getCredentials();
		return u;
	}
	
	public void signInAuthentication(String firebaseToken) {
		if(StringUtils.isBlank(firebaseToken))
			throw new IllegalArgumentException("FirebaseTokenBlank");

		//validate token and return FirebaseTokenHolder instance

		User loadedUser = null;



	}

	public void signout() {
		SecurityContextHolder.clearContext();
	}

	public User getUserFromDB(String email) {
		User userLoaded = userRepository.findUserByEmail(email);

		if(userLoaded == null) {
			userLoaded = new User();
			userLoaded.setEmail(email);

			userRepository.save(userLoaded);
		}
		return userLoaded;
	}

}