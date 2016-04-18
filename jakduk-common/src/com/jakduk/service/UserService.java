package com.jakduk.service;

import java.util.*;

import com.jakduk.authentication.social.SocialUserDetail;
import com.jakduk.exception.DuplicateDataException;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.web.*;

import com.jakduk.repository.user.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.authentication.jakduk.JakdukPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.User;
import com.jakduk.model.embedded.CommonWriter;
import com.jakduk.model.simple.SocialUserOnAuthentication;
import com.jakduk.model.simple.UserOnPasswordUpdate;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.repository.FootballClubRepository;
import com.jakduk.repository.user.UserRepository;
import org.springframework.web.context.request.WebRequest;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private StandardPasswordEncoder encoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private FootballClubRepository footballClubRepository;

	@Autowired
	private ProviderSignInUtils providerSignInUtils;

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public UserProfile getUserProfileById(String id) {
		return userProfileRepository.findOne(id);
	}

	public void create(User user) {
		StandardPasswordEncoder encoder = new StandardPasswordEncoder();
		
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
	}

	public CommonWriter testFindId(String userid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(userid));
		
		return mongoTemplate.findOne(query, CommonWriter.class);
	}

	public Model getUserWrite(Model model, String language) {

		List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

		model.addAttribute("userWrite", new UserWrite());
		model.addAttribute("footballClubs", footballClubs);
		
		return model;
	}
	
	public void saveUserOnSignUp(UserWrite userWrite) {

		User user = new User();
		user.setEmail(userWrite.getEmail().trim());
		user.setUsername(userWrite.getUsername().trim());
		user.setPassword(userWrite.getPassword().trim());
		user.setProviderId(CommonConst.ACCOUNT_TYPE.JAKDUK);
		
		String footballClub = userWrite.getFootballClub();
		String about = userWrite.getAbout();

		if (Objects.nonNull(footballClub) && !footballClub.isEmpty()) {
			FootballClub supportFC = footballClubRepository.findOne(userWrite.getFootballClub());
			
			user.setSupportFC(supportFC);
		}

		if (Objects.nonNull(about) && !about.isEmpty()) {
			user.setAbout(about.trim());
		}

		ArrayList<Integer> roles = new ArrayList<Integer>();
		roles.add(CommonRole.ROLE_NUMBER_USER_01);
		
		user.setRoles(roles);
		
		this.create(user);

		if (log.isDebugEnabled()) {
			log.debug("user=" + user);
		}
	}
	
	public void oauthUserWrite(SocialUserOnAuthentication oauthUserOnLogin) {
		User user = new User();
		
		user.setUsername(oauthUserOnLogin.getUsername());
		//user.setSocialInfo(oauthUserOnLogin.getSocialInfo());
		user.setRoles(oauthUserOnLogin.getRoles());
		
		userRepository.save(user);
	}
	
	public Boolean existEmail(Locale locale, String email) {
		Boolean result = false;

		if (commonService.isAnonymousUser()) {
			User user = userRepository.findOneByEmail(email);

			if (Objects.nonNull(user))
				throw new DuplicateDataException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.already.email"));
		} else {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.already.you.are.user"));
		}
		
		return result;
	}
	
	public Boolean existUsernameOnWrite(Locale locale, String username) {
		Boolean result = false;

		if (commonService.isAnonymousUser()) {
			User user = userRepository.findOneByUsername(username);

			if (Objects.nonNull(user))
				throw new DuplicateDataException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.already.username"));
		} else {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.already.you.are.user"));
		}

		return result;
	}

	/**
	 * 회원 프로필 업데이트 시 Email 중복 체크.
	 * @param locale
	 * @param email
     * @return
     */
	public Boolean existEmailOnUpdate(Locale locale, String email) {

		CommonPrincipal commonPrincipal = this.getCommonPrincipal();

		if (Objects.isNull(commonPrincipal.getId()))
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

		UserProfile userProfile = userRepository.userFindByNEIdAndEmail(commonPrincipal.getId(), email);

		if (Objects.nonNull(userProfile))
			throw new DuplicateDataException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.replicated.data"));

		return false;
	}

	/**
	 * 회원 프로필 업데이트 시 별명 중복 체크.
	 * @param locale
	 * @param username
     * @return
     */
	public Boolean existUsernameOnUpdate(Locale locale, String username) {

		CommonPrincipal commonPrincipal = this.getCommonPrincipal();

		if (Objects.isNull(commonPrincipal.getId()))
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

		UserProfile userProfile = userRepository.userFindByNEIdAndUsername(commonPrincipal.getId(), username);

		if (Objects.nonNull(userProfile))
			throw new DuplicateDataException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.replicated.data"));

		return false;
	}

	/**
	 * SNS 계정의 회원 정보 업데이트 시 DB에 쿼리하여 중복 체크한다.
	 * @param userProfileForm
	 * @param result
     */
	public void checkSocialProfileUpdate(UserProfileForm userProfileForm, BindingResult result) {

		SocialUserDetail principal = (SocialUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String id = principal.getId();
		String email = userProfileForm.getEmail();
		String username = userProfileForm.getUsername();

		UserProfile existEmail = userRepository.userFindByNEIdAndEmail(id, email);
		if (Objects.nonNull(existEmail))
			result.rejectValue("email", "user.msg.already.email");

		UserProfile existUsername = userRepository.userFindByNEIdAndUsername(id, username);
		if (Objects.nonNull(existUsername))
			result.rejectValue("username", "user.msg.already.username");
	}

	public User writeSocialUser(UserProfileForm userForm, CommonConst.ACCOUNT_TYPE providerId, String providerUserId) {

		User user = new User();

		user.setEmail(userForm.getEmail().trim());
		user.setUsername(userForm.getUsername().trim());
		user.setProviderId(providerId);
		user.setProviderUserId(providerUserId);


		ArrayList<Integer> roles = new ArrayList<Integer>();
		roles.add(CommonRole.ROLE_NUMBER_USER_01);

		user.setRoles(roles);

		String footballClub = userForm.getFootballClub();
		String about = userForm.getAbout();

		if (Objects.nonNull(footballClub) && !footballClub.isEmpty()) {
			FootballClub supportFC = footballClubRepository.findOne(footballClub);

			user.setSupportFC(supportFC);
		}

		if (Objects.nonNull(about) && !about.isEmpty()) {
			user.setAbout(userForm.getAbout().trim());
		}

		if (log.isDebugEnabled()) {
			log.debug("OAuth user=" + user);
		}

		userRepository.save(user);

		return user;
	}

	public Model getUserProfileUpdate(Model model, String language) {

		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {

			List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);
			
			JakdukPrincipal authUser = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserProfile userProfile = userProfileRepository.findOne(authUser.getId());
			
			FootballClub footballClub = userProfile.getSupportFC();
			
			UserProfileForm userProfileForm = new UserProfileForm();
			userProfileForm.setEmail(userProfile.getEmail());
			userProfileForm.setUsername(userProfile.getUsername());
			userProfileForm.setAbout(userProfile.getAbout());

			if (footballClub != null) {
				userProfileForm.setFootballClub(footballClub.getId());
			}
			
			model.addAttribute("userProfileWrite", userProfileForm);
			model.addAttribute("footballClubs", footballClubs);
			
		} else {
		}
		
		return model;
	}
	
	public void checkProfileUpdate(UserProfileForm userProfileForm, BindingResult result) {
		
		JakdukPrincipal jakdukPrincipal = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String id = jakdukPrincipal.getId();
		
		String username = userProfileForm.getUsername();
		
		if (id != null && username != null) {
			UserProfile userProfle = userRepository.userFindByNEIdAndUsername(id, username);
			if (userProfle != null) {
				result.rejectValue("username", "user.msg.already.username");
			}
		}		
	}
	
	public void userProfileUpdate(UserProfileForm userProfileForm) {
		
		JakdukPrincipal principal = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
		String id = principal.getId();
		
		User user = userRepository.findById(id);
		user.setUsername(userProfileForm.getUsername());
		user.setAbout(userProfileForm.getAbout());
		
		String footballClub = userProfileForm.getFootballClub();
		
		if (footballClub != null && !footballClub.isEmpty()) {
			FootballClub supportFC = footballClubRepository.findOne(userProfileForm.getFootballClub());
			
			user.setSupportFC(supportFC);
		}
		
		if (log.isInfoEnabled()) {
			log.info("jakduk user update. id=" + user.getId() + ", username=" + user.getUsername());
		}
		
		userRepository.save(user);
		
		principal.setUsername(userProfileForm.getUsername());
		
		commonService.doJakdukAutoLogin(principal, credentials);
		
	}
	
	public Model getUserPasswordUpdate(Model model) {

		model.addAttribute("userPasswordUpdate", new UserPasswordUpdate());
		
		return model;
	}
	
	public void checkUserPasswordUpdate(UserPasswordUpdate userPasswordUpdate, BindingResult result) {
		
		String oldPwd = userPasswordUpdate.getOldPassword();
		String newPwd = userPasswordUpdate.getNewPassword();
		String newPwdCfm = userPasswordUpdate.getNewPasswordConfirm();
		
		JakdukPrincipal jakdukPrincipal = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String id = jakdukPrincipal.getId();
		
		UserOnPasswordUpdate user = userRepository.userOnPasswordUpdateFindById(id);
		String pwd = user.getPassword();
		
		if (!encoder.matches(oldPwd, pwd)) {
			result.rejectValue("oldPassword", "user.msg.old.password.is.not.vaild");
		}
		
		if (!newPwd.equals(newPwdCfm)) {
			result.rejectValue("passwordConfirm", "user.msg.password.mismatch");
		}
	}
	
	public void userPasswordUpdate(UserPasswordUpdate userPasswordUpdate) {
		
		JakdukPrincipal jakdukPrincipal = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String id = jakdukPrincipal.getId();
		
		User user = userRepository.findById(id);
		
		user.setPassword(userPasswordUpdate.getNewPassword());
		
		this.create(user);
		
		if (log.isInfoEnabled()) {
			log.info("jakduk user password was changed. id=" + user.getId() + ", username=" + user.getUsername());
		}
	}

	public void userPasswordUpdateByEmail(String email, String password) {
		User user = userRepository.findByEmail(email);
		user.setPassword(password);
		this.create(user);

		if (log.isInfoEnabled()) {
			log.info("jakduk user password was changed. id=" + user.getId() + ", username=" + user.getUsername());
		}
	}

	public User editSocialProfile(UserProfileForm userProfileForm) {

		SocialUserDetail principal = (SocialUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User user = userRepository.findById(principal.getId());

		String username = userProfileForm.getUsername();
		String footballClub = userProfileForm.getFootballClub();
		String about = userProfileForm.getAbout();

		if (Objects.nonNull(username) && username.isEmpty() == false) {
			user.setUsername(username.trim());
		}

		if (Objects.nonNull(footballClub) && footballClub.isEmpty() == false) {
			FootballClub supportFC = footballClubRepository.findOne(userProfileForm.getFootballClub());
			user.setSupportFC(supportFC);
		}

		if (Objects.nonNull(about) && about.isEmpty() == false) {
			user.setAbout(about.trim());
		}

		userRepository.save(user);

		return user;
	}

	/**
	 * 로그인 중인 회원의 정보를 가져온다.
	 * @return
     */
	public CommonPrincipal getCommonPrincipal() {
		CommonPrincipal commonPrincipal = new CommonPrincipal();
		
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SocialUserDetail) {
				SocialUserDetail userDetail = (SocialUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				commonPrincipal.setId(userDetail.getId());
				commonPrincipal.setEmail(userDetail.getUserId());
				commonPrincipal.setUsername(userDetail.getUsername());
				commonPrincipal.setProviderId(userDetail.getProviderId());
			} else if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JakdukPrincipal) {
				JakdukPrincipal principal = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				commonPrincipal.setId(principal.getId());
				commonPrincipal.setEmail(principal.getEmail());
				commonPrincipal.setUsername(principal.getUsername());
				commonPrincipal.setProviderId(principal.getProviderId());
			}
		}
		
		return commonPrincipal;
	}

	/**
	 * SNS 인증 회원의 로그인 처리.
	 * @param user
	 * @param request
     */
	public void signUpSocialUser(User user, WebRequest request) {

		SocialUserDetail userDetail = new SocialUserDetail(user.getId(), user.getEmail(), user.getUsername(), user.getProviderId(), user.getProviderUserId(),
				true, true, true, true, getAuthorities(user.getRoles()));

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		providerSignInUtils.doPostSignUp(user.getEmail(), request);
	}

	public Collection<? extends GrantedAuthority> getAuthorities(List<Integer> roles) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(roles));

		return authList;
	}

	public List<String> getRoles(List<Integer> roles) {
		List<String> newRoles = new ArrayList<String>();

		if (roles != null) {
			for (Integer roleNumber : roles) {
				String roleName = CommonRole.getRoleName(roleNumber);
				if (!roleName.isEmpty()) {
					newRoles.add(roleName);
				}
			}
		}

		return newRoles;
	}

	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}

		return authorities;
	}
		
}
