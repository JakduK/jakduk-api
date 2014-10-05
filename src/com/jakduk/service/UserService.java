package com.jakduk.service;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.jakduk.authentication.common.CommonUserDetails;
import com.jakduk.authentication.facebook.FacebookDetails;
import com.jakduk.common.CommonConst;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.User;
import com.jakduk.model.simple.BoardWriter;
import com.jakduk.model.simple.OAuthUserOnLogin;
import com.jakduk.model.web.OAuthUserWrite;
import com.jakduk.model.web.UserWrite;
import com.jakduk.repository.FootballClubRepository;
import com.jakduk.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FootballClubRepository footballClubRepository;
	
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
		
		String pwd = userWrite.getPassword();
		String pwdCfm = userWrite.getPasswordConfirm();
		
		if (this.existEmail(userWrite.getEmail())) {
			result.rejectValue("email", "user.msg.already.email");
		}
		
		if (this.existUsername(userWrite.getUsername())) {
			result.rejectValue("username", "user.msg.already.username");
		}
		
		if (!pwd.equals(pwdCfm)) {
			result.rejectValue("passwordConfirm", "user.msg.password.mismatch");
		}
	}
	
	public Model getUserWrite(Model model, String language) {
		
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("names"));
		Pageable pageable = new PageRequest(0, 100, sort);
		
		List<FootballClub> footballClubs = footballClubRepository.findByNamesLanguage(language, pageable);
		
		model.addAttribute("userWrite", new UserWrite());
		model.addAttribute("footballClubs", footballClubs);
		
		return model;
	}
	
	public void userWrite(UserWrite userWrite) {
		User user = new User();
		user.setEmail(userWrite.getEmail());
		user.setUsername(userWrite.getUsername());
		user.setPassword(userWrite.getPassword());
		
		String footballClub = userWrite.getFootballClub();
		
		if (footballClub != null && footballClub != "-1") {
			FootballClub supportFC = footballClubRepository.findById(userWrite.getFootballClub());
			
			user.setSupportFC(supportFC);
		}
		
		user.setAbout(userWrite.getAbout());
		
		this.create(user);
	}
	
	public void oauthUserWrite(OAuthUserOnLogin oauthUserOnLogin) {
		User user = new User();
		user.setOauthUser(oauthUserOnLogin.getOauthUser());
		userRepository.save(user);
	}
	
	public Boolean existEmail(String email) {
		Boolean result = false;
		
		if (userRepository.findOneByEmail(email) != null) result = true;
		
		return result;
	}
	
	public Boolean existUsername(String username) {
		Boolean result = false;
		
		if (userRepository.findOneByUsername(username) != null) result = true;
		
		return result;
	}
	
	public Model getOAuthWriteDetails(Model model, String language) {
		
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("names"));
		Pageable pageable = new PageRequest(0, 100, sort);
		
		List<FootballClub> footballClubs = footballClubRepository.findByNamesLanguage(language, pageable);
		
		CommonUserDetails userDetails = (CommonUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
		
		OAuthUserWrite oauthUserWrite = new OAuthUserWrite();
		oauthUserWrite.setAbout(userDetails.getBio());
		
		model.addAttribute("userWrite", oauthUserWrite);
		model.addAttribute("footballClubs", footballClubs);
		
		return model;
	}
	
	public void oAuthWriteDetails(OAuthUserWrite userWrite) {
		
		FacebookDetails userDetails = (FacebookDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		User user = userRepository.userFindByOauthUser(CommonConst.OAUTH_TYPE_FACEBOOK, userDetails.getId());
		
		String footballClub = userWrite.getFootballClub();
		
		if (footballClub != null && footballClub != "-1") {
			FootballClub supportFC = footballClubRepository.findById(userWrite.getFootballClub());
			
			user.setSupportFC(supportFC);
		}
		
		user.setAbout(userWrite.getAbout());
		
		logger.debug("user=" + user);
		
		userRepository.save(user);
	}
		
}
