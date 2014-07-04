package com.jakduk.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.jakduk.model.db.BoardWriter;
import com.jakduk.model.db.User;
import com.jakduk.model.web.UserWrite;
import com.jakduk.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void create(User user) {
		StandardPasswordEncoder encoder = new StandardPasswordEncoder();
		
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
	}
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public BoardWriter testFindId(String userid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(userid));
		
		return mongoTemplate.findOne(query, BoardWriter.class);
	}
	
	public void checkUserWrite(UserWrite userWrite, BindingResult result) {
		
		Integer countEmail = userRepository.countByEmail(userWrite.getEmail());
		Integer countUsername = userRepository.countByEmail(userWrite.getUsername());
		String pwd = userWrite.getPassword();
		String pwdCfm = userWrite.getPasswordConfirm();
		
		if (countEmail > 0) {
			result.rejectValue("email", "user.msg.already.email");
		}
		
		if (countUsername > 0) {
			result.rejectValue("username", "user.msg.already.username");
		}
		
		if (!pwd.equals(pwdCfm)) {
			result.rejectValue("passwordConfirm", "user.msg.not.equal.password");
		}
	}
	
	public void userWrite(UserWrite userWrite) {
		User user = new User();
		user.setEmail(userWrite.getEmail());
		user.setUsername(userWrite.getUsername());
		user.setPassword(userWrite.getPassword());
		user.setAbout(userWrite.getAbout());
		
		this.create(user);
	}
	
	
		
}
