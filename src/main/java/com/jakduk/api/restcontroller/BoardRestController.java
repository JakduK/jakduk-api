package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.annotation.SecuredUser;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.aggregate.BoardTop;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.UserFeelingResponse;
import com.jakduk.api.restcontroller.vo.board.*;
import com.jakduk.api.service.ArticleService;
import com.jakduk.api.service.GalleryService;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시판 API
 *
 * @author pyohwan
 * 16. 3. 26 오후 11:05
 */

@RestController
@RequestMapping("/api/board")
public class BoardRestController {

    @Autowired private ArticleService articleService;
    @Autowired private GalleryService galleryService;
    @Autowired private RabbitMQPublisher rabbitMQPublisher;

    // 게시판 글 목록
    @GetMapping("/{board}/articles")
    public GetArticlesResponse getArticles(
            @PathVariable Constants.BOARD_TYPE board, // 게시판
            @RequestParam(required = false, defaultValue = "1") Integer page, // 페이지 번호(1부터 시작)
            @RequestParam(required = false, defaultValue = "20") Integer size, // 페이지 사이즈
            @RequestParam(required = false, defaultValue = "ALL") String categoryCode // 말머리
    ) {

        return articleService.getArticles(board, categoryCode, page, size);
    }

    // 게시판 주간 선두 글
    @GetMapping("/{board}/tops")
    public GetArticlesTopsResponse getArticlesTops(
            @PathVariable Constants.BOARD_TYPE board // 게시판
    ) {

        LocalDate localDate = LocalDate.now().minusWeeks(1);
        ObjectId objectId = new ObjectId(DateUtils.localDateToDate(localDate));

        List<BoardTop> topLikes = articleService.getArticlesTopLikes(board, objectId);
        List<BoardTop> topComments = articleService.getArticlesTopComments(board, objectId);

        return GetArticlesTopsResponse.builder()
                .topLikes(topLikes)
                .topComments(topComments)
                .build();
    }

    // 게시판 글 상세
    @GetMapping("/{board}/{seq}")
    public ResponseEntity<GetArticleDetailResponse> getArticleDetail(
            @PathVariable Constants.BOARD_TYPE board, // 게시판
            @PathVariable Integer seq, // 글 seq
            HttpServletRequest request,
            HttpServletResponse response) {

        Boolean isAddCookie = JakdukUtils.addViewsCookie(request, response, Constants.VIEWS_COOKIE_TYPE.ARTICLE, String.valueOf(seq));

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        return articleService.getArticleDetail(commonWriter, board, seq, isAddCookie);
    }

    // 게시판 말머리 목록
    @GetMapping("/{board}/categories")
    public GetBoardCategoriesResponse getBoardCategories(
            @PathVariable Constants.BOARD_TYPE board // 게시판
    ) {

        List<BoardCategory> categories = BoardCategoryGenerator.getCategories(board, JakdukUtils.getLocale());

        return GetBoardCategoriesResponse.builder()
                .categories(categories)
                .build();
    }

    // 게시판 글쓰기
    @PostMapping("/{board}")
    public WriteArticleResponse writeArticle(
            @PathVariable Constants.BOARD_TYPE board,
            @Valid @RequestBody WriteArticle form) {

        List<GalleryOnBoard> formGalleries = form.getGalleries();
        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        // 연관된 사진 id 배열 (검증 전)
        List<String> unverifiableGalleryIds = null;

        if (! CollectionUtils.isEmpty(formGalleries)) {
            unverifiableGalleryIds = formGalleries.stream()
                    .map(GalleryOnBoard::getId)
                    .distinct()
                    .collect(Collectors.toList());
        }

        List<Gallery> galleries = galleryService.findByIdIn(unverifiableGalleryIds);

        // 연관된 사진 id 배열 (검증 후)
        List<String> galleryIds = galleries.stream()
                .map(Gallery::getId)
                .collect(Collectors.toList());

        Article article = articleService.insertArticle(commonWriter, board, form.getSubject().trim(), form.getContent().trim(),
                StringUtils.trim(form.getCategoryCode()), ! galleries.isEmpty());

        galleryService.processLinkedGalleries(commonWriter.getUserId(), galleries, formGalleries, null,
                Constants.GALLERY_FROM_TYPE.ARTICLE, article.getId());

        // 엘라스틱서치 색인 요청
        rabbitMQPublisher.indexDocumentArticle(article.getId(), article.getSeq(), article.getBoard(), article.getCategory(),
                article.getWriter(), article.getSubject(), article.getContent(), galleryIds);

        return WriteArticleResponse.builder()
                .board(article.getBoard())
                .seq(article.getSeq())
                .build();
    }

