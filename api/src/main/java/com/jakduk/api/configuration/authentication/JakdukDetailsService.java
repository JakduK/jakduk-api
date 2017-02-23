package com.jakduk.api.configuration.authentication;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.user.JakdukUserDetail;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.db.UserImage;
import com.jakduk.core.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

@Slf4j
@Component
public class JakdukDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Resource
	private UserUtils userUtils;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		if (ObjectUtils.isEmpty(email)) {
			throw new IllegalArgumentException("email 은 꼭 필요한 값입니다.");
		} else {
			User user = userRepository.findOneByEmail(email)
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ACCOUNT,
							CoreUtils.getExceptionMessage("exception.not.found.jakduk.account", email)));

			if (! user.getProviderId().equals(CoreConst.ACCOUNT_TYPE.JAKDUK))
				throw new ServiceException(ServiceError.NOT_FOUND_ACCOUNT,
						CoreUtils.getExceptionMessage("exception.not.jakduk.user", email, user.getProviderId()));

			log.debug("Jakduk user=" + user);

			String userImageId = null;
			UserImage userImage = user.getUserImage();

			if (! ObjectUtils.isEmpty(userImage))
				userImageId = userImage.getId();

			JakdukUserDetail jakdukUserDetail = new JakdukUserDetail(user.getEmail(), user.getId()
					, user.getPassword(), user.getUsername(), CoreConst.ACCOUNT_TYPE.JAKDUK, userUtils.generateUserImageUrl(userImageId)
					, true, true, true, true, UserUtils.getAuthorities(user.getRoles()));

			log.info("load Jakduk username=" + jakdukUserDetail.getUsername());

			return jakdukUserDetail;
		}
	}

}
