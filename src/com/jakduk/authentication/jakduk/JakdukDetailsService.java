package com.jakduk.authentication.jakduk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.User;
import com.jakduk.repository.UserRepository;

@Service
public class JakdukDetailsService implements UserDetailsManager {
	
	@Autowired
	UserRepository userRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		if (email != null && (email.equals(CommonConst.OAUTH_TYPE_FACEBOOK) 
				|| email.equals(CommonConst.OAUTH_TYPE_DAUM))) {
			throw new UsernameNotFoundException("not found email=" + email);
		} else {
			User domainUser = userRepository.findByEmail(email);

			if (domainUser != null) {
				boolean enabled = true;
				boolean accountNonExpired = true;
				boolean credentialsNonExpired = true;
				boolean accountNonLocked = true;

				JakdukPrincipal jakdukPrincipal = new JakdukPrincipal(domainUser.getEmail(), domainUser.getId()
						, domainUser.getPassword(), domainUser.getUsername(), CommonConst.AUTH_TYPE_JAKDUK
						, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities(2));
				
				if (logger.isInfoEnabled()) {
					logger.info("load user JakdukPrincipal=" + jakdukPrincipal);
				}

				return jakdukPrincipal;
			} else {
				throw new UsernameNotFoundException("not found email=" + email);
			}
		}
		
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



	@Override
	public void createUser(UserDetails user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void updateUser(UserDetails user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void deleteUser(String username) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void changePassword(String oldPassword, String newPassword) {
		
		logger.debug("aaaaaaaaaaaa");
	}



	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

}
