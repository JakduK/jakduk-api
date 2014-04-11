package org.jakduk.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.jakduk.model.User;
import org.jakduk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void create(User user) {
		userRepository.save(user);
	}
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
}