    // 게시판 글 고치기
    @SecuredUser
    @PutMapping("/{board}/{seq}")
    public WriteArticleResponse editArticle(
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable Integer seq,
            @Valid @RequestBody WriteArticle form,
            HttpServletRequest request) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        // 연관된 사진 id 배열 (검증 전)
        List<String> unverifiableGalleryIds = null;

        if (! CollectionUtils.isEmpty(form.getGalleries())) {
            unverifiableGalleryIds = form.getGalleries().stream()
                    .map(GalleryOnBoard::getId)
                    .distinct()
                    .collect(Collectors.toList());
        }

        List<Gallery> galleries = galleryService.findByIdIn(unverifiableGalleryIds);

        // 연관된 사진 id 배열 (검증 후)
        List<String> galleryIds = galleries.stream()
                .map(Gallery::getId)
                .collect(Collectors.toList());

        Article article = articleService.updateArticle(commonWriter, board, seq, form.getSubject().trim(), form.getContent().trim(),
                form.getCategoryCode(), ! galleries.isEmpty());

        List<String> galleryIdsForRemoval = JakdukUtils.getSessionOfGalleryIdsForRemoval(request, Constants.GALLERY_FROM_TYPE.ARTICLE,
                article.getId());

        galleryService.processLinkedGalleries(commonWriter.getUserId(), galleries, form.getGalleries(), galleryIdsForRemoval,
                Constants.GALLERY_FROM_TYPE.ARTICLE, article.getId());

        JakdukUtils.removeSessionOfGalleryIdsForRemoval(request, Constants.GALLERY_FROM_TYPE.ARTICLE, article.getId());

        // 엘라스틱서치 색인 요청
        rabbitMQPublisher.indexDocumentArticle(article.getId(), article.getSeq(), article.getBoard(), article.getCategory(),
                article.getWriter(), article.getSubject(), article.getContent(), galleryIds);

