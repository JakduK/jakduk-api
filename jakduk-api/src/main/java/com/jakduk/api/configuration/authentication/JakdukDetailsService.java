package com.jakduk.api.configuration.authentication;

import com.jakduk.core.authentication.common.JakdukUserDetail;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.common.CommonRole;
import com.jakduk.core.exception.FindUserButNotJakdukAccount;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.simple.UserOnAuthentication;
import com.jakduk.core.repository.user.UserRepository;
import com.jakduk.core.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class JakdukDetailsService implements UserDetailsManager {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommonService commonService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		if (ObjectUtils.isEmpty(email)) {
			throw new IllegalArgumentException("email 은 꼭 필요한 값입니다.");
		} else {
			UserOnAuthentication user = userRepository.findAuthUserByEmail(email);

			if (ObjectUtils.isEmpty(user))
				throw new ServiceException(ServiceError.NOT_FOUND_JAKDUK_ACCOUNT,
						commonService.getResourceBundleMessage("messages.common", "common.exception.not.found.jakduk.account", email));

			if (! user.getProviderId().equals(CommonConst.ACCOUNT_TYPE.JAKDUK))
				throw new FindUserButNotJakdukAccount("JakduK 계정이 아니라 SNS 계정으로 연동되어 있습니다. email=" + email, user.getProviderId());

			log.debug("Jakduk user=" + user);

			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;

			JakdukUserDetail jakdukUserDetail = new JakdukUserDetail(user.getEmail(), user.getId()
					, user.getPassword(), user.getUsername(), CommonConst.ACCOUNT_TYPE.JAKDUK
					, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities(user.getRoles()));

			if (log.isInfoEnabled()) {
				log.info("load Jakduk username=" + jakdukUserDetail.getUsername());
			}

			return jakdukUserDetail;
		}
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(List<Integer> roles) {

		return getGrantedAuthorities(getRoles(roles));
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
