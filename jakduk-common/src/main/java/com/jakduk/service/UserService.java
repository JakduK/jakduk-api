package com.jakduk.service;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.authentication.common.JakdukPrincipal;
import com.jakduk.authentication.common.SocialUserDetail;
import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.exception.ServiceError;
import com.jakduk.exception.ServiceException;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.db.User;
import com.jakduk.model.simple.UserOnPasswordUpdate;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.repository.user.UserProfileRepository;
import com.jakduk.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	public User findById(String id) {
		return userRepository.findOne(id);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public UserProfile findUserProfileById(String id) {
		return userProfileRepository.findOne(id);
	}

	// email과 일치하는 회원 찾기.
	public UserProfile findOneByEmail(String email) {
		return userProfileRepository.findOneByEmail(email);
	}

	// username과 일치하는 회원 찾기.
	public UserProfile findOneByUsername(String username) {
		return userProfileRepository.findOneByUsername(username);
	}

	// 해당 ID를 제외하고 username과 일치하는 회원 찾기.
	public UserProfile findByNEIdAndUsername(String id, String username) {
		return userProfileRepository.findByNEIdAndUsername(id, username);
	};

	// 해당 ID를 제외하고 email과 일치하는 회원 찾기.
	public UserProfile findByNEIdAndEmail(String id, String email) {
		return userProfileRepository.findByNEIdAndEmail(id, email);
	}

	// SNS 계정으로 가입한 회원 찾기.
	public UserProfile findUserProfileByProviderIdAndProviderUserId(CommonConst.ACCOUNT_TYPE providerId, String providerUserId) {
		return userProfileRepository.findOneByProviderIdAndProviderUserId(providerId, providerUserId);
	}

	// SNS 계정으로 가입한 회원 찾기(로그인).
	public User findOneByProviderIdAndProviderUserId(CommonConst.ACCOUNT_TYPE providerId, String providerUserId) {
		return userRepository.findOneByProviderIdAndProviderUserId(providerId, providerUserId);
	}

	/**
	 * 비밀번호 변경을 위한 이메일 기반 회원 가져오기
	 *
	 * @param id userID
	 * @return UserOnPasswordUpdate
     */
	public UserOnPasswordUpdate findUserOnPasswordUpdateById(String id){

		return userRepository.findUserOnPasswordUpdateById(id);
	}

	// 회원 정보 저장.
	public void save(User user) {
		userRepository.save(user);
	}

	public Boolean existEmail(String email) {
		Boolean result;

		if (commonService.isAnonymousUser()) {
			User user = userRepository.findOneByEmail(email);
			result = Objects.nonNull(user);
		} else {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage("messages.common", "common.exception.already.you.are.user"));
		}
		
		return result;
	}
	
	public Boolean existUsernameOnWrite(String username) {

		Boolean result;

		if (commonService.isAnonymousUser()) {
			User user = userRepository.findOneByUsername(username);
			result = Objects.nonNull(user);
		} else {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage("messages.common", "common.exception.already.you.are.user"));
		}

		return result;
	}

	/**
	 * 이메일 기반 회원의 비밀번호 변경.
	 *
	 * @param newPassword 새 비밀번호
     */
	public void updateUserPassword(String newPassword) {
		
		JakdukPrincipal jakdukPrincipal = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String id = jakdukPrincipal.getId();
		
		Optional<User> user = userRepository.findOneById(id);

		if (!user.isPresent())
			throw new ServiceException(ServiceError.NOT_FOUND_USER);

		User updateUser = user.get();
		updateUser.setPassword(encoder.encode(newPassword));
		
		this.save(updateUser);
		
		if (log.isInfoEnabled()) {
			log.info("jakduk user password changed. email=" + updateUser.getEmail() + ", username=" + updateUser.getUsername());
		}
	}

	public void userPasswordUpdateByEmail(String email, String password) {
		User user = userRepository.findByEmail(email);
		user.setPassword(encoder.encode(password));
		this.save(user);

		if (log.isInfoEnabled()) {
			log.info("jakduk user password changed. id=" + user.getId() + ", username=" + user.getUsername());
		}
	}

	/**
	 * 로그인 중인 회원의 정보를 가져온다.
	 * @return 로그인 회원 객체.
     */
	public CommonPrincipal getCommonPrincipal() {
		CommonPrincipal commonPrincipal = null;
		
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SocialUserDetail) {
				SocialUserDetail userDetail = (SocialUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				commonPrincipal = CommonPrincipal.builder()
						.id(userDetail.getId())
						.email(userDetail.getUserId())
						.username(userDetail.getUsername())
						.providerId(userDetail.getProviderId())
						.build();
			} else if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JakdukPrincipal) {
				JakdukPrincipal principal = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				commonPrincipal = CommonPrincipal.builder()
						.id(principal.getId())
						.email(principal.getUsername())
						.username(principal.getNickname())
						.providerId(principal.getProviderId())
						.build();
			}
		}
		
		return commonPrincipal;
	}

	/**
	 * 이메일 기반 회원의 로그인 처리
	 * @param user User 객체
     */
	public void signInJakdukUser(User user) {

		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		JakdukPrincipal jakdukPrincipal = new JakdukPrincipal(user.getEmail(), user.getId(),
				user.getPassword(), user.getUsername(), user.getProviderId(),
				enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, new Date(), getAuthorities(user.getRoles()));

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(jakdukPrincipal, user.getPassword(), jakdukPrincipal.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(token);
	}

	/**
	 * SNS 기반 회원의 로그인 처리
	 * @param user User 객체
     */
	public void signInSocialUser(User user) {

		SocialUserDetail userDetail = new SocialUserDetail(user.getId(), user.getEmail(), user.getUsername(), user.getProviderId(), user.getProviderUserId(),
				true, true, true, true, getAuthorities(user.getRoles()));

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(List<Integer> roles) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(roles));

		return authList;
	}

	private List<String> getRoles(List<Integer> roles) {
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

	private static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}

		return authorities;
	}
		
}
