package org.jakduk.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.jakduk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public UserDetails loadUserByUsername(String principle)
			throws UsernameNotFoundException {
		try {
			User authenticatedUser;
			
			org.jakduk.model.User domainUser = userRepository.findByUserName(principle);
			StandardPasswordEncoder encoder = new StandardPasswordEncoder();
			
			logger.debug("domainUser=" + domainUser);
			
			if (domainUser != null) {
				authenticatedUser = new User(domainUser.getPrinciple(), 
						domainUser.getPassword(), true, true, true, true, getAuthorities(2));
			} else {
				authenticatedUser = new User("admin", "21232f297a57a5a743894a0e4a801fc3", true, true, true, true, getAuthorities(1));
			}
			
			logger.debug("authenticatedUser=" + authenticatedUser);
			
			return authenticatedUser;
			
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
	
	public static List<GrantedAuthority> getGrantedAuthorities(
			List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

}
