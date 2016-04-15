package com.jakduk.authentication.jakduk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.model.simple.UserOnAuthentication;
import com.jakduk.repository.UserRepository;

@Service
@Slf4j
public class JakdukDetailsService implements UserDetailsManager {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		if (email != null && (email.equals(CommonConst.ACCOUNT_TYPE.FACEBOOK)
				|| email.equals(CommonConst.ACCOUNT_TYPE.DAUM))) {
			throw new UsernameNotFoundException("not found email=" + email);
		} else {
			UserOnAuthentication user = userRepository.userFindByEmail(email);

			if (user != null) {
				boolean enabled = true;
				boolean accountNonExpired = true;
				boolean credentialsNonExpired = true;
				boolean accountNonLocked = true;

				JakdukPrincipal jakdukPrincipal = new JakdukPrincipal(user.getEmail(), user.getId()
						, user.getPassword(), user.getUsername(), CommonConst.ACCOUNT_TYPE.JAKDUK
						, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities(user.getRoles()));
				
				if (log.isInfoEnabled()) {
					log.info("load Jakduk username=" + jakdukPrincipal.getUsername());
				}

				return jakdukPrincipal;
			} else {
				throw new UsernameNotFoundException("not found email=" + email);
			}
		}
		
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
	}



	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

}
