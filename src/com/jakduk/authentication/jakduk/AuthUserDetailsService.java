package com.jakduk.authentication.jakduk;

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

import com.jakduk.model.db.User;
import com.jakduk.repository.UserRepository;

@Service
public class AuthUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			AuthUser authUser;
			
			User domainUser = userRepository.findByEmail(email);
			
			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;
			
			logger.debug("email=" + email);
			logger.debug("domainUser=" + domainUser);
			
			if (domainUser != null) {
				authUser = new AuthUser(domainUser.getEmail(), domainUser.getId(), domainUser.getPassword(), domainUser.getUsername(),
						enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities(2));
			} else {
				authUser = new AuthUser("admin", "admin", "21232f297a57a5a743894a0e4a801fc3", "admin", true, true, true, true, getAuthorities(1));
			}
			
			logger.debug("authUser=" + authUser);
			
			return authUser;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
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

}
