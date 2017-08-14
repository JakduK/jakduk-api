package com.jakduk.api.restcontroller;

import com.jakduk.api.common.AuthHelper;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.annotation.SecuredUser;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.BoardFree;
import com.jakduk.api.model.db.BoardFreeComment;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.jongo.BoardFreeOnBest;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.UserFeelingResponse;
import com.jakduk.api.restcontroller.vo.board.*;
import com.jakduk.api.service.BoardFreeService;
import com.jakduk.api.service.GalleryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 * 16. 3. 26 오후 11:05
 */

@Api(tags = "BoardFree", description = "자유게시판 API")
@RestController
@RequestMapping("/api/board")
public class BoardRestController {

    @Autowired private BoardFreeService boardFreeService;
    @Autowired private GalleryService galleryService;
    @Autowired private AuthHelper authHelper;

    @ApiOperation("자유게시판 글 목록")
    @GetMapping("/{board}/posts")
    public FreePostsResponse getFreePosts(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "페이지 번호(1부터 시작)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "페이지 사이즈") @RequestParam(required = false, defaultValue = "20") Integer size,
            @ApiParam(value = "말머리") @RequestParam(required = false, defaultValue = "ALL") String categoryCode) {

        Constants.BOARD_TYPE boardType = Constants.BOARD_TYPE.valueOf(StringUtils.upperCase(board.name()));

        return boardFreeService.getFreePosts(boardType, categoryCode, page, size);
    }

    @ApiOperation("자유게시판 주간 선두 글")
    @GetMapping("/{board}/tops")
    public FreeTopsResponse getFreePostsTops(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board) {

        List<BoardFreeOnBest> topLikes = boardFreeService.getFreeTopLikes();
        List<BoardFreeOnBest> topComments = boardFreeService.getFreeTopComments();

        return FreeTopsResponse.builder()
                .topLikes(topLikes)
                .topComments(topComments)
                .build();
    }

    @ApiOperation("자유게시판 댓글 목록")
    @GetMapping("/{board}/comments")
    public FreePostCommentsResponse getFreeComments(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "페이지 번호(1부터 시작)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "페이지 사이즈") @RequestParam(required = false, defaultValue = "20") Integer size) {

        return boardFreeService.getBoardFreeComments(page, size);
    }

    @ApiOperation("자유게시판 글 상세")
    @GetMapping("/{board}/{seq}")
    public FreePostDetailResponse getFreePost(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            HttpServletRequest request,
            HttpServletResponse response) {

        Boolean isAddCookie = JakdukUtils.addViewsCookie(request, response, Constants.VIEWS_COOKIE_TYPE.FREE_BOARD, String.valueOf(seq));

        return boardFreeService.getBoardFreeDetail(seq, isAddCookie);
    }

    @ApiOperation("자유게시판 말머리 목록")
    @GetMapping("/{board}/categories")
    public FreeCategoriesResponse getFreeCategories(
            @ApiParam(value = "게시판", required = true, example = "FOOTBALL") @PathVariable Constants.BOARD_TYPE_LOWERCASE board) {

        Constants.BOARD_TYPE boardType = Constants.BOARD_TYPE.valueOf(StringUtils.upperCase(board.name()));

        List<BoardCategory> categories = new BoardCategoryGenerator().getCategories(boardType, JakdukUtils.getLocale());

        return FreeCategoriesResponse.builder()
                .categories(categories)
                .build();
    }

    @ApiOperation("자유게시판 글쓰기")
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

        BoardFree boardFree = boardFreeService.insertFreePost(commonWriter, boardType, form.getSubject().trim(), form.getContent().trim(),
                form.getCategoryCode().trim(), galleries, JakdukUtils.getDeviceInfo(device));

        galleryService.processLinkedGalleries(galleries, form.getGalleries(), null, Constants.GALLERY_FROM_TYPE.BOARD_FREE, boardFree.getId());

        return FreePostWriteResponse.builder()
                .seq(boardFree.getSeq())
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

        BoardFree boardFree = boardFreeService.updateFreePost(commonWriter, seq, form.getSubject().trim(), form.getContent().trim(),
                form.getCategoryCode(), galleryIds, JakdukUtils.getDeviceInfo(device));

        List<String> galleryIdsForRemoval = JakdukUtils.getSessionOfGalleryIdsForRemoval(request, Constants.GALLERY_FROM_TYPE.BOARD_FREE, boardFree.getId());

        galleryService.processLinkedGalleries(galleries, form.getGalleries(), galleryIdsForRemoval, Constants.GALLERY_FROM_TYPE.BOARD_FREE, boardFree.getId());

        JakdukUtils.removeSessionOfGalleryIdsForRemoval(request, Constants.GALLERY_FROM_TYPE.BOARD_FREE, boardFree.getId());

