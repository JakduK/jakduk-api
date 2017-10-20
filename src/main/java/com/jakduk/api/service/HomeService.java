package com.jakduk.api.service;


import com.jakduk.api.dao.JakdukDAO;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Encyclopedia;
import com.jakduk.api.model.db.HomeDescription;
import com.jakduk.api.model.simple.UserOnHome;
import com.jakduk.api.repository.EncyclopediaRepository;
import com.jakduk.api.repository.HomeDescriptionRepository;
import com.jakduk.api.repository.article.comment.ArticleCommentOnHomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 4.
 * @desc     :
 */

@Service
public class HomeService {

	@Autowired
	private JakdukDAO jakdukDAO;
	
	@Autowired private EncyclopediaRepository encyclopediaRepository;
	@Autowired private ArticleCommentOnHomeRepository articleCommentOnHomeRepository;
	@Autowired private HomeDescriptionRepository homeDescriptionRepository;

	/**
	 * 랜덤하게 백과 사전 하나를 가져온다.
	 */
	public Encyclopedia getEncyclopediaWithRandom(String language) {

		List<Encyclopedia> encyclopedias = encyclopediaRepository.findListByLanguage(language);

		if (CollectionUtils.isEmpty(encyclopedias))
			throw new ServiceException(ServiceError.NOT_FOUND_ENCYCLOPEDIA);

		Integer random = (int)(Math.random() * encyclopedias.size());

		return encyclopedias.get(random);
	}

	// 최근 가입 회원 가져오기.
	public List<UserOnHome> getUsersLatest(String language) {
		return jakdukDAO.getUserOnHome(language);
	}

	// 알림판 가져오기.
	public HomeDescription getHomeDescription() {
		return homeDescriptionRepository.findOneByOrderByPriorityDesc()
				.orElseGet(HomeDescription::new);
	}
	
}
