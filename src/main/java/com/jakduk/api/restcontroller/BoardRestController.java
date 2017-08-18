package com.jakduk.api.restcontroller;

import com.jakduk.api.common.AuthHelper;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.annotation.SecuredUser;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.aggregate.BoardPostTop;
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
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
    @Autowired private AuthHelper authHelper;

    @ApiOperation("게시판 글 목록")
    @GetMapping("/{board}/posts")
    public FreePostsResponse getFreePosts(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "페이지 번호(1부터 시작)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "페이지 사이즈") @RequestParam(required = false, defaultValue = "20") Integer size,
            @ApiParam(value = "말머리") @RequestParam(required = false, defaultValue = "ALL") String categoryCode) {

        Constants.BOARD_TYPE boardType = Constants.BOARD_TYPE.valueOf(StringUtils.upperCase(board.name()));

        return articleService.getFreePosts(boardType, categoryCode, page, size);
    }

    @ApiOperation("게시판 주간 선두 글")
    @GetMapping("/{board}/tops")
    public FreeTopsResponse getFreePostsTops(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board) {

        Constants.BOARD_TYPE boardType = Constants.BOARD_TYPE.valueOf(StringUtils.upperCase(board.name()));

        LocalDate localDate = LocalDate.now().minusWeeks(1);
        ObjectId objectId = new ObjectId(DateUtils.localDateToDate(localDate));

        List<BoardPostTop> topLikes = articleService.getFreeTopLikes(boardType, objectId);
        List<BoardPostTop> topComments = articleService.getFreeTopComments(boardType, objectId);

        return FreeTopsResponse.builder()
                .topLikes(topLikes)
                .topComments(topComments)
                .build();
    }

    @ApiOperation("게시판 댓글 목록")
    @GetMapping("/{board}/comments")
    public FreePostCommentsResponse getFreeComments(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "페이지 번호(1부터 시작)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "페이지 사이즈") @RequestParam(required = false, defaultValue = "20") Integer size,
            Authentication authentication) {

        Constants.BOARD_TYPE boardType = Constants.BOARD_TYPE.valueOf(StringUtils.upperCase(board.name()));

        CommonWriter commonWriter = authHelper.getCommonWriter(authentication);

        return articleService.getBoardFreeComments(commonWriter, boardType, page, size);
    }

    @ApiOperation("자유게시판 글 상세")
    @GetMapping("/{board}/{seq}")
    public FreePostDetailResponse getFreePost(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            HttpServletRequest request,
            HttpServletResponse response) {

        Boolean isAddCookie = JakdukUtils.addViewsCookie(request, response, Constants.VIEWS_COOKIE_TYPE.FREE_BOARD, String.valueOf(seq));

        return articleService.getBoardFreeDetail(seq, isAddCookie);
    }

    @ApiOperation("게시판 말머리 목록")
    @GetMapping("/{board}/categories")
    public FreeCategoriesResponse getFreeCategories(
            @ApiParam(value = "게시판", required = true, example = "FOOTBALL") @PathVariable Constants.BOARD_TYPE_LOWERCASE board) {

        Constants.BOARD_TYPE boardType = Constants.BOARD_TYPE.valueOf(StringUtils.upperCase(board.name()));

        List<BoardCategory> categories = new BoardCategoryGenerator().getCategories(boardType, JakdukUtils.getLocale());

        return FreeCategoriesResponse.builder()
                .categories(categories)
                .build();
    }

    @ApiOperation("게시판 글쓰기")
    @PostMapping("/{board}")
    public FreePostWriteResponse addFreePost(
            @ApiParam(value = "게시판", required = true, example = "FOOTBALL") @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 폼", required = true) @Valid @RequestBody FreePostForm form,
            Device device,
            Authentication authentication) {

        Constants.BOARD_TYPE boardType = Constants.BOARD_TYPE.valueOf(StringUtils.upperCase(board.name()));

        CommonWriter commonWriter = authHelper.getCommonWriter(authentication);

        // 연관된 사진 id 배열 (검증 전)
        List<String> unverifiableGalleryIds = null;

        if (! CollectionUtils.isEmpty(form.getGalleries())) {
            unverifiableGalleryIds = form.getGalleries().stream()
                    .map(GalleryOnBoard::getId)
                    .collect(Collectors.toList());
        }

        List<Gallery> galleries = galleryService.findByIdIn(unverifiableGalleryIds);

        Article article = articleService.insertFreePost(commonWriter, boardType, form.getSubject().trim(), form.getContent().trim(),
                form.getCategoryCode().trim(), galleries, JakdukUtils.getDeviceInfo(device));

        galleryService.processLinkedGalleries(galleries, form.getGalleries(), null, Constants.GALLERY_FROM_TYPE.BOARD_FREE, article.getId());

        return FreePostWriteResponse.builder()
                .seq(article.getSeq())
                .build();
    }

    @ApiOperation("자유게시판 글 고치기")
    @SecuredUser
    @PutMapping("/{board}/{seq}")
    public FreePostWriteResponse editFreePost(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "글 폼", required = true)  @Valid @RequestBody FreePostForm form,
            HttpServletRequest request,
            Device device,
            Authentication authentication) {

        CommonWriter commonWriter = authHelper.getCommonWriter(authentication);

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

        Article article = articleService.updateFreePost(commonWriter, seq, form.getSubject().trim(), form.getContent().trim(),
                form.getCategoryCode(), galleryIds, JakdukUtils.getDeviceInfo(device));

        List<String> galleryIdsForRemoval = JakdukUtils.getSessionOfGalleryIdsForRemoval(request, Constants.GALLERY_FROM_TYPE.BOARD_FREE, article.getId());

        galleryService.processLinkedGalleries(galleries, form.getGalleries(), galleryIdsForRemoval, Constants.GALLERY_FROM_TYPE.BOARD_FREE, article.getId());

        JakdukUtils.removeSessionOfGalleryIdsForRemoval(request, Constants.GALLERY_FROM_TYPE.BOARD_FREE, article.getId());

        return FreePostWriteResponse.builder()
                .seq(article.getSeq())
                .build();
    }

    @ApiOperation(value = "자유게시판 글 지움")
    @SecuredUser
    @DeleteMapping("/{board}/{seq}")
    public FreePostDeleteResponse deleteFree(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

		Constants.BOARD_DELETE_TYPE deleteType = articleService.deleteFreePost(commonWriter, seq);

        return FreePostDeleteResponse.builder()
                .result(deleteType)
                .build();
    }

    @ApiOperation("자유게시판 글의 댓글 목록")
    @GetMapping("/{board}/{seq}/comments")
    public FreePostDetailCommentsResponse getFreePostDetailComments(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "이 CommentId 이후부터 목록 가져옴") @RequestParam(required = false) String commentId) {

        return articleService.getBoardFreeDetailComments(seq, commentId);
    }

    @ApiOperation("자유게시판 글의 댓글 달기")
    @PostMapping("/{board}/{seq}/comment")
    public ArticleComment addFreeComment(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "댓글 폼", required = true) @Valid @RequestBody BoardCommentForm form,
            Device device,
            Authentication authentication) {

        CommonWriter commonWriter = authHelper.getCommonWriter(authentication);

        // 연관된 사진 id 배열 (검증 전)
        List<String> unverifiableGalleryIds = null;

        if (! CollectionUtils.isEmpty(form.getGalleries())) {
            unverifiableGalleryIds = form.getGalleries().stream()
                    .map(GalleryOnBoard::getId)
                    .collect(Collectors.toList());
        }

        List<Gallery> galleries = galleryService.findByIdIn(unverifiableGalleryIds);

        ArticleComment articleComment =  articleService.insertFreeComment(seq, commonWriter, form.getContent().trim(),
                galleries, JakdukUtils.getDeviceInfo(device));

        galleryService.processLinkedGalleries(galleries, form.getGalleries(), null,
                Constants.GALLERY_FROM_TYPE.BOARD_FREE_COMMENT, articleComment.getId());

        return articleComment;
    }

    @ApiOperation("자유게시판 글의 댓글 고치기")
    @SecuredUser
    @PutMapping("/{board}/{seq}/comment/{id}")
    public ArticleComment editFreeComment(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String id,
            @ApiParam(value = "댓글 폼", required = true) @Valid @RequestBody BoardCommentForm form,
            HttpServletRequest request,
            Device device,
            Authentication authentication) {

        if (! AuthUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonWriter commonWriter = authHelper.getCommonWriter(authentication);

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

        ArticleComment articleComment = articleService.updateFreeComment(id, seq, commonWriter, form.getContent().trim(), galleryIds,
                JakdukUtils.getDeviceInfo(device));

        List<String> galleryIdsForRemoval = JakdukUtils.getSessionOfGalleryIdsForRemoval(request,
                Constants.GALLERY_FROM_TYPE.BOARD_FREE_COMMENT, articleComment.getId());

        galleryService.processLinkedGalleries(galleries, form.getGalleries(), galleryIdsForRemoval,
                Constants.GALLERY_FROM_TYPE.BOARD_FREE_COMMENT, articleComment.getId());

        JakdukUtils.removeSessionOfGalleryIdsForRemoval(request, Constants.GALLERY_FROM_TYPE.BOARD_FREE_COMMENT, articleComment.getId());

        return articleComment;

    }

    @ApiOperation("자유게시판 글의 댓글 지우기")
    @SecuredUser
    @DeleteMapping("/{board}/comment/{id}")
    public EmptyJsonResponse deleteFreeComment(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String id) {

        if (! AuthUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        articleService.deleteFreeComment(id, commonWriter);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation("자유게시판 글 감정 표현")
    @PostMapping("/{board}/{seq}/{feeling}")
    public UserFeelingResponse addFreeFeeling(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "감정", required = true) @PathVariable Constants.FEELING_TYPE feeling) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        Article article = articleService.setFreeFeelings(commonWriter, seq, feeling);

        UserFeelingResponse response = UserFeelingResponse.builder()
                .numberOfLike(CollectionUtils.isEmpty(article.getUsersLiking()) ? 0 : article.getUsersLiking().size())
                .numberOfDislike(CollectionUtils.isEmpty(article.getUsersDisliking()) ? 0 : article.getUsersDisliking().size())
                .build();

        if (Objects.nonNull(commonWriter))
            response.setMyFeeling(JakdukUtils.getMyFeeling(commonWriter, article.getUsersLiking(), article.getUsersDisliking()));

        return response;
    }

    @ApiOperation(value = "자유게시판 글의 감정 표현 회원 목록")
    @RequestMapping(value = "/{board}/{seq}/feeling/users", method = RequestMethod.GET)
    public FreePostFeelingsResponse getFreeFeelings (
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq) {

        Article article = articleService.findOneBySeq(seq);

        return FreePostFeelingsResponse.builder()
                .seq(seq)
                .usersLiking(article.getUsersLiking())
                .usersDisliking(article.getUsersDisliking())
                .build();
    }

    @ApiOperation("자유게시판 댓글 감정 표현")
    @PostMapping("/{board}/comment/{commentId}/{feeling}")
    public UserFeelingResponse addFreeCommentFeeling(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String commentId,
            @ApiParam(value = "감정", required = true) @PathVariable Constants.FEELING_TYPE feeling) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        ArticleComment boardComment = articleService.setFreeCommentFeeling(commonWriter, commentId, feeling);

        UserFeelingResponse response = UserFeelingResponse.builder()
                .numberOfLike(CollectionUtils.isEmpty(boardComment.getUsersLiking()) ? 0 : boardComment.getUsersLiking().size())
                .numberOfDislike(CollectionUtils.isEmpty(boardComment.getUsersDisliking()) ? 0 : boardComment.getUsersDisliking().size())
                .build();

        if (Objects.nonNull(commonWriter))
            response.setMyFeeling(JakdukUtils.getMyFeeling(commonWriter, boardComment.getUsersLiking(), boardComment.getUsersDisliking()));

        return response;
    }

    @ApiOperation(value = "자유게시판 글의 공지 활성화")
    @RequestMapping(value = "/{board}/{seq}/notice", method = RequestMethod.POST)
    public EmptyJsonResponse enableFreeNotice(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @PathVariable int seq) {

        if (! AuthUtils.isAdmin())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

		articleService.setFreeNotice(commonWriter, seq, true);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "자유게시판 글의 공지 비활성화")
    @RequestMapping(value = "/{board}/{seq}/notice", method = RequestMethod.DELETE)
    public EmptyJsonResponse disableFreeNotice(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @PathVariable int seq) {

        if (! AuthUtils.isAdmin())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        articleService.setFreeNotice(commonWriter, seq, false);

        return EmptyJsonResponse.newInstance();
    }

}
