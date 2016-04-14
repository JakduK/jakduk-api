package com.jakduk.authentication.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import com.jakduk.common.CommonConst;
import com.jakduk.model.embedded.SocialInfo;
import com.jakduk.model.simple.SocialUserOnLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jakduk.authentication.daum.DaumUser;
import com.jakduk.authentication.facebook.FacebookUser;
import com.jakduk.common.CommonRole;
import com.jakduk.repository.UserRepository;
import com.jakduk.service.UserService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 15.
 * @desc     :
 */

@Service
@Slf4j
public class OAuthDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	private String username;
	
	private CommonConst.ACCOUNT_TYPE providerId;

	public String getUsername() {
		return username;
	}
	
	public CommonConst.ACCOUNT_TYPE getProviderId() {
		return providerId;
	}

	public UserDetails loadUser(String oauthId, String username, CommonConst.ACCOUNT_TYPE providerId) {
		this.username = username;
		this.providerId = providerId;
		return loadUserByUsername(oauthId);
	}
	
	@Override
	public UserDetails loadUserByUsername(String oauthId)	throws UsernameNotFoundException {
		
		SocialUserOnLogin user = userRepository.findByOauthUser(providerId, oauthId);
		
		if (user == null) {
			SocialUserOnLogin oauthUserOnLogin = new SocialUserOnLogin();
			oauthUserOnLogin.setUsername(username);			
			SocialInfo oauthUser = new SocialInfo();
			oauthUser.setOauthId(oauthId);
			oauthUser.setProviderId(providerId);
			oauthUserOnLogin.setSocialInfo(oauthUser);
			
			ArrayList<Integer> roles = new ArrayList<Integer>();
			roles.add(CommonRole.ROLE_NUMBER_USER_02);
			
			oauthUserOnLogin.setRoles(roles);
			
			userService.oauthUserWrite(oauthUserOnLogin);
			
			user = userRepository.findByOauthUser(providerId, oauthId);
			
			if (log.isInfoEnabled()) {
				log.info("new oauth user joined. username=" + username);
			}
			
			if (log.isDebugEnabled()) {
				log.debug("oauth user info=" + user);
			}
		}
		
		OAuthPrincipal principal = new OAuthPrincipal(user.getId(), oauthId, user.getUsername(), providerId,
				true, true, true, true, getAuthorities(user.getRoles()));
		
		return principal;
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
	
	public CommonUser getUserDetails(DaumUser user) {
		CommonUser userDetails = new CommonUser();
		
		if (user.getImagePath() != null) {
			userDetails.setImagePath(user.getImagePath());
		}	
		
		return userDetails;
	}
	
	public CommonUser getUserDetails(FacebookUser user) {
		CommonUser userDetails = new CommonUser();
		
		if (user.getEmail() != null) {
			userDetails.setEmail(user.getEmail());
		}
		
		if (user.getGender() != null) {
			userDetails.setGender(user.getGender());
		}
		
		if (user.getBirthday() != null) {
			userDetails.setBirthday(user.getBirthday());
		}
		
		if (user.getLink() != null) {
			userDetails.setLink(user.getLink());
		}
		
		if (user.getLocale() != null) {
			userDetails.setLocale(user.getLocale());
		}
		
		if (user.getBio() != null) {
			userDetails.setBio(user.getBio());
		}
		
		return userDetails;
	}
}
