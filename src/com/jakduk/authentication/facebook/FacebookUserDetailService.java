package com.jakduk.authentication.facebook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jakduk.common.CommonConst;
import com.jakduk.model.simple.OAuthUser;
import com.jakduk.model.simple.OAuthUserWrite;
import com.jakduk.repository.UserRepository;
import com.jakduk.service.UserService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 8. 7.
 * @desc     :
 */

@Service
public class FacebookUserDetailService implements UserDetailsService {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private FacebookService facebookService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		FacebookUser fUser = facebookService.findUser();
		
		OAuthUser oauthUser = new OAuthUser();
		oauthUser.setOauthId(fUser.getId());
		oauthUser.setType(CommonConst.OAUTH_TYPE_FACEBOOK);
		
		OAuthUserWrite oauthUserWrite = userRepository.findByOauthUser(oauthUser);
		
		if (oauthUserWrite == null) {
			oauthUserWrite = new OAuthUserWrite();
			oauthUserWrite.setOauthUser(oauthUser);
			if (logger.isDebugEnabled()) {
				logger.debug("new oauthuser of facebook=" + oauthUserWrite);
			}
			userService.oauthUserWrite(oauthUserWrite);
		}
		
		logger.debug("phjang02=" + fUser);
		
		FacebookUserDetails facebookUser02 = new FacebookUserDetails(fUser.getId(), fUser.getName(), fUser.getUsername()
				, "", true, true, true, true, getAuthorities(2));
		
		return facebookUser02;
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
		return authList;
	}
	
	public List<String> getRoles(Integer role) {
		List<String> roles = new ArrayList<String>();

		if (role.intValue() == 1) {
			roles.add("ROLE_USER");
			roles.add("ROLE_ADMIN");

		} else if (role.intValue() == 2) {
			roles.add("ROLE_TEST_01");
		}

		return roles;
	}
	
	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

}
