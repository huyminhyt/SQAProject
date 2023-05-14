package SQA.service;

import SQA.model.User;

public interface UserService {
	public User findUser();
	public void signInAuthentication(String firebaseToken);
	public void signout();
	public User getUserFromDB(String email);
}