        return WriteArticleResponse.builder()
                .seq(article.getSeq())
                .build();
    }

    // 게시판 글 지움
    @SecuredUser
    @DeleteMapping("/{board}/{seq}")
    public DeleteArticleResponse deleteArticle(
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable Integer seq) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

		Constants.ARTICLE_DELETE_TYPE deleteType = articleService.deleteArticle(commonWriter, board, seq);

        return DeleteArticleResponse.builder()
                .result(deleteType)
                .build();
    }

    // 게시판 글의 댓글 목록
    @GetMapping("/{board}/{seq}/comments")
    public GetArticleDetailCommentsResponse getArticleDetailComments(
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable Integer seq,
            @RequestParam(required = false) String commentId // 이 CommentId 이후부터 목록 가져옴
    ) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        return articleService.getArticleDetailComments(commonWriter, board, seq, commentId);
    }

    // 게시판 글의 댓글 달기
    @PostMapping("/{board}/{seq}/comment")
    public ArticleComment writeArticleComment(
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable Integer seq,
            @Valid @RequestBody WriteArticleComment form) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        // 연관된 사진 id 배열 (검증 전)
        List<String> unverifiableGalleryIds = null;

        if (! CollectionUtils.isEmpty(form.getGalleries())) {
            unverifiableGalleryIds = form.getGalleries().stream()
                    .map(GalleryOnBoard::getId)
                    .collect(Collectors.toList());
        }

        List<Gallery> galleries = galleryService.findByIdIn(unverifiableGalleryIds);

        ArticleComment articleComment =  articleService.insertArticleComment(commonWriter, board, seq, form.getContent().trim(),
                galleries);

        galleryService.processLinkedGalleries(commonWriter.getUserId(), galleries, form.getGalleries(), null,
                Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, articleComment.getId());

        return articleComment;
    }

    // 게시판 글의 댓글 고치기
    @PutMapping("/{board}/comment/{id}")
    public ArticleComment editArticleComment(
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable String id,
            @Valid @RequestBody WriteArticleComment form,
            HttpServletRequest request) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        // 연관된 사진 id 배열 (검증 전)
        List<String> unverifiableGalleryIds = null;

        if (! CollectionUtils.isEmpty(form.getGalleries())) {
            unverifiableGalleryIds = form.getGalleries().stream()
                    .map(GalleryOnBoard::getId)
                    .distinct()
                    .collect(Collectors.toList());
        }

        List<Gallery> galleries = galleryService.findByIdIn(unverifiableGalleryIds);

        // 연관된 사진 id 배열 (검증 후)
        List<String> galleryIds = galleries.stream()
                .map(Gallery::getId)
                .collect(Collectors.toList());

        ArticleComment articleComment = articleService.updateArticleComment(commonWriter, board, id, form.getContent().trim(), galleryIds);

        List<String> galleryIdsForRemoval = JakdukUtils.getSessionOfGalleryIdsForRemoval(request,
                Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, articleComment.getId());

        galleryService.processLinkedGalleries(commonWriter.getUserId(), galleries, form.getGalleries(), galleryIdsForRemoval,
                Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, articleComment.getId());

        JakdukUtils.removeSessionOfGalleryIdsForRemoval(request, Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, articleComment.getId());

        return articleComment;

    }

    // 게시판 글의 댓글 지우기
    @DeleteMapping("/{board}/comment/{id}")
    public EmptyJsonResponse deleteFreeComment(
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable String id // 댓글 ID
    ) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        articleService.deleteArticleComment(commonWriter, board, id);

        return EmptyJsonResponse.newInstance();
    }

    // 게시판 글 감정 표현
    @PostMapping("/{board}/{seq}/{feeling}")
    public UserFeelingResponse setArticleFeeling(
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable Integer seq,
            @PathVariable Constants.FEELING_TYPE feeling) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        Article article = articleService.setArticleFeelings(commonWriter, board, seq, feeling);

        List<CommonFeelingUser> usersLiking = article.getUsersLiking();
        List<CommonFeelingUser> usersDisliking = article.getUsersDisliking();

        return UserFeelingResponse.builder()
                .myFeeling(JakdukUtils.getMyFeeling(commonWriter, usersLiking, usersDisliking))
                .numberOfLike(CollectionUtils.isEmpty(usersLiking) ? 0 : usersLiking.size())
                .numberOfDislike(CollectionUtils.isEmpty(usersDisliking) ? 0 : usersDisliking.size())
                .build();
    }

    // 자유게시판 글의 감정 표현 회원 목록
    @GetMapping("/{board}/{seq}/feeling/users")
    public GetArticleFeelingUsersResponse getArticleFeelingUsers (
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable Integer seq) {

        Article article = articleService.findOneBySeq(board, seq);

        return GetArticleFeelingUsersResponse.builder()
                .seq(seq)
                .usersLiking(article.getUsersLiking())
                .usersDisliking(article.getUsersDisliking())
                .build();
    }

    // 자유게시판 댓글 감정 표현
    @PostMapping("/{board}/comment/{commentId}/{feeling}")
    public UserFeelingResponse addFreeCommentFeeling(
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable String commentId,
            @PathVariable Constants.FEELING_TYPE feeling) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        ArticleComment boardComment = articleService.setArticleCommentFeeling(commonWriter, commentId, feeling);

        List<CommonFeelingUser> usersLiking = boardComment.getUsersLiking();
        List<CommonFeelingUser> usersDisliking = boardComment.getUsersDisliking();

        return UserFeelingResponse.builder()
                .myFeeling(JakdukUtils.getMyFeeling(commonWriter, usersLiking, usersDisliking))
                .numberOfLike(CollectionUtils.isEmpty(usersLiking) ? 0 : usersLiking.size())
                .numberOfDislike(CollectionUtils.isEmpty(usersDisliking) ? 0 : usersDisliking.size())
                .build();
    }

    // 게시판 글의 공지 활성화
    @PostMapping("/{board}/{seq}/notice")
    public EmptyJsonResponse enableArticleNotice(
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable Integer seq) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

		articleService.enableArticleNotice(commonWriter, board, seq);

        return EmptyJsonResponse.newInstance();
    }

    // 게시판 글의 공지 비활성화
    @DeleteMapping("/{board}/{seq}/notice")
    public EmptyJsonResponse disableArticleNotice(
            @PathVariable Constants.BOARD_TYPE board,
            @PathVariable Integer seq) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        articleService.disableArticleNotice(commonWriter, board, seq);

        return EmptyJsonResponse.newInstance();
    }

    @InitBinder
    public void initBoardTypeEnumBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Constants.BOARD_TYPE.class, new BoardTypeEnumConverter());
        dataBinder.registerCustomEditor(Constants.FEELING_TYPE.class, new FeelingTypeEnumConverter());
    }

    private class BoardTypeEnumConverter extends PropertyEditorSupport {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (StringUtils.isAllLowerCase(text)) {
				setValue(text.toUpperCase());
			} else {
				throw new ServiceException(ServiceError.INVALID_PARAMETER);
			}
        }
    }

    private class FeelingTypeEnumConverter extends PropertyEditorSupport {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (StringUtils.isAllLowerCase(text)) {
                setValue(text.toUpperCase());
            } else {
                throw new ServiceException(ServiceError.INVALID_PARAMETER);
            }
        }
    }
}
