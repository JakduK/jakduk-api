package com.jakduk.core.service;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.Encyclopedia;
import com.jakduk.core.model.db.HomeDescription;
import com.jakduk.core.model.simple.BoardFreeCommentOnHome;
import com.jakduk.core.model.simple.GallerySimple;
import com.jakduk.core.model.simple.UserOnHome;
import com.jakduk.core.repository.EncyclopediaRepository;
import com.jakduk.core.repository.board.free.comment.BoardFreeCommentOnHomeRepository;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
	
	@Autowired
	private EncyclopediaRepository encyclopediaRepository;
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;

	@Autowired
	private BoardFreeCommentOnHomeRepository boardFreeCommentOnHomeRepository;

	/**
	 * 랜덤하게 백과 사전 하나를 가져온다.
	 */
	public Encyclopedia getEncyclopediaWithRandom(String language) {

		List<Encyclopedia> encyclopedias = encyclopediaRepository.findListByLanguage(language);

		if (ObjectUtils.isEmpty(encyclopedias))
			throw new ServiceException(ServiceError.NOT_FOUND_ENCYCLOPEDIA);

		int random = (int)(Math.random() * encyclopedias.size());

		return encyclopedias.get(random);
	}

	// 최근 가입 회원 가져오기.
	public List<UserOnHome> getUsersLatest(String language) {
		return jakdukDAO.getUserOnHome(language);
	}

	/**
	 * 최근 그림 목록 가져오기
	 * @return 사진 목록
     */
	public List<GallerySimple> getGalleriesLatest() {
		return jakdukDAO.findGalleriesById(Direction.DESC, CoreConst.HOME_SIZE_GALLERY, null);
	}

	// 최근 댓글 가져오기.
	public List<BoardFreeCommentOnHome> getBoardCommentsLatest() {
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(0, CoreConst.HOME_SIZE_LINE_NUMBER, sort);
		
		List<BoardFreeCommentOnHome> comments = boardFreeCommentOnHomeRepository.findAll(pageable).getContent();
		
		for (BoardFreeCommentOnHome comment : comments) {
			String content = CoreUtils.stripHtmlTag(comment.getContent());

			if (StringUtils.isNotBlank(content)) {
				Integer contentLength = content.length() + comment.getWriter().getUsername().length();

				if (contentLength > CoreConst.HOME_COMMENT_CONTENT_MAX_LENGTH) {
					content = content.substring(0, CoreConst.HOME_COMMENT_CONTENT_MAX_LENGTH - comment.getWriter().getUsername().length());
					content = String.format("%s...", content);
				}
				comment.setContent(content);
			}
		}

		return comments;
	}

	// 알림판 가져오기.
	public HomeDescription getHomeDescription() {
		
		HomeDescription homeDescription = jakdukDAO.getHomeDescription();

		if (Objects.nonNull(homeDescription)) {
			return homeDescription;
		} else {
			return new HomeDescription();
		}
	}
	
}
