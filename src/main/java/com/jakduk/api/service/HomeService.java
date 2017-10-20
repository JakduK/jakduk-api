package com.jakduk.api.service;


import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.dao.JakdukDAO;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Encyclopedia;
import com.jakduk.api.model.db.HomeDescription;
import com.jakduk.api.model.simple.HomeArticleComment;
import com.jakduk.api.model.simple.UserOnHome;
import com.jakduk.api.repository.EncyclopediaRepository;
import com.jakduk.api.repository.HomeDescriptionRepository;
import com.jakduk.api.repository.article.comment.ArticleCommentOnHomeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
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

	// 최근 댓글 가져오기.
	public List<HomeArticleComment> getBoardCommentsLatest() {
		
		Sort sort = new Sort(Direction.DESC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(0, Constants.HOME_SIZE_LINE_NUMBER, sort);
		
		List<HomeArticleComment> comments = articleCommentOnHomeRepository.findAll(pageable).getContent();
		
		for (HomeArticleComment comment : comments) {
			String content = JakdukUtils.stripHtmlTag(comment.getContent());

			if (StringUtils.isNotBlank(content)) {
				Integer contentLength = content.length() + comment.getWriter().getUsername().length();

				if (contentLength > Constants.HOME_COMMENT_CONTENT_MAX_LENGTH) {
					content = content.substring(0, Constants.HOME_COMMENT_CONTENT_MAX_LENGTH - comment.getWriter().getUsername().length());
					content = String.format("%s...", content);
				}
				comment.setContent(content);
			}
		}

		return comments;
	}

	// 알림판 가져오기.
	public HomeDescription getHomeDescription() {
		return homeDescriptionRepository.findOneByOrderByPriorityDesc()
				.orElseGet(HomeDescription::new);
	}
	
}
