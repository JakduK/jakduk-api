package com.jakduk.api.service;

import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.restcontroller.home.vo.LatestPost;
import com.jakduk.api.restcontroller.vo.BoardGallery;
import com.jakduk.api.vo.board.*;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.dao.BoardDAO;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.db.BoardFreeComment;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.*;
import com.jakduk.core.model.etc.BoardFeelingCount;
import com.jakduk.core.model.etc.BoardFreeOnBest;
import com.jakduk.core.model.etc.CommonCount;
import com.jakduk.core.model.simple.*;
import com.jakduk.core.repository.board.category.BoardCategoryRepository;
import com.jakduk.core.repository.board.free.BoardFreeOnListRepository;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import com.jakduk.core.repository.board.free.comment.BoardFreeCommentRepository;
import com.jakduk.core.repository.gallery.GalleryRepository;
import com.jakduk.core.service.CommonGalleryService;
import com.jakduk.core.service.CommonSearchService;
import com.jakduk.core.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BoardFreeService {

	@Autowired
	private BoardFreeRepository boardFreeRepository;

	@Autowired
	private BoardFreeOnListRepository boardFreeOnListRepository;

	@Autowired
	private BoardFreeCommentRepository boardFreeCommentRepository;

	@Autowired
	private BoardCategoryRepository boardCategoryRepository;

	@Autowired
	private GalleryRepository galleryRepository;

	@Autowired
	private BoardDAO boardDAO;

	@Autowired
	private CommonService commonService;

	@Autowired
	private CommonSearchService commonSearchService;

	@Autowired
	private CommonGalleryService commonGalleryService;

	@Resource
	private ApiUtils apiUtils;

	public BoardFree findOneBySeq(Integer seq) {
        return boardFreeRepository.findOneBySeq(seq)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_POST));
    }

	public BoardFreeOfMinimum findBoardFreeOfMinimumBySeq(Integer seq) {
		return boardFreeRepository.findBoardFreeOfMinimumBySeq(seq);
	}

	public Integer countCommentsByBoardItem(BoardItem boardItem) {
		return boardFreeCommentRepository.countByBoardItem(boardItem);
	}

    /**
     * 자유게시판 글쓰기
	 *
     * @param subject 글 제목
     * @param content 글 내용
     * @param categoryCode 글 말머리 Code
     * @param galleries 글과 연동된 사진들
     * @param device 디바이스
     */
	public BoardFree insertFreePost(CommonWriter writer, String subject, String content, CoreConst.BOARD_CATEGORY_TYPE categoryCode,
								  List<Gallery> galleries, CoreConst.DEVICE_TYPE device) {

		boardCategoryRepository.findOneByCode(categoryCode.name())
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_CATEGORY));

		// shortContent 만듦
		String stripHtmlContent = StringUtils.defaultIfBlank(CoreUtils.stripHtmlTag(content), StringUtils.EMPTY);
		String shortContent = StringUtils.truncate(stripHtmlContent, CoreConst.BOARD_SHORT_CONTENT_LENGTH);

		// 글 상태
		BoardStatus boardStatus = BoardStatus.builder()
				.device(device)
				.build();

		// boardHistory
		List<BoardHistory> histories = new ArrayList<>();
		ObjectId boardHistoryId = new ObjectId();
		BoardHistory history = new BoardHistory(boardHistoryId.toString(), CoreConst.BOARD_HISTORY_TYPE.CREATE, writer);
		histories.add(history);

		// lastUpdated
		LocalDateTime lastUpdated = LocalDateTime.ofInstant(boardHistoryId.getDate().toInstant(), ZoneId.systemDefault());

		// 연관된 사진 id 배열 (검증 후)
		List<String> galleryIds = galleries.stream()
				.map(Gallery::getId)
				.collect(Collectors.toList());

		BoardFree boardFree = BoardFree.builder()
				.writer(writer)
				.category(categoryCode)
				.subject(subject)
				.content(content)
				.shortContent(shortContent)
				.views(0)
				.seq(commonService.getNextSequence(CoreConst.BOARD_TYPE.BOARD_FREE.name()))
				.status(boardStatus)
				.history(histories)
				.lastUpdated(lastUpdated)
				.linkedGallery(! galleries.isEmpty())
				.build();

		boardFreeRepository.save(boardFree);

	 	// 엘라스틱서치 색인 요청
		commonSearchService.indexDocumentBoard(boardFree.getId(), boardFree.getSeq(), boardFree.getWriter(), boardFree.getSubject(),
				boardFree.getContent(), boardFree.getCategory().name(), galleryIds);

		/*
		// 슬랙 알림
		slackService.sendPost(
				boardFree.getWriter().getUsername(),
				boardFree.getSubject(),
				"New post created.",
				UrlUtils.buildFullRequestUrl(
						request.getScheme(),
						request.getServerName(),
						request.getServerPort(),
						request.getContextPath(), null) + "/board/free/" + boardFree.getSeq()
		);
		*/

		log.info("new post created. post seq={}, subject={}", boardFree.getSeq(), boardFree.getSubject());

		return boardFree;
	}

	/**
	 * 자유게시판 글 고치기
	 *
	 * @param seq 글 seq
	 * @param subject 글 제목
	 * @param content 글 내용
	 * @param categoryCode 글 말머리 Code
	 * @param galleries 글과 연동된 사진들
     * @param device 디바이스
     */
	public BoardFree updateFreePost(CommonWriter writer, Integer seq, String subject, String content, CoreConst.BOARD_CATEGORY_TYPE categoryCode,
								  List<Gallery> galleries, CoreConst.DEVICE_TYPE device) {

		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_POST));

		if (! boardFree.getWriter().getUserId().equals(writer.getUserId()))
			throw new ServiceException(ServiceError.FORBIDDEN);

		// shortContent 만듦
		String stripHtmlContent = StringUtils.defaultIfBlank(CoreUtils.stripHtmlTag(content), StringUtils.EMPTY);
		String shortContent = StringUtils.truncate(stripHtmlContent, CoreConst.BOARD_SHORT_CONTENT_LENGTH);

		boardFree.setSubject(subject);
		boardFree.setContent(content);
		boardFree.setCategory(categoryCode);
		boardFree.setShortContent(shortContent);

		// 연관된 사진 id 배열 (검증 후)
		List<String> galleryIds = galleries.stream()
				.map(Gallery::getId)
				.collect(Collectors.toList());

		boardFree.setLinkedGallery(! galleries.isEmpty());

		// 글 상태
		BoardStatus boardStatus = boardFree.getStatus();

		if (Objects.isNull(boardStatus))
			boardStatus = new BoardStatus();

        boardStatus.setDevice(device);
		boardFree.setStatus(boardStatus);

		// boardHistory
		List<BoardHistory> histories = boardFree.getHistory();

		if (ObjectUtils.isEmpty(histories))
			histories = new ArrayList<>();

		ObjectId boardHistoryId = new ObjectId();
		BoardHistory history = new BoardHistory(boardHistoryId.toString(), CoreConst.BOARD_HISTORY_TYPE.EDIT, writer);
		histories.add(history);
		boardFree.setHistory(histories);

		boardFree.setLastUpdated(LocalDateTime.ofInstant(boardHistoryId.getDate().toInstant(), ZoneId.systemDefault()));

		boardFreeRepository.save(boardFree);

		// 엘라스틱서치 색인 요청
		commonSearchService.indexDocumentBoard(boardFree.getId(), boardFree.getSeq(), boardFree.getWriter(), boardFree.getSubject(),
				boardFree.getContent(), boardFree.getCategory().name(), galleryIds);

		log.info("post was edited. post seq={}, subject=", boardFree.getSeq(), boardFree.getSubject());

		return boardFree;
	}

	/**
	 * 자유게시판 글 지움
	 *
	 * @param seq 글 seq
	 * @return CoreConst.BOARD_DELETE_TYPE
     */
    public CoreConst.BOARD_DELETE_TYPE deleteFreePost(CommonWriter writer, Integer seq) {

        BoardFree boardFree = boardFreeRepository.findOneBySeq(seq)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_POST));

        if (! boardFree.getWriter().getUserId().equals(writer.getUserId()))
            throw new ServiceException(ServiceError.FORBIDDEN);

        BoardItem boardItem = new BoardItem(boardFree.getId(), boardFree.getSeq());

        Integer count = boardFreeCommentRepository.countByBoardItem(boardItem);

        // 댓글이 하나라도 달리면 글을 몽땅 지우지 못한다.
        if (count > 0) {
			boardFree.setContent(null);
			boardFree.setSubject(null);
			boardFree.setWriter(null);

            List<BoardHistory> histories = boardFree.getHistory();

            if (Objects.isNull(histories))
                histories = new ArrayList<>();

            BoardHistory history = new BoardHistory(new ObjectId().toString(), CoreConst.BOARD_HISTORY_TYPE.DELETE, writer);
            histories.add(history);
			boardFree.setHistory(histories);

            BoardStatus boardStatus = boardFree.getStatus();

            if (Objects.isNull(boardStatus))
                boardStatus = new BoardStatus();

            boardStatus.setDelete(true);
			boardFree.setStatus(boardStatus);

            boardFreeRepository.save(boardFree);

			log.info("A post was deleted(post only). post seq={}, subject={}", boardFree.getSeq(), boardFree.getSubject());
        }
		// 몽땅 지우기
        else {
            boardFreeRepository.delete(boardFree);

			log.info("A post was deleted(all). post seq={}, subject={}", boardFree.getSeq(), boardFree.getSubject());
        }

        // 연결된 사진 끊기
        if (boardFree.isLinkedGallery()) {
			List<Gallery> galleries = galleryRepository.findByItemIdAndFromType(
					new ObjectId(boardFree.getId()), CoreConst.GALLERY_FROM_TYPE.BOARD_FREE, 100);

			galleries.forEach(gallery -> {
				List<LinkedItem> linkedItems = gallery.getLinkedItems();
				Boolean isRemoved = linkedItems.removeIf(
						linkedItem -> linkedItem.getId().equals(boardFree.getId()) &&
								linkedItem.getFrom().equals(CoreConst.GALLERY_FROM_TYPE.BOARD_FREE));

				if (isRemoved && linkedItems.size() >= 1) {
					gallery.setLinkedItems(linkedItems);
					galleryRepository.save(gallery);
				} else if (isRemoved && linkedItems.size() < 1) {
					commonGalleryService.deleteGallery(gallery.getId(), gallery.getContentType());

                    // 엘라스틱 서치 document 삭제.
                    commonSearchService.deleteDocumentGallery(gallery.getId());
				}
			});
		}

		// 색인 지움
        commonSearchService.deleteDocumentBoard(boardFree.getId());

        return count > 0 ? CoreConst.BOARD_DELETE_TYPE.CONTENT : CoreConst.BOARD_DELETE_TYPE.ALL;
    }

	/**
	 * 자유게시판 글 목록
	 *
	 * @param category 말머리
	 * @param page 페이지
	 * @param size 크기
     * @return 글 목록
     */
	public FreePostsResponse getFreePosts(CoreConst.BOARD_CATEGORY_TYPE category, Integer page, Integer size) {

		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));
		Pageable pageable = new PageRequest(page - 1, size, sort);
		Page<BoardFreeOnList> postsPage = null;

		switch (category) {
			case ALL:
				postsPage = boardFreeOnListRepository.findAll(pageable);
				break;
			case FOOTBALL:
			case FREE:
				postsPage = boardFreeOnListRepository.findByCategory(category, pageable);
				break;
		}

		// 자유 게시판 공지글 목록
		List<BoardFreeOnList> notices = boardFreeRepository.findNotices(sort);

		// 게시물 VO 변환 및 썸네일 URL 추가
		Function<BoardFreeOnList, FreePost> convertToFreePost = post -> {
			FreePost freePosts = new FreePost();
			BeanUtils.copyProperties(post, freePosts);

			if (post.isLinkedGallery()) {
				List<Gallery> galleries = galleryRepository.findByItemIdAndFromType(
						new ObjectId(post.getId()), CoreConst.GALLERY_FROM_TYPE.BOARD_FREE, 1);

				if (! ObjectUtils.isEmpty(galleries)) {
					List<BoardGallery> boardGalleries = galleries.stream()
							.map(gallery -> BoardGallery.builder()
									.id(gallery.getId())
									.thumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, gallery.getId()))
									.build())
							.collect(Collectors.toList());

					freePosts.setGalleries(boardGalleries);
				}
			}

			return freePosts;
		};

		List<FreePost> freePosts = postsPage.getContent().stream()
				.map(convertToFreePost)
				.collect(Collectors.toList());

		List<FreePost> freeNotices = notices.stream()
				.map(convertToFreePost)
				.collect(Collectors.toList());

		// Board ID 뽑아내기.
		ArrayList<ObjectId> ids = new ArrayList<>();

		Consumer<FreePost> extractIds = board -> {
			String boardId = board.getId();
			ObjectId objId = new ObjectId(boardId);

			ids.add(objId);
		};

		freePosts.forEach(extractIds);
		freeNotices.forEach(extractIds);

		// 게시물의 댓글수
		List<CommonCount> numberOfItems = boardFreeCommentRepository.findCommentsCountByIds(ids);

		Map<String, Integer> commentCounts =  numberOfItems.stream()
				.collect(Collectors.toMap(CommonCount::getId, CommonCount::getCount));

		// 게시물의 감정수
		Map<String, BoardFeelingCount> feelingCounts = boardDAO.getBoardFreeUsersFeelingCount(ids);

		// 댓글수, 감정 표현수 합치기.
		Consumer<FreePost> applyCounts = board -> {
			String boardId = board.getId();
			Integer commentCount = commentCounts.get(boardId);

			if (! ObjectUtils.isEmpty(commentCount))
				board.setCommentCount(commentCount);

			BoardFeelingCount feelingCount = feelingCounts.get(boardId);

			if (Objects.nonNull(feelingCount)) {
				board.setLikingCount(feelingCount.getUsersLikingCount());
				board.setDislikingCount(feelingCount.getUsersDisLikingCount());
			}
		};

		freePosts.forEach(applyCounts);
		freeNotices.forEach(applyCounts);

		// 말머리
		List<BoardCategory> categories = boardCategoryRepository.findByLanguage(CoreUtils.getLanguageCode(LocaleContextHolder.getLocale(), null));

		Map<String, String> categoriesMap = categories.stream()
				.collect(Collectors.toMap(BoardCategory::getCode, boardCategory -> boardCategory.getNames().get(0).getName()));

		categoriesMap.put("ALL", CoreUtils.getResourceBundleMessage("messages.board", "board.category.all"));

		return FreePostsResponse.builder()
				.categories(categoriesMap)
				.posts(freePosts)
				.notices(freeNotices)
				.first(postsPage.isFirst())
				.last(postsPage.isLast())
				.totalPages(postsPage.getTotalPages())
				.totalElements(postsPage.getTotalElements())
				.numberOfElements(postsPage.getNumberOfElements())
				.size(postsPage.getSize())
				.number(postsPage.getNumber())
				.build();
	}

	/**
	 * 최근 글 가져오기
	 */
	public List<LatestPost> getFreeLatest() {

		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));

		List<BoardFreeOnList> posts = boardFreeRepository.findLatest(sort, CoreConst.HOME_SIZE_POST);

		// 게시물 VO 변환 및 썸네일 URL 추가
		List<LatestPost> latestPosts = posts.stream()
				.map(post -> {
					LatestPost latestPost = new LatestPost();
					BeanUtils.copyProperties(post, latestPost);

					if (post.isLinkedGallery()) {
						List<Gallery> galleries = galleryRepository.findByItemIdAndFromType(
								new ObjectId(post.getId()), CoreConst.GALLERY_FROM_TYPE.BOARD_FREE, 1);

						List<BoardGallery> boardGalleries = galleries.stream()
								.sorted(Comparator.comparing(Gallery::getId))
								.limit(1)
								.map(gallery -> BoardGallery.builder()
										.id(gallery.getId())
										.thumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, gallery.getId()))
										.build())
								.collect(Collectors.toList());

						latestPost.setGalleries(boardGalleries);
					}

					return latestPost;
				})
				.collect(Collectors.toList());

		return latestPosts;
	}

    /**
     * 글 감정 표현.
     */
	public BoardFree setFreeFeelings(CommonWriter writer, Integer seq, CoreConst.FEELING_TYPE feeling) {

		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_POST));

        String userId = writer.getUserId();
        String username = writer.getUsername();

		CommonWriter postWriter = boardFree.getWriter();

		List<CommonFeelingUser> usersLiking = boardFree.getUsersLiking();
		List<CommonFeelingUser> usersDisliking = boardFree.getUsersDisliking();

		if (Objects.isNull(usersLiking)) usersLiking = new ArrayList<>();
		if (Objects.isNull(usersDisliking)) usersDisliking = new ArrayList<>();

		// 이 게시물의 작성자라서 감정 표현을 할 수 없음
		if (userId.equals(postWriter.getUserId()))
			throw new ServiceException(ServiceError.FEELING_YOU_ARE_WRITER);

        // 해당 회원이 좋아요를 이미 했는지 검사
		Optional<CommonFeelingUser> alreadyLike = usersLiking.stream()
                .filter(commonFeelingUser -> commonFeelingUser.getUserId().equals(userId))
                .findFirst();

        // 해당 회원이 싫어요를 이미 했는지 검사
        Optional<CommonFeelingUser> alreadyDislike = usersDisliking.stream()
                .filter(commonFeelingUser -> commonFeelingUser.getUserId().equals(userId))
                .findFirst();

		CommonFeelingUser feelingUser = new CommonFeelingUser(new ObjectId().toString(), userId, username);

		switch (feeling) {
			case LIKE:
			    // 이미 좋아요를 했을 때, 좋아요를 취소
			    if (alreadyLike.isPresent()) {
			        usersLiking.remove(alreadyLike.get());
                }
                // 이미 싫어요를 했을 때, 싫어요를 없애고 좋아요로 바꿈
                else if (alreadyDislike.isPresent()) {
			        usersDisliking.remove(alreadyDislike.get());
			        usersLiking.add(feelingUser);

                    boardFree.setUsersDisliking(usersDisliking);
                }
                // 아직 감정 표현을 하지 않아 좋아요로 등록
                else {
			    	usersLiking.add(feelingUser);
				}

				boardFree.setUsersLiking(usersLiking);

				break;

			case DISLIKE:
                // 이미 싫어요를 했을 때, 싫어요를 취소
                if (alreadyDislike.isPresent()) {
                    usersDisliking.remove(alreadyDislike.get());
                }
                // 이미 좋아요를 했을 때, 좋아요를 없애고 싫어요로 바꿈
                else if (alreadyLike.isPresent()) {
                    usersLiking.remove(alreadyLike.get());
                    usersDisliking.add(feelingUser);

                    boardFree.setUsersLiking(usersLiking);
                }
				// 아직 감정 표현을 하지 않아 싫어요로 등록
                else {
					usersDisliking.add(feelingUser);
				}

				boardFree.setUsersDisliking(usersDisliking);

				break;
		}

		boardFreeRepository.save(boardFree);

		return boardFree;
	}

	/**
	 * 게시판 댓글 달기
	 */
	public BoardFreeComment insertFreeComment(Integer seq, CommonWriter writer, String content, List<Gallery> galleries,
											  CoreConst.DEVICE_TYPE device) {

		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_POST));

		// 연관된 사진 id 배열 (검증 후)
		List<String> galleryIds = galleries.stream()
				.map(Gallery::getId)
				.collect(Collectors.toList());

		BoardFreeComment boardFreeComment = BoardFreeComment.builder()
				.boardItem(new BoardItem(boardFree.getId(), boardFree.getSeq()))
				.writer(writer)
				.content(content)
				.status(new BoardCommentStatus(device))
				.linkedGallery(! galleries.isEmpty())
				.build();

		boardFreeCommentRepository.save(boardFreeComment);

		// 엘라스틱서치 색인 요청
		commonSearchService.indexDocumentBoardComment(boardFreeComment.getId(), boardFreeComment.getBoardItem(), boardFreeComment.getWriter(),
				boardFreeComment.getContent(), galleryIds);

		return boardFreeComment;
	}

	/**
	 * 게시판 댓글 고치기
	 */
	public BoardFreeComment updateFreeComment(String id, Integer seq, CommonWriter writer, String content, List<Gallery> galleries,
											  CoreConst.DEVICE_TYPE device) {

		boardFreeRepository.findOneBySeq(seq)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_POST));

		BoardFreeComment boardFreeComment = boardFreeCommentRepository.findOneById(id)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_COMMENT));

		if (! boardFreeComment.getWriter().getUserId().equals(writer.getUserId()))
			throw new ServiceException(ServiceError.FORBIDDEN);

		boardFreeComment.setWriter(writer);
		boardFreeComment.setContent(StringUtils.trim(content));
		BoardCommentStatus boardCommentStatus = boardFreeComment.getStatus();

		if (Objects.isNull(boardCommentStatus)) {
			boardCommentStatus = new BoardCommentStatus(device);
		} else {
			boardCommentStatus.setDevice(device);
		}

		boardFreeComment.setStatus(boardCommentStatus);

		// 연관된 사진 id 배열 (검증 후)
		List<String> galleryIds = galleries.stream()
				.map(Gallery::getId)
				.collect(Collectors.toList());

		boardFreeComment.setLinkedGallery(! galleries.isEmpty());

		boardFreeCommentRepository.save(boardFreeComment);

		// 엘라스틱서치 색인 요청
		commonSearchService.indexDocumentBoardComment(boardFreeComment.getId(), boardFreeComment.getBoardItem(), boardFreeComment.getWriter(),
				boardFreeComment.getContent(), galleryIds);

		return boardFreeComment;
	}

	/**
	 * 게시판 댓글 지움
	 */
	public void deleteFreeComment(String id, CommonWriter writer) {

		BoardFreeComment boardFreeComment = boardFreeCommentRepository.findOneById(id)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_COMMENT));

		if (! boardFreeComment.getWriter().getUserId().equals(writer.getUserId()))
			throw new ServiceException(ServiceError.FORBIDDEN);

		boardFreeCommentRepository.delete(id);

		// 색인 지움
		commonSearchService.deleteDocumentBoardComment(id);
	}

	// 게시판 댓글 목록
	public List<BoardFreeComment> getFreeComments(Integer seq, String commentId) {

		List<BoardFreeComment> comments;

		if (! ObjectUtils.isEmpty(commentId)) {
			comments  = boardFreeCommentRepository.findByBoardSeqAndGTId(seq, new ObjectId(commentId));
		} else {
			comments  = boardFreeCommentRepository.findByBoardSeqAndGTId(seq, null);
		}

		return comments;
	}

	/**
	 * 자유게시판 댓글 감정 표현.
	 *
	 * @param commentId 댓글 ID
	 * @param feeling 감정표현 종류
     * @return 자유게시판 댓글 객체
     */
	public BoardFreeComment setFreeCommentFeeling(CommonWriter writer, String commentId, CoreConst.FEELING_TYPE feeling) {

		BoardFreeComment boardComment = boardFreeCommentRepository.findOneById(commentId)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_COMMENT));

		String userId = writer.getUserId();
		String username = writer.getUsername();

		CommonWriter postWriter = boardComment.getWriter();

		List<CommonFeelingUser> usersLiking = boardComment.getUsersLiking();
		List<CommonFeelingUser> usersDisliking = boardComment.getUsersDisliking();

		if (Objects.isNull(usersLiking)) usersLiking = new ArrayList<>();
		if (Objects.isNull(usersDisliking)) usersDisliking = new ArrayList<>();

		// 이 게시물의 작성자라서 감정 표현을 할 수 없음
		if (userId.equals(postWriter.getUserId()))
			throw new ServiceException(ServiceError.FEELING_YOU_ARE_WRITER);

		// 해당 회원이 좋아요를 이미 했는지 검사
		Optional<CommonFeelingUser> alreadyLike = usersLiking.stream()
				.filter(commonFeelingUser -> commonFeelingUser.getUserId().equals(userId))
				.findFirst();

		// 해당 회원이 싫어요를 이미 했는지 검사
		Optional<CommonFeelingUser> alreadyDislike = usersDisliking.stream()
				.filter(commonFeelingUser -> commonFeelingUser.getUserId().equals(userId))
				.findFirst();

		CommonFeelingUser feelingUser = new CommonFeelingUser(new ObjectId().toString(), userId, username);

		switch (feeling) {
			case LIKE:
				// 이미 좋아요를 했을 때, 좋아요를 취소
				if (alreadyLike.isPresent()) {
					usersLiking.remove(alreadyLike.get());
				}
				// 이미 싫어요를 했을 때, 싫어요를 없애고 좋아요로 바꿈
				else if (alreadyDislike.isPresent()) {
					usersDisliking.remove(alreadyDislike.get());
					usersLiking.add(feelingUser);

					boardComment.setUsersDisliking(usersDisliking);
				}
				// 아직 감정 표현을 하지 않아 좋아요로 등록
				else {
					usersLiking.add(feelingUser);
				}

				boardComment.setUsersLiking(usersLiking);

				break;

			case DISLIKE:
				// 이미 싫어요를 했을 때, 싫어요를 취소
				if (alreadyDislike.isPresent()) {
					usersDisliking.remove(alreadyDislike.get());
				}
				// 이미 좋아요를 했을 때, 좋아요를 없애고 싫어요로 바꿈
				else if (alreadyLike.isPresent()) {
					usersLiking.remove(alreadyLike.get());
					usersDisliking.add(feelingUser);

					boardComment.setUsersLiking(usersLiking);
				}
				// 아직 감정 표현을 하지 않아 싫어요로 등록
				else {
					usersDisliking.add(feelingUser);
				}

				boardComment.setUsersDisliking(usersDisliking);

				break;
		}

		boardFreeCommentRepository.save(boardComment);

		return boardComment;
	}

	/**
	 * 자유게시판 글의 공지를 활성화/비활성화 한다.
	 * @param seq 글 seq
	 * @param isEnable 활성화/비활성화
     */
	public void setFreeNotice(CommonWriter writer, int seq, boolean isEnable) {

		Optional<BoardFree> boardFree = boardFreeRepository.findOneBySeq(seq);

		if (!boardFree.isPresent())
			throw new ServiceException(ServiceError.NOT_FOUND_POST);

		BoardFree getBoardFree = boardFree.get();
		BoardStatus status = getBoardFree.getStatus();

		if (Objects.isNull(status))
			status = new BoardStatus();

		Boolean isNotice = status.getNotice();

		if (Objects.nonNull(isNotice)) {
			if (isEnable && isNotice)
				throw new ServiceException(ServiceError.ALREADY_ENABLE);

			if (! isEnable && ! isNotice)
				throw new ServiceException(ServiceError.ALREADY_DISABLE);
		}

		if (isEnable) {
			status.setNotice(true);
		} else {
			status.setNotice(null);
		}

		getBoardFree.setStatus(status);

		List<BoardHistory> histories = getBoardFree.getHistory();

		if (Objects.isNull(histories))
			histories = new ArrayList<>();

		BoardHistory history = new BoardHistory(new ObjectId().toString(),
				isEnable ? CoreConst.BOARD_HISTORY_TYPE.ENABLE_NOTICE : CoreConst.BOARD_HISTORY_TYPE.DISABLE_NOTICE, writer);
		histories.add(history);

		getBoardFree.setHistory(histories);

		boardFreeRepository.save(getBoardFree);

		if (log.isInfoEnabled())
			log.info("Set notice. post seq=" + getBoardFree.getSeq() + ", type=" + status.getNotice());
	}


	/**
	 * 자유게시판 주간 좋아요수 선두
	 * @return 게시물 목록
     */
	public List<BoardFreeOnBest> getFreeTopLikes() {
		LocalDate date = LocalDate.now().minusWeeks(1);
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

		return boardDAO.getBoardFreeCountOfLikeBest(new ObjectId(Date.from(instant)));
	}

	/**
	 * 자유게시판 주간 댓글수 선두
	 * @return 게시물 목록
	 */
	public List<BoardFreeOnBest> getFreeTopComments() {
		LocalDate date = LocalDate.now().minusWeeks(1);
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

		HashMap<String, Integer> boardFreeCommentCount = boardDAO.getBoardFreeCountOfCommentBest(new ObjectId(Date.from(instant)));

		ArrayList<ObjectId> commentIds = new ArrayList<>();

		Iterator<?> commentIterator = boardFreeCommentCount.entrySet().iterator();

		// 댓글 많은 글 id 뽑아내기
		while (commentIterator.hasNext()) {
			Entry<String, Integer> entry = (Entry<String, Integer>) commentIterator.next();
			ObjectId objId = new ObjectId(entry.getKey());
			commentIds.add(objId);
		}

		// commentIds를 파라미터로 다시 글을 가져온다.
		List<BoardFreeOnBest> posts = boardDAO.getBoardFreeListOfTop(commentIds);

		for (BoardFreeOnBest boardFree : posts) {
			String id = boardFree.getId();
			Integer count = boardFreeCommentCount.get(id);
			boardFree.setCount(count);
		}

		// sort and limit
		Comparator<BoardFreeOnBest> byCount = (b1, b2) -> b2.getCount() - b1.getCount();
		Comparator<BoardFreeOnBest> byView = (b1, b2) -> b2.getViews() - b1.getViews();

		posts = posts.stream()
				.sorted(byCount.thenComparing(byView))
				.limit(CoreConst.BOARD_TOP_LIMIT)
				.collect(Collectors.toList());

		return posts;
	}

	/**
	 * 자유게시판 댓글 목록
     */
	public Page<BoardFreeComment> getBoardFreeComments(int page, int size) {

		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));
		Pageable pageable = new PageRequest(page - 1, size, sort);

		return boardFreeCommentRepository.findAll(pageable);
	}

	/**
	 * id 배열에 해당하는 BoardFree 목록.
	 * @param ids id 배열
	 */
	public Map<String, BoardFreeOnSearch> getBoardFreeOnSearchByIds(List<ObjectId> ids) {
		List<BoardFreeOnSearch> posts = boardFreeRepository.findPostsOnSearchByIds(ids);

		return posts.stream()
				.collect(Collectors.toMap(BoardFreeOnSearch::getId, Function.identity()));
	}

	/**
	 * RSS 용 게시물 목록
	 */
	public List<BoardFreeOnRSS> getBoardFreeOnRss(ObjectId objectId, Integer limit) {
		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));

		return boardFreeRepository.findPostsOnRss(objectId, sort, limit);

	}

	/**
	 * 사이트맵 용 게시물 목록
	 */
	public List<BoardFreeOnSitemap> getBoardFreeOnSitemap(ObjectId objectId, Integer limit) {
		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));

		return boardFreeRepository.findPostsOnSitemap(objectId, sort, limit);

	}

	/**
	 * 글 상세 객체 가져오기
	 */
	public FreePostDetailResponse getBoardFreeDetail(Integer seq, Boolean isAddCookie) {

		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_POST));

		if (isAddCookie)
			this.increaseViews(boardFree);

		CommonWriter commonWriter = UserUtils.getCommonWriter();

        // 글 상세
		FreePostDetail freePostDetail = new FreePostDetail();
		BeanUtils.copyProperties(boardFree, freePostDetail);

		Integer numberOfLike = ObjectUtils.isEmpty(boardFree.getUsersLiking()) ? 0 : boardFree.getUsersLiking().size();
		Integer numberOfDisLike = ObjectUtils.isEmpty(boardFree.getUsersDisliking()) ? 0 : boardFree.getUsersDisliking().size();

		BoardCategory boardCategory = boardCategoryRepository.findByCodeAndLanguage(boardFree.getCategory().name(),
				CoreUtils.getLanguageCode(LocaleContextHolder.getLocale(), null));

		freePostDetail.setCategory(boardCategory);
		freePostDetail.setNumberOfLike(numberOfLike);
		freePostDetail.setNumberOfDislike(numberOfDisLike);

		List<Gallery> galleries = galleryRepository.findByItemIdAndFromType(
				new ObjectId(boardFree.getId()), CoreConst.GALLERY_FROM_TYPE.BOARD_FREE, 100);

		if (! ObjectUtils.isEmpty(galleries)) {
			List<FreePostDetailGallery> postDetailGalleries = galleries.stream()
					.map(gallery -> FreePostDetailGallery.builder()
							.id(gallery.getId())
							.name(gallery.getName())
							.imageUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, gallery.getId()))
							.thumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, gallery.getId()))
							.build())
					.collect(Collectors.toList());

			freePostDetail.setGalleries(postDetailGalleries);
		}

		if (Objects.nonNull(commonWriter))
			freePostDetail.setMyFeeling(ApiUtils.getMyFeeling(commonWriter, boardFree.getUsersLiking(), boardFree.getUsersDisliking()));

		// 앞, 뒤 글
		CoreConst.BOARD_CATEGORY_TYPE categoryType = CoreConst.BOARD_CATEGORY_TYPE.valueOf(freePostDetail.getCategory().getCode());

		BoardFreeSimple prevPost = boardFreeRepository.findByIdAndCategoryWithOperator(new ObjectId(freePostDetail.getId())
				, categoryType, CoreConst.CRITERIA_OPERATOR.GT);
		BoardFreeSimple nextPost = boardFreeRepository.findByIdAndCategoryWithOperator(new ObjectId(freePostDetail.getId())
				, categoryType, CoreConst.CRITERIA_OPERATOR.LT);

        // 글쓴이의 최근 글
		List<LatestFreePost> latestFreePosts = null;

		if (ObjectUtils.isEmpty(freePostDetail.getStatus()) || BooleanUtils.isNotTrue(freePostDetail.getStatus().getDelete())) {

			List<BoardFreeOnList> latestPostsByWriter = boardFreeRepository.findByIdAndUserId(
					new ObjectId(freePostDetail.getId()), freePostDetail.getWriter().getUserId(), 3);

			// 게시물 VO 변환 및 썸네일 URL 추가
			latestFreePosts = latestPostsByWriter.stream()
					.map(post -> {
						LatestFreePost latestFreePost = new LatestFreePost();
						BeanUtils.copyProperties(post, latestFreePost);

						if (! post.isLinkedGallery()) {
							List<Gallery> latestPostGalleries = galleryRepository.findByItemIdAndFromType(new ObjectId(post.getId()),
									CoreConst.GALLERY_FROM_TYPE.BOARD_FREE, 1);

							if (! ObjectUtils.isEmpty(latestPostGalleries)) {
								List<BoardGallery> boardGalleries = latestPostGalleries.stream()
										.map(gallery -> BoardGallery.builder()
												.id(gallery.getId())
												.thumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, gallery.getId()))
												.build())
										.collect(Collectors.toList());

								latestFreePost.setGalleries(boardGalleries);
							}
						}

						return latestFreePost;
					})
					.collect(Collectors.toList());
		}

		return FreePostDetailResponse.builder()
				.post(freePostDetail)
				.prevPost(prevPost)
				.nextPost(nextPost)
				.latestPostsByWriter(ObjectUtils.isEmpty(latestFreePosts) ? null : latestFreePosts)
				.build();
	}

	/**
	 * 읽음수 1 증가
	 */
	private void increaseViews(BoardFree boardFree) {
		int views = boardFree.getViews();
		boardFree.setViews(++views);
		boardFreeRepository.save(boardFree);
	}

}
