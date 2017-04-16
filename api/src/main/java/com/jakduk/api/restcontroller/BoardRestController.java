package com.jakduk.api.restcontroller;

import com.jakduk.api.common.ApiConst;
import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.UserFeelingResponse;
import com.jakduk.api.service.BoardFreeService;
import com.jakduk.api.service.GalleryService;
import com.jakduk.api.vo.board.*;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.db.BoardFreeComment;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.etc.BoardFreeOnBest;
import com.jakduk.core.model.simple.BoardFreeOfMinimum;
import com.jakduk.core.model.simple.BoardFreeOnSearch;
import com.jakduk.core.service.BoardCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mobile.device.Device;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 * 16. 3. 26 오후 11:05
 */

@Api(tags = "BoardFree", description = "자유게시판 API")
@RestController
@RequestMapping("/api/board/free")
public class BoardRestController {

    @Autowired
    private BoardFreeService boardFreeService;

    @Autowired
    private BoardCategoryService boardCategoryService;

    @Autowired
    private GalleryService galleryService;

    @ApiOperation(value = "자유게시판 글 목록")
    @GetMapping("/posts")
    public FreePostsResponse getFreePosts(
            @ApiParam(value = "페이지 번호(1부터 시작)") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "페이지 사이즈") @RequestParam(required = false, defaultValue = "20") Integer size,
            @ApiParam(value = "말머리") @RequestParam(required = false, defaultValue = "ALL") CoreConst.BOARD_CATEGORY_TYPE category) {

        return boardFreeService.getFreePosts(category, page, size);
    }

    @ApiOperation(value = "자유게시판 주간 선두 글")
    @RequestMapping(value = "/tops", method = RequestMethod.GET)
    public FreeTopsResponse getFreePostsTops() {

        List<BoardFreeOnBest> topLikes = boardFreeService.getFreeTopLikes();
        List<BoardFreeOnBest> topComments = boardFreeService.getFreeTopComments();

        return FreeTopsResponse.builder()
                .topLikes(topLikes)
                .topComments(topComments)
                .build();
    }

    @ApiOperation(value = "자유게시판 댓글 목록")
    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public FreeCommentsOnListResponse getFreeComments(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {

        Page<BoardFreeComment> comments = boardFreeService.getBoardFreeComments(page, size);

        // id 뽑아내기.
        List<ObjectId> boardIds = comments.getContent().stream()
                .map(comment -> {
                    String tempId = comment.getBoardItem().getId();
                    return new ObjectId(tempId);
                })
                .distinct()
                .collect(Collectors.toList());

        Map<String, BoardFreeOnSearch> postsHavingComments = boardFreeService.getBoardFreeOnSearchByIds(boardIds);

        List<FreeCommentsOnList> freeComments = comments.getContent().stream()
                .map(comment -> {
                            FreeCommentsOnList newComment = new FreeCommentsOnList();
                            BeanUtils.copyProperties(comment, newComment);
                            newComment.setBoardItem(
                                    Optional.ofNullable(postsHavingComments.get(comment.getBoardItem().getId()))
                                            .orElse(new BoardFreeOnSearch())
                            );
                            return newComment;
                        }
                )
                .collect(Collectors.toList());

        return FreeCommentsOnListResponse.builder()
                .comments(freeComments)
                .first(comments.isFirst())
                .last(comments.isLast())
                .totalPages(comments.getTotalPages())
                .totalElements(comments.getTotalElements())
                .numberOfElements(comments.getNumberOfElements())
                .size(comments.getSize())
                .number(comments.getNumber())
                .build();
    }

    @ApiOperation(value = "자유게시판 글 상세")
    @GetMapping("/{seq}")
    public FreePostDetailResponse getFreePost(
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            HttpServletRequest request,
            HttpServletResponse response) {

        Boolean isAddCookie = ApiUtils.addViewsCookie(request, response, ApiConst.VIEWS_COOKIE_TYPE.FREE_BOARD, String.valueOf(seq));

        return boardFreeService.getBoardFreeDetail(seq, isAddCookie);
    }

    @ApiOperation(value = "자유게시판 말머리 목록")
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public FreeCategoriesResponse getFreeCategories() {

        List<BoardCategory> categories = boardCategoryService.getFreeCategories();

        return FreeCategoriesResponse.builder()
                .categories(categories)
                .build();
    }

    @ApiOperation(value = "자유게시판 글쓰기")
    @PostMapping("")
    public FreePostWriteResponse addFreePost(
            @ApiParam(value = "글 폼", required = true) @Valid @RequestBody FreePostForm form,
            Device device) {

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        // 연관된 사진 id 배열 (검증 전)
        List<String> unverifiableGalleryIds = null;

        if (! ObjectUtils.isEmpty(form.getGalleries())) {
            unverifiableGalleryIds = form.getGalleries().stream()
                    .map(GalleryOnBoard::getId)
                    .collect(Collectors.toList());
        }

        List<Gallery> galleries = galleryService.findByIdIn(unverifiableGalleryIds);

        BoardFree boardFree = boardFreeService.insertFreePost(commonWriter, form.getSubject().trim(), form.getContent().trim(),
                form.getCategoryCode(), galleries, ApiUtils.getDeviceInfo(device));

        galleryService.processLinkedGalleries(galleries, form.getGalleries(), null, CoreConst.GALLERY_FROM_TYPE.BOARD_FREE, boardFree.getId());

        return FreePostWriteResponse.builder()
                .seq(boardFree.getSeq())
                .build();
    }

    @ApiOperation(value = "자유게시판 글 고치기")
    @PutMapping("/{seq}")
    public FreePostWriteResponse editFreePost(
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "글 폼", required = true)  @Valid @RequestBody FreePostForm form,
            HttpServletRequest request,
            Device device) {

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        // 연관된 사진 id 배열 (검증 전)
        List<String> unverifiableGalleryIds = null;

        if (! ObjectUtils.isEmpty(form.getGalleries())) {
            unverifiableGalleryIds = form.getGalleries().stream()
                    .map(GalleryOnBoard::getId)
                    .collect(Collectors.toList());
        }

        List<Gallery> galleries = galleryService.findByIdIn(unverifiableGalleryIds);

        BoardFree boardFree = boardFreeService.updateFreePost(commonWriter, seq, form.getSubject().trim(), form.getContent().trim(),
                form.getCategoryCode(), galleries, ApiUtils.getDeviceInfo(device));

        List<String> galleryIdsForRemoval = ApiUtils.getSessionOfGalleryIdsForRemoval(request, CoreConst.GALLERY_FROM_TYPE.BOARD_FREE, boardFree.getId());

        galleryService.processLinkedGalleries(galleries, form.getGalleries(), galleryIdsForRemoval, CoreConst.GALLERY_FROM_TYPE.BOARD_FREE, boardFree.getId());

        ApiUtils.removeSessionOfGalleryIdsForRemoval(request, CoreConst.GALLERY_FROM_TYPE.BOARD_FREE, boardFree.getId());

        return FreePostWriteResponse.builder()
                .seq(boardFree.getSeq())
                .build();
    }

    @ApiOperation(value = "자유게시판 글 지움")
    @RequestMapping(value = "/{seq}", method = RequestMethod.DELETE)
    public FreePostOnDeleteResponse deleteFree(@PathVariable Integer seq) {

        CommonWriter commonWriter = UserUtils.getCommonWriter();

		CoreConst.BOARD_DELETE_TYPE deleteType = boardFreeService.deleteFreePost(commonWriter, seq);

        return FreePostOnDeleteResponse.builder().result(deleteType).build();
    }

    @ApiOperation(value = "자유게시판 글의 댓글 목록")
    @RequestMapping(value = "/{seq}/comments", method = RequestMethod.GET)
    public FreePostCommentsResponse getFreePostComments(
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "이 CommentId 이후부터 목록 가져옴") @RequestParam(required = false) String commentId) {

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        BoardFreeOfMinimum boardFreeOnComment = boardFreeService.findBoardFreeOfMinimumBySeq(seq);

        List<BoardFreeComment> boardComments = boardFreeService.getFreeComments(seq, commentId);

        BoardItem boardItem = new BoardItem(boardFreeOnComment.getId(), boardFreeOnComment.getSeq());

        Integer count = boardFreeService.countCommentsByBoardItem(boardItem);

        List<FreePostComment> postComments = boardComments.stream()
                .map(boardFreeComment -> {
                    FreePostComment freePostComment = new FreePostComment();
                    BeanUtils.copyProperties(boardFreeComment, freePostComment);

                    Integer numberOfLike = ObjectUtils.isEmpty(boardFreeComment.getUsersLiking()) ? 0 : boardFreeComment.getUsersLiking().size();
                    Integer numberOfDisLike = ObjectUtils.isEmpty(boardFreeComment.getUsersDisliking()) ? 0 : boardFreeComment.getUsersDisliking().size();

                    freePostComment.setNumberOfLike(numberOfLike);
                    freePostComment.setNumberOfDislike(numberOfDisLike);

                    if (Objects.nonNull(commonWriter))
                        freePostComment.setMyFeeling(ApiUtils.getMyFeeling(commonWriter, boardFreeComment.getUsersLiking(),
                                boardFreeComment.getUsersDisliking()));

                    return freePostComment;
                })
                .collect(Collectors.toList());

        return FreePostCommentsResponse.builder()
                .comments(postComments)
                .count(count)
                .build();
    }

    @ApiOperation(value = "자유게시판 글의 댓글 달기")
    @PostMapping("/comment")
    public BoardFreeComment addFreeComment(
            @ApiParam(value = "댓글 폼", required = true) @Valid @RequestBody BoardCommentForm form,
            Device device) {

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        return boardFreeService.insertFreeComment(form.getSeq(), commonWriter, form.getContent().trim(), form.getGalleries(),
                ApiUtils.getDeviceInfo(device));
    }

    @ApiOperation(value = "자유게시판 글의 댓글 고치기")
    @PutMapping(value ="/comment/{id}")
    public BoardFreeComment editFreeComment(
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String id,
            @ApiParam(value = "댓글 폼", required = true) @Valid @RequestBody BoardCommentForm form,
            Device device) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        return boardFreeService.updateFreeComment(id, form.getSeq(), commonWriter, form.getContent().trim(), form.getGalleries(),
                ApiUtils.getDeviceInfo(device));

    }

    @ApiOperation(value = "자유게시판 글의 댓글 지우기")
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE)
    public EmptyJsonResponse deleteFreeComment(
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String id) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        boardFreeService.deleteFreeComment(id, commonWriter);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "자유게시판 글 감정 표현")
    @RequestMapping(value = "/{seq}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse addFreeFeeling(
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq,
            @ApiParam(value = "감정", required = true) @PathVariable CoreConst.FEELING_TYPE feeling) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS, CoreUtils.getExceptionMessage("exception.need.login.to.feel"));

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        BoardFree boardFree = boardFreeService.setFreeFeelings(commonWriter, seq, feeling);

        Integer numberOfLike = ObjectUtils.isEmpty(boardFree.getUsersLiking()) ? 0 : boardFree.getUsersLiking().size();
        Integer numberOfDisLike = ObjectUtils.isEmpty(boardFree.getUsersDisliking()) ? 0 : boardFree.getUsersDisliking().size();

        UserFeelingResponse response = UserFeelingResponse.builder()
                .numberOfLike(numberOfLike)
                .numberOfDislike(numberOfDisLike)
                .build();

        if (Objects.nonNull(commonWriter))
            response.setMyFeeling(ApiUtils.getMyFeeling(commonWriter, boardFree.getUsersLiking(), boardFree.getUsersDisliking()));

        return response;
    }

    @ApiOperation(value = "자유게시판 글의 감정 표현 회원 목록")
    @RequestMapping(value = "/{seq}/feeling/users", method = RequestMethod.GET)
    public FreePostFeelingsResponse getFreeFeelings (
            @ApiParam(value = "글 seq", required = true) @PathVariable Integer seq) {

        BoardFree boardFree = boardFreeService.findOneBySeq(seq);

        return FreePostFeelingsResponse.builder()
                .seq(seq)
                .usersLiking(boardFree.getUsersLiking())
                .usersDisliking(boardFree.getUsersDisliking())
                .build();
    }

    @ApiOperation(value = "자유게시판 댓글 감정 표현")
    @RequestMapping(value = "/comment/{commentId}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse addFreeCommentFeeling(
            @ApiParam(value = "댓글 ID", required = true) @PathVariable String commentId,
            @ApiParam(value = "감정", required = true) @PathVariable CoreConst.FEELING_TYPE feeling) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS, CoreUtils.getExceptionMessage("exception.need.login.to.feel"));

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        BoardFreeComment boardComment = boardFreeService.setFreeCommentFeeling(commonWriter, commentId, feeling);

        Integer numberOfLike = ObjectUtils.isEmpty(boardComment.getUsersLiking()) ? 0 : boardComment.getUsersLiking().size();
        Integer numberOfDisLike = ObjectUtils.isEmpty(boardComment.getUsersDisliking()) ? 0 : boardComment.getUsersDisliking().size();

        UserFeelingResponse response = UserFeelingResponse.builder()
                .numberOfLike(numberOfLike)
                .numberOfDislike(numberOfDisLike)
                .build();

        if (Objects.nonNull(commonWriter))
            response.setMyFeeling(ApiUtils.getMyFeeling(commonWriter, boardComment.getUsersLiking(), boardComment.getUsersDisliking()));

        return response;
    }

    @ApiOperation(value = "자유게시판 글의 공지 활성화")
    @RequestMapping(value = "/{seq}/notice", method = RequestMethod.POST)
    public EmptyJsonResponse enableFreeNotice(@PathVariable int seq) {

        if (! UserUtils.isAdmin())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonWriter commonWriter = UserUtils.getCommonWriter();

		boardFreeService.setFreeNotice(commonWriter, seq, true);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "자유게시판 글의 공지 비활성화")
    @RequestMapping(value = "/{seq}/notice", method = RequestMethod.DELETE)
    public EmptyJsonResponse disableFreeNotice(@PathVariable int seq) {

        if (! UserUtils.isAdmin())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonWriter commonWriter = UserUtils.getCommonWriter();

        boardFreeService.setFreeNotice(commonWriter, seq, false);

        return EmptyJsonResponse.newInstance();
    }

}
