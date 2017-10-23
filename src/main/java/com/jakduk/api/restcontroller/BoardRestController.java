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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
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
 * @author pyohwan
 * 16. 3. 26 오후 11:05
 */

@Api(tags = "Board", description = "게시판 API")
@RestController
@RequestMapping("/api/board")
public class BoardRestController {

    @Autowired private ArticleService articleService;
    @Autowired private GalleryService galleryService;
    @Autowired private RabbitMQPublisher rabbitMQPublisher;

    @ApiOperation("게시판 글 목록")
    @GetMapping("/{board}/articles")
    public GetArticlesResponse getArticles(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "페이지 번호(1부터 시작)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "페이지 사이즈") @RequestParam(required = false, defaultValue = "20") Integer size,
            @ApiParam(value = "말머리") @RequestParam(required = false, defaultValue = "ALL") String categoryCode) {

        return articleService.getArticles(board, categoryCode, page, size);
    }

    @ApiOperation("게시판 주간 선두 글")
    @GetMapping("/{board}/tops")
    public GetArticlesTopsResponse getArticlesTops(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board) {

        LocalDate localDate = LocalDate.now().minusWeeks(1);
        ObjectId objectId = new ObjectId(DateUtils.localDateToDate(localDate));

        List<BoardTop> topLikes = articleService.getArticlesTopLikes(board, objectId);
        List<BoardTop> topComments = articleService.getArticlesTopComments(board, objectId);

        return GetArticlesTopsResponse.builder()
                .topLikes(topLikes)
                .topComments(topComments)
                .build();
    }

    @ApiOperation("게시판 글 상세")
    @GetMapping("/{board}/{seq}")
    public ResponseEntity<GetArticleDetailResponse> getArticleDetail(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            HttpServletRequest request,
            HttpServletResponse response) {

        Boolean isAddCookie = JakdukUtils.addViewsCookie(request, response, Constants.VIEWS_COOKIE_TYPE.ARTICLE, String.valueOf(seq));

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        return articleService.getArticleDetail(commonWriter, board, seq, isAddCookie);
    }

    @ApiOperation("게시판 말머리 목록")
    @GetMapping("/{board}/categories")
    public GetBoardCategoriesResponse getBoardCategories(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board) {

        List<BoardCategory> categories = BoardCategoryGenerator.getCategories(board, JakdukUtils.getLocale());

        return GetBoardCategoriesResponse.builder()
                .categories(categories)
                .build();
    }

    @ApiOperation("게시판 글쓰기")
    @PostMapping("/{board}")
    public WriteArticleResponse writeArticle(
            @ApiParam(value = "게시판", required = true, example = "FOOTBALL") @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "글 폼", required = true) @Valid @RequestBody WriteArticle form,
            Device device) {

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
                StringUtils.trim(form.getCategoryCode()), ! galleries.isEmpty(), JakdukUtils.getDeviceInfo(device));

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

    @ApiOperation("게시판 글 고치기")
    @SecuredUser
    @PutMapping("/{board}/{seq}")
    public WriteArticleResponse editArticle(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "글 폼", required = true)  @Valid @RequestBody WriteArticle form,
            HttpServletRequest request,
            Device device) {

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
                form.getCategoryCode(), ! galleries.isEmpty(), JakdukUtils.getDeviceInfo(device));

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

    @ApiOperation("게시판 글 지움")
    @SecuredUser
    @DeleteMapping("/{board}/{seq}")
    public DeleteArticleResponse deleteArticle(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

		Constants.ARTICLE_DELETE_TYPE deleteType = articleService.deleteArticle(commonWriter, board, seq);

        return DeleteArticleResponse.builder()
                .result(deleteType)
                .build();
    }

    @ApiOperation("게시판 글의 댓글 목록")
    @GetMapping("/{board}/{seq}/comments")
    public GetArticleDetailCommentsResponse getArticleDetailComments(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "이 CommentId 이후부터 목록 가져옴") @RequestParam(required = false) String commentId) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        return articleService.getArticleDetailComments(commonWriter, board, seq, commentId);
    }

    @ApiOperation("게시판 글의 댓글 달기")
    @PostMapping("/{board}/{seq}/comment")
    public ArticleComment writeArticleComment(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "댓글 폼", required = true) @Valid @RequestBody WriteArticleComment form,
            Device device) {

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
                galleries, JakdukUtils.getDeviceInfo(device));

        galleryService.processLinkedGalleries(commonWriter.getUserId(), galleries, form.getGalleries(), null,
                Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, articleComment.getId());

        return articleComment;
    }

    @ApiOperation("게시판 글의 댓글 고치기")
    @PutMapping("/{board}/comment/{id}")
    public ArticleComment editArticleComment(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String id,
            @ApiParam(value = "댓글 폼", required = true) @Valid @RequestBody WriteArticleComment form,
            HttpServletRequest request,
            Device device) {

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

        ArticleComment articleComment = articleService.updateArticleComment(commonWriter, board, id, form.getContent().trim(), galleryIds,
                JakdukUtils.getDeviceInfo(device));

        List<String> galleryIdsForRemoval = JakdukUtils.getSessionOfGalleryIdsForRemoval(request,
                Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, articleComment.getId());

        galleryService.processLinkedGalleries(commonWriter.getUserId(), galleries, form.getGalleries(), galleryIdsForRemoval,
                Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, articleComment.getId());

        JakdukUtils.removeSessionOfGalleryIdsForRemoval(request, Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, articleComment.getId());

        return articleComment;

    }

    @ApiOperation("게시판 글의 댓글 지우기")
    @DeleteMapping("/{board}/comment/{id}")
    public EmptyJsonResponse deleteFreeComment(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String id) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        articleService.deleteArticleComment(commonWriter, board, id);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation("게시판 글 감정 표현")
    @PostMapping("/{board}/{seq}/{feeling}")
    public UserFeelingResponse setArticleFeeling(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "감정", required = true) @PathVariable Constants.FEELING_TYPE feeling) {

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

    @ApiOperation(value = "자유게시판 글의 감정 표현 회원 목록")
    @GetMapping("/{board}/{seq}/feeling/users")
    public GetArticleFeelingUsersResponse getArticleFeelingUsers (
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq) {

        Article article = articleService.findOneBySeq(board, seq);

        return GetArticleFeelingUsersResponse.builder()
                .seq(seq)
                .usersLiking(article.getUsersLiking())
                .usersDisliking(article.getUsersDisliking())
                .build();
    }

    @ApiOperation("자유게시판 댓글 감정 표현")
    @PostMapping("/{board}/comment/{commentId}/{feeling}")
    public UserFeelingResponse addFreeCommentFeeling(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String commentId,
            @ApiParam(value = "감정", required = true) @PathVariable Constants.FEELING_TYPE feeling) {

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

    @ApiOperation(value = "게시판 글의 공지 활성화")
    @PostMapping("/{board}/{seq}/notice")
    public EmptyJsonResponse enableArticleNotice(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
            @PathVariable Integer seq) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

		articleService.enableArticleNotice(commonWriter, board, seq);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "게시판 글의 공지 비활성화")
    @DeleteMapping("/{board}/{seq}/notice")
    public EmptyJsonResponse disableArticleNotice(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE board,
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
