package com.jakduk.configuration.authentication;

import com.jakduk.authentication.common.JakdukPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.exception.FindUserButNotJakdukAccount;
import com.jakduk.exception.NotFoundJakdukAccountException;
import com.jakduk.model.simple.UserOnAuthentication;
import com.jakduk.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
			UserOnAuthentication user = userRepository.findAuthUserByEmail(email);

			if (Objects.isNull(user))
				throw new NotFoundJakdukAccountException("로그인 할 사용자 데이터가 존재하지 않습니다. email=" + email);

			if (user.getProviderId().equals(CommonConst.ACCOUNT_TYPE.JAKDUK) == false)
				throw new FindUserButNotJakdukAccount("JakduK 계정이 아니라 SNS 계정으로 연동되어 있습니다. email=" + email, user.getProviderId());

			log.debug("Jakduk user=" + user);

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