        return FreePostWriteResponse.builder()
                .seq(boardFree.getSeq())
                .build();
    }

    @ApiOperation(value = "자유게시판 글 지움")
    @SecuredUser
    @DeleteMapping("/{board}/{seq}")
    public FreePostDeleteResponse deleteFree(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

		Constants.BOARD_DELETE_TYPE deleteType = boardFreeService.deleteFreePost(commonWriter, seq);

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

        return boardFreeService.getBoardFreeDetailComments(seq, commentId);
    }

    @ApiOperation("자유게시판 글의 댓글 달기")
    @PostMapping("/{board}/comment")
    public BoardFreeComment addFreeComment(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "댓글 폼", required = true) @Valid @RequestBody BoardCommentForm form,
            Device device) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        // 연관된 사진 id 배열 (검증 전)
        List<String> unverifiableGalleryIds = null;

        if (! ObjectUtils.isEmpty(form.getGalleries())) {
            unverifiableGalleryIds = form.getGalleries().stream()
                    .map(GalleryOnBoard::getId)
                    .collect(Collectors.toList());
        }

        List<Gallery> galleries = galleryService.findByIdIn(unverifiableGalleryIds);

        BoardFreeComment boardFreeComment =  boardFreeService.insertFreeComment(form.getSeq(), commonWriter, form.getContent().trim(),
                galleries, JakdukUtils.getDeviceInfo(device));

        galleryService.processLinkedGalleries(galleries, form.getGalleries(), null,
                Constants.GALLERY_FROM_TYPE.BOARD_FREE_COMMENT, boardFreeComment.getId());

        return boardFreeComment;
    }

    @ApiOperation("자유게시판 글의 댓글 고치기")
    @SecuredUser
    @PutMapping("/{board}/comment/{id}")
    public BoardFreeComment editFreeComment(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String id,
            @ApiParam(value = "댓글 폼", required = true) @Valid @RequestBody BoardCommentForm form,
            HttpServletRequest request,
            Device device) {

        if (! AuthUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

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

        BoardFreeComment boardFreeComment = boardFreeService.updateFreeComment(id, form.getSeq(), commonWriter, form.getContent().trim(), galleryIds,
                JakdukUtils.getDeviceInfo(device));

        List<String> galleryIdsForRemoval = JakdukUtils.getSessionOfGalleryIdsForRemoval(request,
                Constants.GALLERY_FROM_TYPE.BOARD_FREE_COMMENT, boardFreeComment.getId());

        galleryService.processLinkedGalleries(galleries, form.getGalleries(), galleryIdsForRemoval,
                Constants.GALLERY_FROM_TYPE.BOARD_FREE_COMMENT, boardFreeComment.getId());

        JakdukUtils.removeSessionOfGalleryIdsForRemoval(request, Constants.GALLERY_FROM_TYPE.BOARD_FREE_COMMENT, boardFreeComment.getId());

        return boardFreeComment;

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

        boardFreeService.deleteFreeComment(id, commonWriter);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation("자유게시판 글 감정 표현")
    @PostMapping("/{board}/{seq}/{feeling}")
    public UserFeelingResponse addFreeFeeling(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "감정", required = true) @PathVariable Constants.FEELING_TYPE feeling) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        BoardFree boardFree = boardFreeService.setFreeFeelings(commonWriter, seq, feeling);

        UserFeelingResponse response = UserFeelingResponse.builder()
                .numberOfLike(CollectionUtils.isEmpty(boardFree.getUsersLiking()) ? 0 : boardFree.getUsersLiking().size())
                .numberOfDislike(CollectionUtils.isEmpty(boardFree.getUsersDisliking()) ? 0 : boardFree.getUsersDisliking().size())
                .build();

        if (Objects.nonNull(commonWriter))
            response.setMyFeeling(JakdukUtils.getMyFeeling(commonWriter, boardFree.getUsersLiking(), boardFree.getUsersDisliking()));

        return response;
    }

    @ApiOperation(value = "자유게시판 글의 감정 표현 회원 목록")
    @RequestMapping(value = "/{board}/{seq}/feeling/users", method = RequestMethod.GET)
    public FreePostFeelingsResponse getFreeFeelings (
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq) {

        BoardFree boardFree = boardFreeService.findOneBySeq(seq);

        return FreePostFeelingsResponse.builder()
                .seq(seq)
                .usersLiking(boardFree.getUsersLiking())
                .usersDisliking(boardFree.getUsersDisliking())
                .build();
    }

    @ApiOperation("자유게시판 댓글 감정 표현")
    @PostMapping("/{board}/comment/{commentId}/{feeling}")
    public UserFeelingResponse addFreeCommentFeeling(
            @ApiParam(value = "게시판", required = true) @PathVariable Constants.BOARD_TYPE_LOWERCASE board,
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String commentId,
            @ApiParam(value = "감정", required = true) @PathVariable Constants.FEELING_TYPE feeling) {

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        BoardFreeComment boardComment = boardFreeService.setFreeCommentFeeling(commonWriter, commentId, feeling);

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

		boardFreeService.setFreeNotice(commonWriter, seq, true);

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

        boardFreeService.setFreeNotice(commonWriter, seq, false);

        return EmptyJsonResponse.newInstance();
    }

}
