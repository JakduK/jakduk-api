package com.jakduk.authentication.jakduk;

import java.util.*;

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

		if (Objects.isNull(email)) {
			throw new IllegalArgumentException("email 은 꼭 필요한 값입니다.");
		} else {
			UserOnAuthentication user = userRepository.userFindByEmail(email);

			if (Objects.isNull(user))
				throw new UsernameNotFoundException("로그인 할 사용자 데이터가 존재하지 않습니다. email=" + email);

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
	}

	@Override
	public void updateUser(UserDetails user) {
	}

	@Override
	public void deleteUser(String username) {
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
	}

	@Override
	public boolean userExists(String username) {
		return false;
	}

}
