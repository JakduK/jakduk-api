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
import com.jakduk.model.embedded.OAuthUser;
import com.jakduk.model.simple.OAuthUserOnLogin;
import com.jakduk.repository.UserRepository;
import com.jakduk.service.UserService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 8. 7.
 * @desc     :
 */

@Service
public class FacebookDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	private String username;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public String getUsername() {
		return username;
	}

	public UserDetails loadUser(String oauthId, String username) {
		this.username = username;
		return loadUserByUsername(oauthId);
	}
	
	@Override
	public UserDetails loadUserByUsername(String oauthId)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
//		FacebookUser facebookUser = facebookService.findUser();
		
		OAuthUserOnLogin oauthUserOnLogin = userRepository.findByOauthUser(CommonConst.OAUTH_TYPE_FACEBOOK, oauthId);
		
		if (oauthUserOnLogin == null) {
			oauthUserOnLogin = new OAuthUserOnLogin();
			OAuthUser oauthUser = new OAuthUser();
			oauthUser.setOauthId(oauthId);
			oauthUser.setType(CommonConst.OAUTH_TYPE_FACEBOOK);
			oauthUser.setAddInfoStatus(CommonConst.OAUTH_ADDITIONAL_INFO_STATUS_BLANK);
			oauthUserOnLogin.setOauthUser(oauthUser);
			
			if (logger.isDebugEnabled()) {
				logger.debug("new oauthuser of facebook=" + oauthUserOnLogin);
			}
			userService.oauthUserWrite(oauthUserOnLogin);
		}
		
		FacebookDetails facebookUserDetails = new FacebookDetails(oauthId, this.username, oauthUserOnLogin.getOauthUser().getAddInfoStatus()
				, true, true, true, true, getAuthorities(2));
		
		return facebookUserDetails;
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
