package com.jakduk.api.service;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.UrlGenerationUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.aggregate.BoardFeelingCount;
import com.jakduk.api.model.aggregate.BoardTop;
import com.jakduk.api.model.aggregate.CommonCount;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.db.UsersFeeling;
import com.jakduk.api.model.embedded.*;
import com.jakduk.api.model.simple.*;
import com.jakduk.api.repository.article.ArticleOnListRepository;
import com.jakduk.api.repository.article.ArticleRepository;
import com.jakduk.api.repository.article.comment.ArticleCommentRepository;
import com.jakduk.api.repository.gallery.GalleryRepository;
import com.jakduk.api.restcontroller.vo.board.*;
import com.jakduk.api.restcontroller.vo.home.HomeArticle;
import com.jakduk.api.restcontroller.vo.home.HomeArticleComment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleService {

	@Autowired private UrlGenerationUtils urlGenerationUtils;
	@Autowired private ArticleRepository articleRepository;
	@Autowired private ArticleOnListRepository articleOnListRepository;
	@Autowired private ArticleCommentRepository articleCommentRepository;
	@Autowired private GalleryRepository galleryRepository;
	@Autowired private CommonService commonService;
	@Autowired private CommonGalleryService commonGalleryService;
	@Autowired private RabbitMQPublisher rabbitMQPublisher;

	public Article findOneBySeq(Constants.BOARD_TYPE board, Integer seq) {
        return articleRepository.findOneByBoardAndSeq(board.name(), seq)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ARTICLE));
    }

    /**
     * 자유게시판 글쓰기
	 *
     * @param subject 글 제목
     * @param content 글 내용
     * @param categoryCode 글 말머리 Code
     * @param linkedGallery 사진 연동 여부
     * @param device 디바이스
     */
	public Article insertArticle(CommonWriter writer, Constants.BOARD_TYPE board, String subject, String content, String categoryCode,
								 Boolean linkedGallery, Constants.DEVICE_TYPE device) {

		if (BoardCategoryGenerator.notExistCategory(board, categoryCode))
			throw new ServiceException(ServiceError.NOT_FOUND_CATEGORY);

		// shortContent 만듦
		String stripHtmlContent = StringUtils.defaultIfBlank(JakdukUtils.stripHtmlTag(content), StringUtils.EMPTY);
		String shortContent = StringUtils.truncate(stripHtmlContent, Constants.ARTICLE_SHORT_CONTENT_LENGTH);

		// 글 상태
		ArticleStatus articleStatus = new ArticleStatus();
		articleStatus.setDevice(device);

		ObjectId objectId = new ObjectId();

		Article article = Article.builder()
				.writer(writer)
				.board(board.name())
				.category(Constants.BOARD_TYPE.FREE.equals(board) ? null : categoryCode)
				.subject(subject)
				.content(content)
				.shortContent(shortContent)
				.views(0)
				.seq(commonService.getNextSequence(Constants.SEQ_BOARD))
				.status(articleStatus)
				.logs(this.initBoardLogs(objectId, Constants.ARTICLE_LOG_TYPE.CREATE.name(), writer))
				.lastUpdated(LocalDateTime.ofInstant(objectId.getDate().toInstant(), ZoneId.systemDefault()))
				.linkedGallery(linkedGallery)
				.build();

		articleRepository.save(article);

		log.info("new post created. post seq={}, subject={}", article.getSeq(), article.getSubject());

		return article;
	}

	/**
	 * 자유게시판 글 고치기
	 *
	 * @param board 게시판
	 * @param seq 글 seq
	 * @param subject 글 제목
	 * @param content 글 내용
	 * @param categoryCode 글 말머리 Code
	 * @param linkedGallery 사진 연동 여부
	 * @param device 디바이스
	 */
	public Article updateArticle(CommonWriter writer, Constants.BOARD_TYPE board, Integer seq, String subject, String content, String categoryCode,
								 Boolean linkedGallery, Constants.DEVICE_TYPE device) {

		Article article = articleRepository.findOneByBoardAndSeq(board.name(), seq)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ARTICLE));

		if (! article.getWriter().getUserId().equals(writer.getUserId()))
			throw new ServiceException(ServiceError.FORBIDDEN);

		if (BoardCategoryGenerator.notExistCategory(board, categoryCode))
			throw new ServiceException(ServiceError.NOT_FOUND_CATEGORY);

		// shortContent 만듦
		String stripHtmlContent = StringUtils.defaultIfBlank(JakdukUtils.stripHtmlTag(content), StringUtils.EMPTY);
		String shortContent = StringUtils.truncate(stripHtmlContent, Constants.ARTICLE_SHORT_CONTENT_LENGTH);

		article.setSubject(subject);
		article.setContent(content);
		article.setCategory(Constants.BOARD_TYPE.FREE.equals(board) ? null : categoryCode);
		article.setShortContent(shortContent);
		article.setLinkedGallery(linkedGallery);

		// 글 상태
		ArticleStatus articleStatus = article.getStatus();

		if (Objects.isNull(articleStatus))
			articleStatus = new ArticleStatus();

        articleStatus.setDevice(device);
		article.setStatus(articleStatus);

		// boardHistory
		List<BoardLog> logs = article.getLogs();

		if (CollectionUtils.isEmpty(logs))
			logs = new ArrayList<>();

		ObjectId logId = new ObjectId();
		logs.add(new BoardLog(logId.toString(), Constants.ARTICLE_LOG_TYPE.EDIT.name(), new SimpleWriter(writer)));
		article.setLogs(logs);

		// lastUpdated
		article.setLastUpdated(LocalDateTime.ofInstant(logId.getDate().toInstant(), ZoneId.systemDefault()));

		articleRepository.save(article);

		log.info("post was edited. post seq={}, subject={}", article.getSeq(), article.getSubject());

		return article;
	}

	/**
	 * 자유게시판 글 지움
	 *
	 * @param board 게시판
	 * @param seq 글 seq
	 * @return Constants.ARTICLE_DELETE_TYPE
     */
    public Constants.ARTICLE_DELETE_TYPE deleteArticle(CommonWriter writer, Constants.BOARD_TYPE board, Integer seq) {

        Article article = articleRepository.findOneByBoardAndSeq(board.name(), seq)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ARTICLE));

        if (! article.getWriter().getUserId().equals(writer.getUserId()))
            throw new ServiceException(ServiceError.FORBIDDEN);

        ArticleItem articleItem = new ArticleItem(article.getId(), article.getSeq(), article.getBoard());

        Integer count = articleCommentRepository.countByArticle(articleItem);

        // 댓글이 하나라도 달리면 글을 몽땅 지우지 못한다.
        if (count > 0) {
			article.setContent(null);
			article.setSubject(null);
			article.setWriter(null);

            List<BoardLog> histories = article.getLogs();

            if (Objects.isNull(histories))
                histories = new ArrayList<>();

			ObjectId boardHistoryId = new ObjectId();
            BoardLog history = new BoardLog(boardHistoryId.toString(), Constants.ARTICLE_LOG_TYPE.DELETE.name(), new SimpleWriter(writer));
            histories.add(history);
			article.setLogs(histories);

            ArticleStatus articleStatus = article.getStatus();

            if (Objects.isNull(articleStatus))
                articleStatus = new ArticleStatus();

            articleStatus.setDelete(true);
			article.setStatus(articleStatus);
			article.setLinkedGallery(false);

			// lastUpdated
			article.setLastUpdated(LocalDateTime.ofInstant(boardHistoryId.getDate().toInstant(), ZoneId.systemDefault()));

			articleRepository.save(article);

			log.info("A post was deleted(post only). post seq={}, subject={}", article.getSeq(), article.getSubject());
        }
		// 몽땅 지우기
        else {
            articleRepository.delete(article);

			log.info("A post was deleted(all). post seq={}, subject={}", article.getSeq(), article.getSubject());
        }

        // 연결된 사진 끊기
        if (article.getLinkedGallery())
			commonGalleryService.unlinkGalleries(article.getId(), Constants.GALLERY_FROM_TYPE.ARTICLE);

		// 색인 지움
		rabbitMQPublisher.deleteDocumentArticle(article.getId());

        return count > 0 ? Constants.ARTICLE_DELETE_TYPE.CONTENT : Constants.ARTICLE_DELETE_TYPE.ALL;
    }

	/**
	 * 자유게시판 글 목록
     */
	public GetArticlesResponse getArticles(Constants.BOARD_TYPE board, String categoryCode, Integer page, Integer size) {

		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));
		Pageable pageable = new PageRequest(page - 1, size, sort);
		Page<ArticleOnList> articlePages;

		if ("ALL".equals(categoryCode)) {
			articlePages = articleOnListRepository.findByBoard(board, pageable);
		} else {
			articlePages = articleOnListRepository.findByBoardAndCategory(board, categoryCode, pageable);
		}

		// 자유 게시판 공지글 목록
		List<ArticleOnList> notices = articleRepository.findNotices(sort);

		// 게시물 VO 변환 및 썸네일 URL 추가
		Function<ArticleOnList, GetArticle> convertToGetArticle = article -> {
			GetArticle getArticle = new GetArticle();
			BeanUtils.copyProperties(article, getArticle);

			if (article.getLinkedGallery()) {
				List<Gallery> galleries = galleryRepository.findByItemIdAndFromType(
						new ObjectId(article.getId()), Constants.GALLERY_FROM_TYPE.ARTICLE, 1);

				if (! CollectionUtils.isEmpty(galleries)) {
					List<BoardGallerySimple> boardGalleries = galleries.stream()
							.map(gallery -> BoardGallerySimple.builder()
									.id(gallery.getId())
									.thumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, gallery.getId()))
									.build())
							.collect(Collectors.toList());

					getArticle.setGalleries(boardGalleries);
				}
			}

			return getArticle;
		};

		List<GetArticle> getArticles = articlePages.getContent().stream()
				.map(convertToGetArticle)
				.collect(Collectors.toList());

		List<GetArticle> getNotices = notices.stream()
				.map(convertToGetArticle)
				.collect(Collectors.toList());

		// Board ID 뽑아내기.
		ArrayList<ObjectId> ids = new ArrayList<>();

		getArticles.forEach(article -> ids.add(new ObjectId(article.getId())));
		getNotices.forEach(article -> ids.add(new ObjectId(article.getId())));

		// 게시물의 댓글수
		Map<String, Integer> commentCounts = articleCommentRepository.findCommentsCountByIds(ids).stream()
				.collect(Collectors.toMap(CommonCount::getId, CommonCount::getCount));

		// 게시물의 감정수
		Map<String, BoardFeelingCount> feelingCounts = articleRepository.findUsersFeelingCount(ids).stream()
				.collect(Collectors.toMap(BoardFeelingCount::getId, Function.identity()));

		// 댓글수, 감정 표현수 합치기.
		Consumer<GetArticle> applyCounts = article -> {
			String boardId = article.getId();
			Integer commentCount = commentCounts.get(boardId);

			if (Objects.nonNull(commentCount))
				article.setCommentCount(commentCount);

			BoardFeelingCount feelingCount = feelingCounts.get(boardId);

			if (Objects.nonNull(feelingCount)) {
				article.setLikingCount(feelingCount.getUsersLikingCount());
				article.setDislikingCount(feelingCount.getUsersDislikingCount());
			}
		};

		getArticles.forEach(applyCounts);
		getNotices.forEach(applyCounts);

		// 말머리
		List<BoardCategory> categories = BoardCategoryGenerator.getCategories(board, JakdukUtils.getLocale());
		Map<String, String> categoriesMap = null;

		if (! CollectionUtils.isEmpty(categories)) {
			categoriesMap = categories.stream()
					.collect(Collectors.toMap(BoardCategory::getCode, boardCategory -> boardCategory.getNames().get(0).getName()));

			categoriesMap.put("ALL", JakdukUtils.getMessageSource("board.category.all"));
		}

		return GetArticlesResponse.builder()
				.categories(categoriesMap)
				.articles(getArticles)
				.notices(getNotices)
				.first(articlePages.isFirst())
				.last(articlePages.isLast())
				.totalPages(articlePages.getTotalPages())
				.totalElements(articlePages.getTotalElements())
				.numberOfElements(articlePages.getNumberOfElements())
				.size(articlePages.getSize())
				.number(articlePages.getNumber())
				.build();
	}

	/**
	 * 최근 글 가져오기
	 */
	public List<HomeArticle> getLatestArticles() {

		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));

		List<ArticleOnList> articles = articleRepository.findLatest(sort, Constants.HOME_SIZE_POST);

		// 게시물 VO 변환 및 썸네일 URL 추가
		return articles.stream()
				.map(post -> {
					HomeArticle homeArticle = new HomeArticle();
					BeanUtils.copyProperties(post, homeArticle);

					if (post.getLinkedGallery()) {
						List<Gallery> galleries = galleryRepository.findByItemIdAndFromType(
								new ObjectId(post.getId()), Constants.GALLERY_FROM_TYPE.ARTICLE, 1);

						List<BoardGallerySimple> boardGalleries = galleries.stream()
								.sorted(Comparator.comparing(Gallery::getId))
								.limit(1)
								.map(gallery -> BoardGallerySimple.builder()
										.id(gallery.getId())
										.thumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, gallery.getId()))
										.build())
								.collect(Collectors.toList());

						homeArticle.setGalleries(boardGalleries);
					}

					return homeArticle;
				})
				.collect(Collectors.toList());
	}

	// 최근 댓글 가져오기.
	public List<HomeArticleComment> getLatestComments() {

		List<ArticleCommentSimple> comments = articleCommentRepository.findSimpleComments();

		// article id 뽑아내기.
		List<ObjectId> articleIds = comments.stream()
				.map(comment -> new ObjectId(comment.getArticle().getId()))
				.distinct()
				.collect(Collectors.toList());

		// 댓글을 가진 글 목록
		List<ArticleSimple> articles = articleRepository.findArticleSimplesByIds(articleIds);

		Map<String, ArticleSimple> postsHavingComments = articles.stream()
				.collect(Collectors.toMap(ArticleSimple::getId, Function.identity()));

		return comments.stream()
				.map(comment -> {
					HomeArticleComment homeArticleComment = new HomeArticleComment();
					BeanUtils.copyProperties(comment, homeArticleComment);

					homeArticleComment.setArticle(
							Optional.ofNullable(postsHavingComments.get(comment.getArticle().getId()))
									.orElse(new ArticleSimple()));

					return homeArticleComment;
				})
				.peek(comment -> {
					String content = JakdukUtils.stripHtmlTag(comment.getContent());

					if (StringUtils.isNotBlank(content)) {
						Integer contentLength = content.length() + comment.getWriter().getUsername().length();

						if (contentLength > Constants.HOME_COMMENT_CONTENT_MAX_LENGTH) {
							content = content.substring(0, Constants.HOME_COMMENT_CONTENT_MAX_LENGTH - comment.getWriter().getUsername().length());
							content = String.format("%s...", content);
						}
						comment.setContent(content);
					}
				})
				.collect(Collectors.toList());
	}

    /**
     * 글 감정 표현.
     */
	public Article setArticleFeelings(CommonWriter writer, Constants.BOARD_TYPE board, Integer seq, Constants.FEELING_TYPE feeling) {

		Article article = articleRepository.findOneByBoardAndSeq(board.name(), seq)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ARTICLE));

        String userId = writer.getUserId();
        String username = writer.getUsername();

		CommonWriter articleWriter = article.getWriter();

		// 이 게시물의 작성자라서 감정 표현을 할 수 없음
		if (userId.equals(articleWriter.getUserId()))
			throw new ServiceException(ServiceError.FEELING_YOU_ARE_WRITER);

		this.setUsersFeeling(userId, username, feeling, article);

		articleRepository.save(article);

		return article;
	}

	/**
	 * 게시판 댓글 달기
	 */
	public ArticleComment insertArticleComment(CommonWriter writer, Constants.BOARD_TYPE board, Integer seq, String content, List<Gallery> galleries,
											   Constants.DEVICE_TYPE device) {

		Article article = articleRepository.findOneByBoardAndSeq(board.name(), seq)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ARTICLE));

		// 연관된 사진 id 배열 (검증 후)
		List<String> galleryIds = galleries.stream()
				.map(Gallery::getId)
				.collect(Collectors.toList());

		ArticleComment articleComment = ArticleComment.builder()
				.article(new ArticleItem(article.getId(), article.getSeq(), article.getBoard()))
				.writer(writer)
				.content(content)
				.status(new ArticleCommentStatus(device))
				.linkedGallery(! galleries.isEmpty())
				.logs(this.initBoardLogs(new ObjectId(), Constants.ARTICLE_COMMENT_LOG_TYPE.CREATE.name(), writer))
				.build();

		articleCommentRepository.save(articleComment);

		// 엘라스틱서치 색인 요청
		rabbitMQPublisher.indexDocumentComment(articleComment.getId(), articleComment.getArticle(), articleComment.getWriter(),
				articleComment.getContent(), galleryIds);

		return articleComment;
	}

	/**
	 * 게시판 댓글 고치기
	 */
	public ArticleComment updateArticleComment(CommonWriter writer, Constants.BOARD_TYPE board, String id, String content, List<String> galleryIds,
											   Constants.DEVICE_TYPE device) {

		ArticleComment articleComment = articleCommentRepository.findOneById(id)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_COMMENT));

		if (! articleComment.getWriter().getUserId().equals(writer.getUserId()))
			throw new ServiceException(ServiceError.FORBIDDEN);

		articleComment.setWriter(writer);
		articleComment.setContent(StringUtils.trim(content));
		ArticleCommentStatus articleCommentStatus = articleComment.getStatus();

		if (Objects.isNull(articleCommentStatus)) {
			articleCommentStatus = new ArticleCommentStatus(device);
		} else {
			articleCommentStatus.setDevice(device);
		}

		articleComment.setStatus(articleCommentStatus);
		articleComment.setLinkedGallery(! galleryIds.isEmpty());

		// boardLogs
		List<BoardLog> logs = Optional.ofNullable(articleComment.getLogs())
				.orElseGet(ArrayList::new);

		logs.add(new BoardLog(new ObjectId().toString(), Constants.ARTICLE_COMMENT_LOG_TYPE.EDIT.name(), new SimpleWriter(writer)));
		articleComment.setLogs(logs);

		articleCommentRepository.save(articleComment);

		// 엘라스틱서치 색인 요청
		rabbitMQPublisher.indexDocumentComment(articleComment.getId(), articleComment.getArticle(), articleComment.getWriter(),
				articleComment.getContent(), galleryIds);

		return articleComment;
	}

	/**
	 * 게시판 댓글 지움
	 */
	public void deleteArticleComment(CommonWriter writer, Constants.BOARD_TYPE board, String id) {

		ArticleComment articleComment = articleCommentRepository.findOneById(id)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_COMMENT));

		if (! articleComment.getWriter().getUserId().equals(writer.getUserId()))
			throw new ServiceException(ServiceError.FORBIDDEN);

		articleCommentRepository.delete(id);

		// 색인 지움
		rabbitMQPublisher.deleteDocumentComment(id);

		commonGalleryService.unlinkGalleries(id, Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT);
	}

	/**
	 * 게시물 댓글 목록
	 */
	public GetArticleDetailCommentsResponse getArticleDetailComments(CommonWriter commonWriter, Constants.BOARD_TYPE board, Integer seq, String commentId) {

		List<ArticleComment> comments;

		if (StringUtils.isNotBlank(commentId)) {
			comments  = articleCommentRepository.findByBoardSeqAndGTId(board.name(), seq, new ObjectId(commentId));
		} else {
			comments  = articleCommentRepository.findByBoardSeqAndGTId(board.name(), seq, null);
		}

		ArticleSimple articleSimple = articleRepository.findBoardFreeOfMinimumBySeq(seq);
		ArticleItem articleItem = new ArticleItem(articleSimple.getId(), articleSimple.getSeq(), articleSimple.getBoard());

		List<GetArticleComment> articleComments = this.toGetArticleComments(commonWriter, comments);
		Integer count = articleCommentRepository.countByArticle(articleItem);

		return GetArticleDetailCommentsResponse.builder()
				.comments(articleComments)
				.count(count)
				.build();
	}

	/**
	 * 자유게시판 댓글 감정 표현.
	 *
	 * @param commentId 댓글 ID
	 * @param feeling 감정표현 종류
     * @return 자유게시판 댓글 객체
     */
	public ArticleComment setArticleCommentFeeling(CommonWriter writer, String commentId, Constants.FEELING_TYPE feeling) {

		ArticleComment boardComment = articleCommentRepository.findOneById(commentId)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_COMMENT));

		String userId = writer.getUserId();
		String username = writer.getUsername();

		CommonWriter postWriter = boardComment.getWriter();

		// 이 게시물의 작성자라서 감정 표현을 할 수 없음
		if (userId.equals(postWriter.getUserId()))
			throw new ServiceException(ServiceError.FEELING_YOU_ARE_WRITER);

		this.setUsersFeeling(userId, username, feeling, boardComment);

		articleCommentRepository.save(boardComment);

		return boardComment;
	}

	public void enableArticleNotice(CommonWriter writer, Constants.BOARD_TYPE board, Integer seq) {
		this.setArticleNotice(writer, board, seq, true);
	}

	public void disableArticleNotice(CommonWriter writer, Constants.BOARD_TYPE board, Integer seq) {
		this.setArticleNotice(writer, board, seq, false);
	}

	/**
	 * 자유게시판 글의 공지를 활성화/비활성화 한다.
	 * @param seq 글 seq
	 * @param isEnable 활성화/비활성화
     */
	private void setArticleNotice(CommonWriter writer, Constants.BOARD_TYPE board, Integer seq, Boolean isEnable) {

		Article article = articleRepository.findOneByBoardAndSeq(board.name(), seq)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ARTICLE));

		ArticleStatus status = article.getStatus();

		if (Objects.isNull(status))
			status = new ArticleStatus();

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

		article.setStatus(status);

		List<BoardLog> histories = article.getLogs();

		if (CollectionUtils.isEmpty(histories))
			histories = new ArrayList<>();

		String historyType = isEnable ? Constants.ARTICLE_LOG_TYPE.ENABLE_NOTICE.name() : Constants.ARTICLE_LOG_TYPE.DISABLE_NOTICE.name();
		histories.add(new BoardLog(new ObjectId().toString(), historyType, new SimpleWriter(writer)));

		article.setLogs(histories);

		articleRepository.save(article);

		if (log.isInfoEnabled())
			log.info("Set notice for article. seq={}, type={}", article.getSeq(), status.getNotice());
	}


	/**
	 * 자유게시판 주간 좋아요수 선두
     */
	public List<BoardTop> getArticlesTopLikes(Constants.BOARD_TYPE board, ObjectId objectId) {
		return articleRepository.findTopLikes(board, objectId);
	}

	/**
	 * 자유게시판 주간 댓글수 선두
	 */
	public List<BoardTop> getArticlesTopComments(Constants.BOARD_TYPE board, ObjectId objectId) {

		// 게시물의 댓글수
		Map<String, Integer> commentCounts = articleCommentRepository.findCommentsCountGreaterThanBoardIdAndBoard(objectId, board).stream()
				.collect(Collectors.toMap(CommonCount::getId, CommonCount::getCount));

		List<String> postIds = commentCounts.entrySet().stream()
				.map(Entry::getKey)
				.collect(Collectors.toList());

		// commentIds를 파라미터로 다시 글을 가져온다.
		List<Article> posts = articleRepository.findByIdInAndBoard(postIds, board.name());

		// sort
		Comparator<BoardTop> byCount = (b1, b2) -> b2.getCount() - b1.getCount();
		Comparator<BoardTop> byView = (b1, b2) -> b2.getViews() - b1.getViews();

		return posts.stream()
				.map(boardFree -> {
					BoardTop boardTop = new BoardTop();
					BeanUtils.copyProperties(boardFree, boardTop);
					boardTop.setCount(commentCounts.get(boardTop.getId()));
					return boardTop;
				})
				.sorted(byCount.thenComparing(byView))
				.limit(Constants.BOARD_TOP_LIMIT)
				.collect(Collectors.toList());
	}

	/**
	 * 자유게시판 댓글 목록
     */
	public GetArticleCommentsResponse getArticleComments(CommonWriter commonWriter, Constants.BOARD_TYPE board, Integer page, Integer size) {

		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));
		Pageable pageable = new PageRequest(page - 1, size, sort);

		Page<ArticleComment> commentsPage = articleCommentRepository.findByArticleBoard(board.name(), pageable);

		List<GetArticleComment> getArticleComments = this.toGetArticleComments(commonWriter, commentsPage.getContent());

		return GetArticleCommentsResponse.builder()
				.comments(getArticleComments)
				.first(commentsPage.isFirst())
				.last(commentsPage.isLast())
				.totalPages(commentsPage.getTotalPages())
				.totalElements(commentsPage.getTotalElements())
				.numberOfElements(commentsPage.getNumberOfElements())
				.size(commentsPage.getSize())
				.number(commentsPage.getNumber())
				.build();
	}

	/**
	 * RSS 용 게시물 목록
	 */
	public List<ArticleOnRSS> getBoardFreeOnRss(ObjectId objectId, Integer limit) {
		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));

		return articleRepository.findPostsOnRss(objectId, sort, limit);

	}

	/**
	 * 사이트맵 용 게시물 목록
	 */
	public List<ArticleOnSitemap> getBoardFreeOnSitemap(ObjectId objectId, Integer limit) {
		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));

		return articleRepository.findSitemapArticles(objectId, sort, limit);

	}

	/**
	 * 글 상세 객체 가져오기
	 */
	public ResponseEntity<GetArticleDetailResponse> getArticleDetail(CommonWriter commonWriter, Constants.BOARD_TYPE board, Integer seq, Boolean isAddCookie) {

		Article article = articleRepository.findOneBySeq(seq)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ARTICLE));

		if (! StringUtils.equals(article.getBoard(), board.name())) {
			return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
					.header(HttpHeaders.LOCATION, urlGenerationUtils.generateArticleDetailApiUrl(article.getBoard(), seq))
					.build();
		}

		if (isAddCookie)
			this.increaseViews(article);

        // 글 상세
		ArticleDetail articleDetail = new ArticleDetail();
		BeanUtils.copyProperties(article, articleDetail);

		if (! CollectionUtils.isEmpty(article.getLogs())) {
			List<ArticleLog> logs = article.getLogs().stream()
					.map(boardLog -> {
						ArticleLog articleLog = new ArticleLog();
						BeanUtils.copyProperties(boardLog, articleLog);
						LocalDateTime timestamp = DateUtils.dateToLocalDateTime(new ObjectId(articleLog.getId()).getDate());
						articleLog.setType(Constants.ARTICLE_LOG_TYPE.valueOf(boardLog.getType()));
						articleLog.setTimestamp(timestamp);

						return articleLog;
					})
					.sorted(Comparator.comparing(ArticleLog::getId).reversed())
					.collect(Collectors.toList());

			articleDetail.setLogs(logs);
		}

		BoardCategory boardCategory = BoardCategoryGenerator.getCategory(board, article.getCategory(), JakdukUtils.getLocale());

		articleDetail.setBoard(board.name());
		articleDetail.setCategory(boardCategory);
		articleDetail.setNumberOfLike(CollectionUtils.isEmpty(article.getUsersLiking()) ? 0 : article.getUsersLiking().size());
		articleDetail.setNumberOfDislike(CollectionUtils.isEmpty(article.getUsersDisliking()) ? 0 : article.getUsersDisliking().size());

		// 엮인 사진들
		if (article.getLinkedGallery()) {
            List<Gallery> galleries = galleryRepository.findByItemIdAndFromType(
                    new ObjectId(article.getId()), Constants.GALLERY_FROM_TYPE.ARTICLE, 100);

            if (! CollectionUtils.isEmpty(galleries)) {
                List<ArticleGallery> postDetailGalleries = galleries.stream()
                        .map(gallery -> ArticleGallery.builder()
                                .id(gallery.getId())
                                .name(StringUtils.isNoneBlank(gallery.getName()) ? gallery.getName() : gallery.getFileName())
                                .imageUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.LARGE, gallery.getId()))
                                .thumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, gallery.getId()))
                                .build())
                        .collect(Collectors.toList());

                articleDetail.setGalleries(postDetailGalleries);
            }
        }

        // 나의 감정 상태
		if (Objects.nonNull(commonWriter))
			articleDetail.setMyFeeling(JakdukUtils.getMyFeeling(commonWriter, article.getUsersLiking(), article.getUsersDisliking()));

		// 앞, 뒤 글
		ArticleSimple prevPost = articleRepository.findByIdAndCategoryWithOperator(new ObjectId(articleDetail.getId()),
				Objects.nonNull(boardCategory) ? boardCategory.getCode() : null, Constants.CRITERIA_OPERATOR.GT);
		ArticleSimple nextPost = articleRepository.findByIdAndCategoryWithOperator(new ObjectId(articleDetail.getId()),
				Objects.nonNull(boardCategory) ? boardCategory.getCode() : null, Constants.CRITERIA_OPERATOR.LT);

        // 글쓴이의 최근 글
		List<LatestArticle> latestArticles = null;

		if (Objects.isNull(articleDetail.getStatus()) || BooleanUtils.isNotTrue(articleDetail.getStatus().getDelete())) {

			List<ArticleOnList> latestPostsByWriter = articleRepository.findByIdAndUserId(
					new ObjectId(articleDetail.getId()), articleDetail.getWriter().getUserId(), 3);

			// 게시물 VO 변환 및 썸네일 URL 추가
			latestArticles = latestPostsByWriter.stream()
					.map(post -> {
						LatestArticle latestArticle = new LatestArticle();
						BeanUtils.copyProperties(post, latestArticle);

						if (! post.getLinkedGallery()) {
							List<Gallery> latestPostGalleries = galleryRepository.findByItemIdAndFromType(new ObjectId(post.getId()),
									Constants.GALLERY_FROM_TYPE.ARTICLE, 1);

							if (! ObjectUtils.isEmpty(latestPostGalleries)) {
								List<BoardGallerySimple> boardGalleries = latestPostGalleries.stream()
										.map(gallery -> BoardGallerySimple.builder()
												.id(gallery.getId())
												.thumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, gallery.getId()))
												.build())
										.collect(Collectors.toList());

								latestArticle.setGalleries(boardGalleries);
							}
						}

						return latestArticle;
					})
					.collect(Collectors.toList());
		}

		return ResponseEntity.ok()
				.body(GetArticleDetailResponse.builder()
						.article(articleDetail)
						.prevArticle(prevPost)
						.nextArticle(nextPost)
						.latestArticlesByWriter(CollectionUtils.isEmpty(latestArticles) ? null : latestArticles)
						.build());
	}

	/**
	 * 읽음수 1 증가
	 */
	private void increaseViews(Article article) {
		int views = article.getViews();
		article.setViews(++views);
		articleRepository.save(article);
	}

	/**
	 * BoardLogs 생성
	 */
	private List<BoardLog> initBoardLogs(ObjectId objectId, String type, CommonWriter writer) {
		List<BoardLog> logs = new ArrayList<>();
		BoardLog history = new BoardLog(objectId.toString(), type, new SimpleWriter(writer));
		logs.add(history);

		return logs;
	}

	/**
	 * 감정 표현 CRUD
	 *
	 * @param userId 회원 ID
	 * @param username 회원 별명
	 * @param feeling 입력받은 감정
	 * @param usersFeeling 편집할 객체
	 */
	private void setUsersFeeling(String userId, String username, Constants.FEELING_TYPE feeling, UsersFeeling usersFeeling) {

		List<CommonFeelingUser> usersLiking = usersFeeling.getUsersLiking();
		List<CommonFeelingUser> usersDisliking = usersFeeling.getUsersDisliking();

		if (CollectionUtils.isEmpty(usersLiking)) usersLiking = new ArrayList<>();
		if (CollectionUtils.isEmpty(usersDisliking)) usersDisliking = new ArrayList<>();

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

					usersFeeling.setUsersDisliking(usersDisliking);
				}
				// 아직 감정 표현을 하지 않아 좋아요로 등록
				else {
					usersLiking.add(feelingUser);
				}

				usersFeeling.setUsersLiking(usersLiking);

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

					usersFeeling.setUsersLiking(usersLiking);
				}
				// 아직 감정 표현을 하지 않아 싫어요로 등록
				else {
					usersDisliking.add(feelingUser);
				}

				usersFeeling.setUsersDisliking(usersDisliking);

				break;
		}
	}

	/**
	 * 게시물 댓글에서 연관된 그림 목록을 가져온다.
	 *
	 * @param articleCommentId 댓글 ID
	 */
	private List<BoardGallerySimple> getArticleCommentGalleries(String articleCommentId) {

		List<Gallery> galleries = galleryRepository.findByItemIdAndFromType(
				new ObjectId(articleCommentId), Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, 100);

		if (! CollectionUtils.isEmpty(galleries)) {
			return galleries.stream()
					.map(gallery -> BoardGallerySimple.builder()
							.id(gallery.getId())
							.thumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, gallery.getId()))
							.build())
					.collect(Collectors.toList());
		}

		return null;
	}

	/**
	 * List<ArticleComment> 를 List<GetArticleComment> 로 변환한다.
	 *
	 * @param commonWriter 글쓴이
	 * @param articleComments ArticleComment 배열
	 */
	private List<GetArticleComment> toGetArticleComments(CommonWriter commonWriter, List<ArticleComment> articleComments) {

		// article id 뽑아내기.
		List<ObjectId> articleIds = articleComments.stream()
				.map(comment -> new ObjectId(comment.getArticle().getId()))
				.distinct()
				.collect(Collectors.toList());

		// 댓글을 가진 글 목록
		List<ArticleSimple> articles = articleRepository.findArticleSimplesByIds(articleIds);

		Map<String, ArticleSimple> postsHavingComments = articles.stream()
				.collect(Collectors.toMap(ArticleSimple::getId, Function.identity()));

		return articleComments.stream()
				.map(boardFreeComment -> {
					GetArticleComment getArticleComment = new GetArticleComment();
					BeanUtils.copyProperties(boardFreeComment, getArticleComment);

					getArticleComment.setArticle(
							Optional.ofNullable(postsHavingComments.get(boardFreeComment.getArticle().getId()))
									.orElse(new ArticleSimple())
					);

					List<CommonFeelingUser> usersLiking = boardFreeComment.getUsersLiking();
					List<CommonFeelingUser> usersDisliking = boardFreeComment.getUsersDisliking();

					getArticleComment.setNumberOfLike(CollectionUtils.isEmpty(usersLiking) ? 0 : usersLiking.size());
					getArticleComment.setNumberOfDislike(CollectionUtils.isEmpty(usersDisliking) ? 0 : usersDisliking.size());

					if (Objects.nonNull(commonWriter))
						getArticleComment.setMyFeeling(JakdukUtils.getMyFeeling(commonWriter, usersLiking, usersDisliking));

					if (! CollectionUtils.isEmpty(boardFreeComment.getLogs())) {
						List<ArticleCommentLog> logs = boardFreeComment.getLogs().stream()
								.map(boardLog -> {
									ArticleCommentLog articleCommentLog = new ArticleCommentLog();
									BeanUtils.copyProperties(boardLog, articleCommentLog);
									LocalDateTime timestamp = DateUtils.dateToLocalDateTime(new ObjectId(articleCommentLog.getId()).getDate());
									articleCommentLog.setType(Constants.ARTICLE_COMMENT_LOG_TYPE.valueOf(boardLog.getType()));
									articleCommentLog.setTimestamp(timestamp);

									return articleCommentLog;
								})
								.sorted(Comparator.comparing(ArticleCommentLog::getId).reversed())
								.collect(Collectors.toList());

						getArticleComment.setLogs(logs);
					}

					// 엮인 사진들
					if (boardFreeComment.getLinkedGallery())
						getArticleComment.setGalleries(this.getArticleCommentGalleries(boardFreeComment.getId()));

					return getArticleComment;
				})
				.collect(Collectors.toList());
	}

}
