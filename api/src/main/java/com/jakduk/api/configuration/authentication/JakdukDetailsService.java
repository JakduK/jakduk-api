package com.jakduk.api.configuration.authentication;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.user.JakdukUserDetail;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.model.simple.UserOnAuthentication;
import com.jakduk.core.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Slf4j
@Component
public class JakdukDetailsService implements UserDetailsManager {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		if (ObjectUtils.isEmpty(email)) {
			throw new IllegalArgumentException("email 은 꼭 필요한 값입니다.");
		} else {
			Optional<UserOnAuthentication> oUser = userRepository.findAuthUserByEmail(email);

			if (! oUser.isPresent())
				throw new ServiceException(ServiceError.NOT_FOUND_ACCOUNT,
						CoreUtils.getExceptionMessage("exception.not.found.jakduk.account", email));

			UserOnAuthentication user = oUser.get();

			if (! user.getProviderId().equals(CoreConst.ACCOUNT_TYPE.JAKDUK))
				throw new ServiceException(ServiceError.NOT_FOUND_ACCOUNT,
						CoreUtils.getExceptionMessage("exception.not.jakduk.user", email, user.getProviderId()));

			log.debug("Jakduk user=" + user);

			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;

			JakdukUserDetail jakdukUserDetail = new JakdukUserDetail(user.getEmail(), user.getId()
					, user.getPassword(), user.getUsername(), CoreConst.ACCOUNT_TYPE.JAKDUK
					, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, UserUtils.getAuthorities(user.getRoles()));

			if (log.isInfoEnabled()) {
				log.info("load Jakduk username=" + jakdukUserDetail.getUsername());
			}

			return jakdukUserDetail;
		}
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
